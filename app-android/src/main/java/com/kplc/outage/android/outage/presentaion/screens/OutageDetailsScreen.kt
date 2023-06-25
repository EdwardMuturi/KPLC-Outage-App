package com.kplc.outage.android.outage.presentaion.screens

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.kplc.outage.outage.model.OutageInformationUiState
import com.ramcosta.composedestinations.annotation.Destination

/**
 *
 *
 * add argument configs for ramcosta argument passing of data classes
 * add on click handle to card
 * create order details screen
 *      Define components
 *      Review components
 *      Implement Components
 * */
@Composable
@Destination
fun OutageDetailsScreen(outageInformationUiState: OutageInformationUiState) {
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
        )
    }) { padding ->

    }
}
