package com.kplc.outage.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.kplc.outage.outage.presentation.OutageViewModel
import com.kplc.outage.presentation.viewmodels.MainViewModel
import org.koin.androidx.compose.get

@Composable
fun MainScreen(viewModel: MainViewModel = get(), outageViewModel: OutageViewModel = get()) {
    val greeting = viewModel.greeting.collectAsState().value
    val outages by outageViewModel.outageInformationUiState.collectAsState()

    LaunchedEffect(key1 = true, block = {
        outageViewModel.fetchOutages()
    })

    LazyColumn(modifier = Modifier.padding(20.dp).fillMaxSize()) {
        item {
            when (outages.isLoading) {
                true -> CircularProgressIndicator()
                false -> {
                    if (outages.regions.isEmpty()) {
                        Text(text = "No outage data found at the moment, please try again later")
                    }
                    outages.regions.forEach { outage ->
                        outage.areas.onEach { area ->
                            val aT = buildAnnotatedString {
                                withStyle(SpanStyle()) {
                                    append(area.places[0].name)
                                }
                                withStyle(SpanStyle()) {
                                    area.places.subList(1, 3)
                                        .forEach {
                                            append(", ${it.name}")
                                        }
                                }
                            }

                            Card(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(2.dp),
                                modifier = Modifier
                                    .padding(vertical = 10.dp),
                            ) {
                                Box(Modifier.fillMaxWidth()) {
                                    Spacer(
                                        Modifier
                                            .align(Alignment.TopStart)
                                            .width(3.dp)
                                            .height(150.dp)
                                            .background(color = Color(0XFF0B4A7A)),
                                    )

                                    Column(
                                        Modifier.fillMaxWidth()
                                            .padding(horizontal = 15.dp, vertical = 10.dp),
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                        ) {
                                            OutageTextBlue(outage.region)
                                            OutageText(text = area.name)
                                        }

                                        Text(
                                            modifier = Modifier.padding(vertical = 5.dp),
                                            text = area.date,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold,
                                        )

                                        OutageAnnotatedText(aT)
                                    }
                                }
                            }
                        }
                    }
                }
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
