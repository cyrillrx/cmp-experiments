package com.cyrillrx.sample.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.sample.core.presentation.MarkdownText

@Composable
fun TextAdapterScreen(
    viewModel: TextAdapterViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TextAdapterScreen(
        text = state,
        onTextChange = viewModel::onTextChange,
    )
}

@Composable
fun TextAdapterScreen(
    text: String,
    onTextChange: (String) -> Unit,
) {
    Column {
        TextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )
        MarkdownText(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = Color.Red)
                .padding(16.dp),
        )
    }
}
