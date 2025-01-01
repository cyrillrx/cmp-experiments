package com.cyrillrx.sample.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cyrillrx.sample.home.HomeScreen
import com.cyrillrx.sample.markdown.TextAdapterScreen
import com.cyrillrx.sample.markdown.TextAdapterViewModel
import com.cyrillrx.sample.markdown.TextAdapterViewModelFactory

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Route.Home,
        ) {
            composable<Route.Home> {
                HomeScreen(
                    openMarkdown = { navController.navigate(Route.Markdown) },
                    openHtml = { navController.navigate(Route.Html) },
                )
            }
            composable<Route.Markdown> {
                val viewModelFactory = TextAdapterViewModelFactory()
                val viewModel = viewModel<TextAdapterViewModel>(factory = viewModelFactory)
                TextAdapterScreen(viewModel)
            }

            composable<Route.Html> {
                val viewModelFactory = TextAdapterViewModelFactory()
                val viewModel = viewModel<TextAdapterViewModel>(factory = viewModelFactory)
                TextAdapterScreen(viewModel)
            }
        }
    }
}
