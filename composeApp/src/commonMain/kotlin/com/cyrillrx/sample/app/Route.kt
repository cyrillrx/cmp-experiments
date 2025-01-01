package com.cyrillrx.sample.app

import kotlinx.serialization.Serializable
sealed interface Route {

    @Serializable
    data object Home : Route

    @Serializable
    data object Markdown : Route

    @Serializable
    data object Html : Route
}
