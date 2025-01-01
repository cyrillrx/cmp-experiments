package com.cyrillrx.sample.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cyrillrx.sample.core.presentation.HomeButton

@Composable
fun HomeScreen(
    openMarkdown: () -> Unit,
    openHtml: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        HomeButton("Markdown", openMarkdown)
        HomeButton("Html", openHtml)
    }
}
