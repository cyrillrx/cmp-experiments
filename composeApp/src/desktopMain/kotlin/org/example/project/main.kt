package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cyrillrx.sample.app.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CMP_Compose",
    ) {
        App()
    }
}
