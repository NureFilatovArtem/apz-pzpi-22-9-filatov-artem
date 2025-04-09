package com.artemfilatov.environmentmonitor.ui.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.artemfilatov.environmentmonitor.ui.history.HistoryScreen
import com.artemfilatov.environmentmonitor.ui.login.LoginScreen
import com.artemfilatov.environmentmonitor.ui.main.CurrentScreen
import com.artemfilatov.environmentmonitor.ui.main.MainScreen
import com.artemfilatov.environmentmonitor.ui.overview.DataOverviewScreen
import com.artemfilatov.environmentmonitor.utils.RetrofitInstance
import com.artemfilatov.environmentmonitor.viewmodel.MainViewModel
import com.artemfilatov.environmentmonitor.viewmodel.OverviewViewModel
import com.artemfilatov.environmentmonitor.viewmodel.OverviewViewModelFactory

import com.artemfilatov.environmentmonitor.ui.entity.EntityViewModel
import com.artemfilatov.environmentmonitor.viewmodel.EntityViewModelFactory
import com.artemfilatov.environmentmonitor.ui.entity.SubscriptionListScreen
import com.artemfilatov.environmentmonitor.ui.entity.EntityListScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            // Переходимо до MainScreen після натискання кнопки
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

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

        composable("history") {
            HistoryScreen()
        }

        composable("offices") {
            val entityVM: EntityViewModel = viewModel(factory = EntityViewModelFactory(RetrofitInstance.api))
            LaunchedEffect(Unit) { entityVM.loadOffices() }

            EntityListScreen(
                title = "Офіси",
                items = entityVM.offices.map { it.name },
                onEdit = { name -> /* TODO: реалізуй логіку редагування */ },
                onDelete = { name -> /* TODO: реалізуй логіку видалення */ }
            )
        }

        composable("buildings") {
            val entityVM: EntityViewModel = viewModel(factory = EntityViewModelFactory(RetrofitInstance.api))
            LaunchedEffect(Unit) { entityVM.loadBuildings() }
            EntityListScreen(title = "Будівлі", items = entityVM.buildings.map { it.name })
        }

        composable("sensors") {
            val entityVM: EntityViewModel = viewModel(factory = EntityViewModelFactory(RetrofitInstance.api))
            LaunchedEffect(Unit) { entityVM.loadSensors() }
            EntityListScreen(title = "Сенсори", items = entityVM.sensors.map { "ID: ${it.id}, Type: ${it.type}" })
        }

        composable("subscriptions") {
            val entityVM: EntityViewModel = viewModel(factory = EntityViewModelFactory(RetrofitInstance.api))
            LaunchedEffect(Unit) { entityVM.loadSubscriptions() }
            SubscriptionListScreen(subscriptions = entityVM.subscriptions)
        }


    }
}
