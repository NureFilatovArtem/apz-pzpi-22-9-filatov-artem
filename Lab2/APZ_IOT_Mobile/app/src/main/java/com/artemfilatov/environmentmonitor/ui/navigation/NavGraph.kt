// 📦 Імпорти
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
import com.artemfilatov.environmentmonitor.ui.entity.*
import com.artemfilatov.environmentmonitor.viewmodel.MainViewModel
import com.artemfilatov.environmentmonitor.viewmodel.OverviewViewModel
import com.artemfilatov.environmentmonitor.viewmodel.OverviewViewModelFactory
import com.artemfilatov.environmentmonitor.viewmodel.EntityViewModelFactory
import com.artemfilatov.environmentmonitor.utils.RetrofitInstance

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    NavHost(navController = navController, startDestination = "login") {

        // 🟢 LOGIN
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // 🟢 MAIN
        composable("main") {
            MainScreen(navController)
        }

        // 🟢 CURRENT
        composable("current") {
            LaunchedEffect(Unit) { viewModel.loadLatestMeasurements() }
            val data = viewModel.latestMeasurements.collectAsState().value
            CurrentScreen(latestMeasurements = data)
        }

        // 🟢 OVERVIEW
        composable("overview") {
            val overviewVM: OverviewViewModel = viewModel(factory = OverviewViewModelFactory(RetrofitInstance.api))
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

        // 🟢 HISTORY
        composable("history") {
            HistoryScreen()
        }

        // 🏢 OFFICES
        composable("offices") {
            val entityVM: EntityViewModel = viewModel(factory = EntityViewModelFactory(RetrofitInstance.api))
            LaunchedEffect(Unit) { entityVM.loadOffices() }

            EntityListScreen(
                title = "Офіси",
                entityType = "office",
                items = entityVM.offices.map { it.name },
                onDelete = { fields -> fields["name"]?.let { entityVM.deleteOfficeByName(it) } },
                onEdit = { fields ->
                    val name = fields["name"] ?: return@EntityListScreen
                    val office = entityVM.offices.find { it.name == name }
                    office?.let { entityVM.editOffice(it.id, fields) }
                },
                onCreateEntity = { entityVM.createOffice(it) }
            )
        }

        // 🏢 BUILDINGS
        composable("buildings") {
            val entityVM: EntityViewModel = viewModel(factory = EntityViewModelFactory(RetrofitInstance.api))
            LaunchedEffect(Unit) { entityVM.loadBuildings() }

            EntityListScreen(
                title = "Будівлі",
                entityType = "building",
                items = entityVM.buildings.map { it.name },
                onDelete = { fields -> fields["name"]?.let { entityVM.deleteBuildingByName(it) } },
                onEdit = { fields ->
                    val name = fields["name"] ?: return@EntityListScreen
                    val building = entityVM.buildings.find { it.name == name }
                    building?.let { entityVM.editBuilding(it.id, fields) }
                },
                onCreateEntity = { entityVM.createBuilding(it) }
            )
        }

        // 📡 SENSORS
        composable("sensors") {
            val entityVM: EntityViewModel = viewModel(factory = EntityViewModelFactory(RetrofitInstance.api))
            LaunchedEffect(Unit) { entityVM.loadSensors() }

            EntityListScreen(
                title = "Сенсори",
                entityType = "sensor",
                items = entityVM.sensors.map { "ID: ${it.id}, Type: ${it.type}" },
                onDelete = { fields -> fields["type"]?.let { entityVM.deleteSensorById(it) } },
                onEdit = { fields ->
                    val id = fields["type"]?.let { entityVM.extractIdSafe("ID: $it,") } ?: return@EntityListScreen
                    val sensor = entityVM.sensors.find { it.id == id }
                    sensor?.let { entityVM.editSensorById(sensor.id, fields) }
                },
                onCreateEntity = { entityVM.createSensor(it) }
            )
        }

        // 🔔 SUBSCRIPTIONS
        composable("subscriptions") {
            val entityVM: EntityViewModel = viewModel(factory = EntityViewModelFactory(RetrofitInstance.api))
            LaunchedEffect(Unit) { entityVM.loadSubscriptions() }

            SubscriptionListScreen(
                subscriptions = entityVM.subscriptions,
                onDelete = { fields -> fields["sensor_id"]?.let { entityVM.deleteSubscription(it) } },
                onEdit = { fields ->
                    val id = fields["sensor_id"] ?: return@SubscriptionListScreen
                    val sub = entityVM.subscriptions.find { it.sensor_id.toString() == id }
                    sub?.let { entityVM.editSubscription(id, fields) }
                },
                onCreateEntity = { entityVM.createSubscriptionFromFields(it) }
            )
        }
    }
}