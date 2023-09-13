package com.kplc.outage.android.outage.presentaion.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.kplc.outage.android.R
import com.kplc.outage.android.outage.presentaion.screens.destinations.OutageDetailsScreenDestination
import com.kplc.outage.outage.model.OutageInformation
import com.kplc.outage.outage.model.OutageInformationUiState
import com.kplc.outage.outage.presentation.OutageViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

val outageBlue500 = Color(0XFF0B4A7A)

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination(start = true)
fun OutagesScreen(outageViewModel: OutageViewModel = get(), navigator: DestinationsNavigator) {
    val outageInformation by outageViewModel.outageInformationUiState.collectAsState()
    var url by remember { mutableStateOf(TextFieldValue("")) }
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    var refreshing by remember { mutableStateOf(outageInformation.isLoading) }
    var isInputValid by remember { mutableStateOf(true) }

//    outageViewModel.fetchOutages(url.text)
    LaunchedEffect(key1 = url, block = {
        outageViewModel.fetchOutages()
    })

    BottomSheetScaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                contentColor = Color.Black,
                title = {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.power_interruptions),
                            fontWeight = FontWeight.Medium,
                        )

                        Icon(imageVector = Icons.Default.Add,
                            contentDescription = "Add url",
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .clickable { scope.launch { scaffoldState.bottomSheetState.expand() } })
                    }
                },
            )
        },
        sheetContent = {
            Column(Modifier.padding(20.dp)) {
                Text(
                    text = "Load url data",
                    style = MaterialTheme.typography.h6,
                    color = outageBlue500,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                )
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = { Text(text = stringResource(R.string.paste_url)) },
                    singleLine = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            modifier = Modifier.clickable { url = TextFieldValue("") }
                        )
                    },
                )
                if (!isInputValid)
                    Text(
                        text = "Url cannot be empty",
                        style = MaterialTheme.typography.caption,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(.5f)
                        .padding(top = 20.dp)
                        .height(50.dp),
                    onClick = {
                        isInputValid = url.text.isNotEmpty()
                        if (url.text.isEmpty())
                            return@Button

                        outageViewModel.fetchOutages(url.text)
                        scope.launch { scaffoldState.bottomSheetState.collapse() }
                        url = TextFieldValue("")
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = outageBlue500,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Load Data")
                }
            }
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp
    ) { padding ->
        OutagesScreenContent(
            padding = padding,
            outageInformation = outageInformation,
            searchValue = outageViewModel.searchValue.collectAsState().value,
            showMoreDetails = { state ->
                navigator.navigate(OutageDetailsScreenDestination(outageInformationUiState = state).route)
            },
            onSearchValueChange = {
                outageViewModel.setSearchValue(it)
                outageViewModel.fetchOutages(it)
            },
        )
    }
}

@Composable
private fun OutagesScreenContent(
    padding: PaddingValues,
    outageInformation: OutageInformation,
    showMoreDetails: (OutageInformationUiState) -> Unit,
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .padding(20.dp)
            .fillMaxSize(),
    ) {
        item {
            OutlinedTextField(
                value = searchValue,
                onValueChange = onSearchValueChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = stringResource(R.string.search)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search),
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_filter_list_24),
                        contentDescription = "Filter",
                        tint = outageBlue500,
                    )
                },
            )
        }
        item {
            when (outageInformation.isLoading) {
                true -> Column(Modifier.fillMaxWidth()) { CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color = outageBlue500) }
                false -> {
                    if (outageInformation.outages.isEmpty()) {
                        Text(text = "No outage data found at the moment, please try again later")
                    }
                    outageInformation.outages.forEach { outage ->
                        OutageCard(outage) {
                            showMoreDetails(outage)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OutageCard(
    outage: OutageInformationUiState,
    onViewDetails: () -> Unit,
) {
    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(2.dp),
        modifier = Modifier
            .padding(vertical = 10.dp),
    ) {
        val borderModifier = if (outage.part.isEmpty()) {
            Modifier.height(140.dp)
        } else {
            Modifier.height(180.dp)
        }

        Box(Modifier.fillMaxWidth()) {
            Spacer(
                Modifier
                    .align(Alignment.TopStart)
                    .width(3.dp)
                    .then(borderModifier)
                    .background(color = Color(0XFF0B4A7A)),
            )

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    OutageTextBlue(outage.region)
                    OutageText(text = outage.area)
                }

                if (outage.part.isNotEmpty()) {
                    OutageTextBlue(outage.part)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    OutageText(text = outage.date)
                    OutageText(text = "${outage.startTime} - ${outage.endTime}")
                }

                OutageAnnotatedText(outage)
            }
        }
    }
}

@Composable
private fun OutageAnnotatedText(outage: OutageInformationUiState) {
    val places = buildAnnotatedString {
        if (outage.places.isNotEmpty())
            withStyle(SpanStyle()) {
                append(outage.places[0])
                outage.places.chunked(3).first()
                    .forEach {
                        append(", $it")
                    }
            }
        else withStyle(SpanStyle()) { append("") }
    }
    Text(
        modifier = Modifier.padding(vertical = 5.dp),
        text = places,
    )
}

@Composable
private fun OutageText(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = Modifier.padding(vertical = 5.dp),
        text = text,
        color = Color.Black,
    )
}

@Composable
private fun OutageTextBlue(text: String) {
    Text(
        modifier = Modifier.padding(vertical = 10.dp),
        text = text,
        color = Color(0XFF0B4A7A),
    )
}
