package com.kplc.outage.android.outage.presentaion.screens

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
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.kplc.outage.android.R
import com.kplc.outage.android.outage.presentaion.screens.destinations.OutageDetailsScreenDestination
import com.kplc.outage.outage.model.OutageInformation
import com.kplc.outage.outage.model.OutageInformationUiState
import com.kplc.outage.outage.presentation.OutageViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.get

val outageBlue500 = Color(0XFF0B4A7A)

@Composable
@Destination(start = true)
fun OutagesScreen(outageViewModel: OutageViewModel = get(), navigator: DestinationsNavigator) {
    val outageInformation by outageViewModel.outageInformationUiState.collectAsState()

    LaunchedEffect(key1 = true, block = {
        outageViewModel.fetchOutages()
    })

//    Display bottom sheet
    Scaffold(topBar = {
        TopAppBar(
            backgroundColor = Color.White,
            contentColor = Color.Black,
            title = {
                Row(Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = stringResource(R.string.power_interruptions),
                        fontWeight = FontWeight.Medium,
                    )

                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add url", modifier = Modifier.padding(horizontal = 20.dp).clickable {  })
                }
            },
        )
    }) { padding ->
        OutagesScreenContent(padding, outageInformation) { state ->
            navigator.navigate(OutageDetailsScreenDestination(outageInformationUiState = state).route)
        }
    }
}

@Composable
private fun OutagesScreenContent(
    padding: PaddingValues,
    outageInformation: OutageInformation,
    showMoreDetails: (OutageInformationUiState) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .padding(20.dp)
            .fillMaxSize(),
    ) {
        item {
            var searchValue by remember { mutableStateOf(TextFieldValue("")) }
            if (outageInformation.outages.isNotEmpty()) {
                OutlinedTextField(
                    value = searchValue,
                    onValueChange = { searchValue = it },
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
        }
        item {
            when (outageInformation.isLoading) {
                true -> CircularProgressIndicator()
                false -> {
                    if (outageInformation.outages.isEmpty()) {
                        Text(text = "No outage data found at the moment, please try again later")
                    }
                    outageInformation.outages.forEach { outage ->
                        val places = buildAnnotatedString {
                            withStyle(SpanStyle()) {
                                append(outage.places[0])
                            }
                            withStyle(SpanStyle()) {
                                outage.places.subList(1, 3)
                                    .forEach {
                                        append(", $it")
                                    }
                            }
                        }
                        OutageCard(outage, places) {
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
    aT: AnnotatedString,
    onViewDetails: () -> Unit,
) {
    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(2.dp),
        modifier = Modifier
            .clickable { onViewDetails() }
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

                OutageAnnotatedText(aT)
            }
        }
    }
}

@Composable
private fun OutageAnnotatedText(text: AnnotatedString) {
    Text(
        modifier = Modifier.padding(vertical = 5.dp),
        text = text,
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
