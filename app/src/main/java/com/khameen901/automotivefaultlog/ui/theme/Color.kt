package com.khameen901.automotivefaultlog.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.khameen901.automotivefaultlog.ui.screens.addedit.AddEditFaultScreen
import com.khameen901.automotivefaultlog.ui.screens.home.HomeDashboardScreen
import com.khameen901.automotivefaultlog.ui.screens.records.FaultRecordsScreen
import com.khameen901.automotivefaultlog.ui.screens.search.SearchFiltersScreen
import com.khameen901.automotivefaultlog.ui.screens.settings.SettingsScreen
import com.khameen901.automotivefaultlog.viewmodel.FaultViewModel
import com.khameen901.automotivefaultlog.viewmodel.FaultViewModelFactory

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val viewModel: FaultViewModel = viewModel(
        factory = FaultViewModelFactory(context.applicationContext as Application)
    )

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeDashboardScreen(navController = navController, viewModel = viewModel)
        }
        composable("records") {
            FaultRecordsScreen(navController = navController, viewModel = viewModel)
        }
        composable("search") {
            SearchFiltersScreen(navController = navController, viewModel = viewModel)
        }
        composable("settings") {
            SettingsScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = "addEdit/{faultId}",
            arguments = listOf(navArgument("faultId") { type = NavType.LongType; defaultValue = 0L })
        ) { backStackEntry ->
            val faultId = backStackEntry.arguments?.getLong("faultId") ?: 0L
            AddEditFaultScreen(
                navController = navController,
                viewModel = viewModel,
                faultId = faultId
            )
        }
    }
}
