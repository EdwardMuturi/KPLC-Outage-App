package com.kplc.outage.outage.utils


import io.github.aakira.napier.Napier
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

suspend fun <T : Any> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(data = apiCall.invoke())
    } catch (e: RedirectResponseException) { // 3xx errors
        Napier.e("RedirectResponseException: ${e.response.status.value}")
        val networkError = parseNetworkError(
            errorResponse = e.response,
            exception = e,
        )
        NetworkResult.Error(
            errorCode = e.response.status.value,
            errorMessage = networkError.message ?: networkError.errors.firstOrNull()
            ?: e.message,
        )
    } catch (e: ClientRequestException) { // 4xx errors
        Napier.e("ClientRequestException: ${e.response.status.value}")
        val networkError = parseNetworkError(
            errorResponse = e.response,
            exception = e,
        )

        NetworkResult.Error(
            errorCode = e.response.status.value,
            errorMessage = networkError.message ?: networkError.message
            ?: e.message,
        )
    } catch (e: ServerResponseException) { // 5xx errors
        Napier.e("ServerResponseException: ${e.response.status.value}")
        val networkError = parseNetworkError(
            errorResponse = e.response,
            exception = e,
        )
        NetworkResult.Error(
            errorCode = e.response.status.value,
            errorMessage = networkError.message ?: networkError.errors.firstOrNull()
            ?: e.message,
        )
    } catch (e: UnresolvedAddressException) {
        Napier.e("UnresolvedAddressException: ${e.message}")
        val networkError = parseNetworkError(
            exception = e,
        )
        NetworkResult.Error(
            errorCode = 0,
            errorMessage = networkError.message ?: networkError.message
            ?: e.message,
        )
    } catch (e: Exception) {
        Napier.e("Exception: ${e.message}")
        NetworkResult.Error(
            errorCode = 0,
            errorMessage = "An unknown error occurred",
        )
    }
}

internal suspend fun parseNetworkError(
    errorResponse: HttpResponse? = null,
    exception: Exception? = null,
): ErrorResponse {
    return errorResponse?.bodyAsText()?.let {
        Napier.e("Error response: $it")
        Json.decodeFromString(ErrorResponse.serializer(), it)
    } ?: ErrorResponse(
        message = exception?.message ?: "An unknown error occurred",
    )
}


sealed class NetworkResult<T>(
    val data: T? = null,
    val errorCode: Int? = null,
    val errorMessage: String? = null,
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(errorCode: Int, errorMessage: String?) :
        NetworkResult<T>(errorCode = errorCode, errorMessage = errorMessage)
}

@Serializable
data class ErrorResponse(
    @SerialName("errors")
    val errors: List<String> = emptyList(),
    @SerialName("message")
    val message: String? = null,
)