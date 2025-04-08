package com.artemfilatov.environmentmonitor.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.artemfilatov.environmentmonitor.ui.history.HistoryScreen
import com.artemfilatov.environmentmonitor.ui.main.CurrentScreen
import com.artemfilatov.environmentmonitor.ui.main.MainScreen
import com.artemfilatov.environmentmonitor.ui.overview.DataOverviewScreen
import com.artemfilatov.environmentmonitor.utils.RetrofitInstance
import com.artemfilatov.environmentmonitor.viewmodel.MainViewModel
import com.artemfilatov.environmentmonitor.viewmodel.OverviewViewModel
import com.artemfilatov.environmentmonitor.viewmodel.OverviewViewModelFactory

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController)
        }
        composable("current") {
            LaunchedEffect(Unit) {
                viewModel.loadLatestMeasurements()
            }
            val data = viewModel.latestMeasurements.collectAsState().value
            CurrentScreen(latestMeasurements = data)
        }

        composable("overview") {
            val overviewVM: OverviewViewModel = viewModel(
                factory = OverviewViewModelFactory(RetrofitInstance.api)
            )

            val offices = overviewVM.offices.collectAsState().value
            val buildings = overviewVM.buildings.collectAsState().value
            val sensors = overviewVM.sensors.collectAsState().value

            LaunchedEffect(Unit) {
                overviewVM.loadAll()
                viewModel.loadLatestMeasurements()
            }

            DataOverviewScreen(
                buildings = buildings,
                offices = offices,
                sensors = sensors,
                onRefresh = { overviewVM.loadAll() }
            )
        }

    }
}
