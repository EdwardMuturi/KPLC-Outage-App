package com.kplc.outage.android.outage.presentaion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kplc.outage.outage.model.OutageInformationUiState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun OutageDetailsScreen(
    outageInformationUiState: OutageInformationUiState,
    navigator: DestinationsNavigator,
) {
    Scaffold(topBar = {
        TopAppBar(
            backgroundColor = Color.White,
            contentColor = Color.Black,
            title = {
                Text(
                    text = outageInformationUiState.area,
                    fontWeight = FontWeight.Medium,
                )
            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigate Back",
                    modifier = Modifier.clickable { navigator.navigateUp() },
                )
            },
        )
    }) { padding ->
        OutageDetailsScreenContent(padding, outageInformationUiState)
    }
}

@Composable
private fun OutageDetailsScreenContent(
    padding: PaddingValues,
    outageInformationUiState: OutageInformationUiState,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(20.dp),
    ) {
        Text(
            text = outageInformationUiState.region,
            color = Color.White,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .background(
                    color = outageBlue500,
                    shape = RoundedCornerShape(4.dp),
                )
                .padding(horizontal = 20.dp, vertical = 10.dp),
        )
    }
}
