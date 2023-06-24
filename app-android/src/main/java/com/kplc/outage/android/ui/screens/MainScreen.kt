package com.kplc.outage.android.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            greeting?.let { OutageText(text = it) }
        }

        outages.regions.forEach { outage ->
            item {
                OutageText(outage.region)
                outage.areas.onEach { area ->
                    OutageText(text = area.name)
                    OutageText(text = area.date)
                    OutageText(text = area.places.toString())
                }
            }
        }
    }
}

@Composable
private fun OutageText(text: String) {
    Text(
        modifier = Modifier.padding(vertical = 10.dp),
        text = text,
        color = Color.Black,
    )
}
