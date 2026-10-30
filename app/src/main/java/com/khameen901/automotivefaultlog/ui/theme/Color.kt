package com.khameen901.automotivefaultlog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.khameen901.automotivefaultlog.ui.screens.AddEditFaultScreen
import com.khameen901.automotivefaultlog.ui.screens.FaultRecordsScreen
import com.khameen901.automotivefaultlog.ui.screens.HomeDashboardScreen
import com.khameen901.automotivefaultlog.ui.screens.SearchFiltersScreen
import com.khameen901.automotivefaultlog.ui.screens.SettingsScreen
import com.khameen901.automotivefaultlog.viewmodel.FaultViewModel

sealed class AppScreen(val route: String) {
    data object Home : AppScreen("home")
    data object AddEdit : AppScreen("add_edit")
    data object Records : AppScreen("records")
    data object Search : AppScreen("search")
    data object Settings : AppScreen("settings")
}

@Composable
fun AutomotiveFaultLogApp(
    viewModel: FaultViewModel = viewModel()
) {
    val navController = rememberNavController()
    val records by viewModel.records.collectAsState()

    NavHost(
        navController = navController,
        startDestination = AppScreen.Home.route
    ) {
        composable(AppScreen.Home.route) {
            HomeDashboardScreen(navController = navController, records = records)
        }

        composable(
            route = "${AppScreen.AddEdit.route}?recordId={recordId}",
            arguments = listOf(
                navArgument("recordId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val recordId = backStackEntry.arguments?.getLong("recordId") ?: 0L
            val selectedRecord = records.find { it.fault.id == recordId }
            AddEditFaultScreen(
                navController = navController,
                viewModel = viewModel,
                selectedRecord = selectedRecord
            )
        }

        composable(AppScreen.Records.route) {
            FaultRecordsScreen(
                navController = navController,
                records = records,
                viewModel = viewModel
            )
        }

        composable(AppScreen.Search.route) {
            SearchFiltersScreen(
                navController = navController,
                records = records,
                viewModel = viewModel
            )
        }

        composable(AppScreen.Settings.route) {
            SettingsScreen(navController = navController, viewModel = viewModel)
        }
    }
}
