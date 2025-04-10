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
import com.artemfilatov.environmentmonitor.ui.entity.EntityViewModel


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
                onDelete = { name -> entityVM.deleteOfficeByName(name) },
                onEdit = { name ->
                    val office = entityVM.offices.find { it.name == name["name"] }
                    office?.let {
                        val fields = mapOf("name" to it.name, "buildingId" to it.buildingId)
                        entityVM.editOffice(it.id, fields)
                    }
                },
                onCreateEntity = { fields -> entityVM.createOffice(fields) }
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
                onDelete = { name -> entityVM.deleteBuildingByName(name) },
                onEdit = { name ->
                    val building = entityVM.buildings.find { it.name == name["name"] }
                    building?.let {
                        val fields = mapOf("name" to it.name, "address" to it.address)
                        entityVM.editBuilding(it.id, fields)
                    }
                },
                onCreateEntity = { fields -> entityVM.createBuilding(fields) }
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
                onDelete = { id -> entityVM.deleteSensorById(id) },
                onEdit = { id ->
                    val sensorId = entityVM.extractIdSafe(id.toString()) // ✅ тут твоя функція
                    val sensor = entityVM.sensors.find { it.id == sensorId }
                    sensor?.let {
                        val fields = mapOf(
                            "type" to it.type,
                            "officeId" to it.officeId.toString()
                        )
                        entityVM.editSensorById(sensorId, fields)
                    }
                },
                onCreateEntity = { fields -> entityVM.createSensor(fields) }
            )
        }

        // 🔔 SUBSCRIPTIONS
        composable("subscriptions") {
            val entityVM: EntityViewModel = viewModel(factory = EntityViewModelFactory(RetrofitInstance.api))
            LaunchedEffect(Unit) { entityVM.loadSubscriptions() }

            SubscriptionListScreen(
                subscriptions = entityVM.subscriptions,
                onDelete = { id -> entityVM.deleteSubscriptionBySensorId(id) },
                onEdit = { id ->
                    val subId = id.substringAfter("Sensor ID: ").substringBefore(",").trim()
                    val subscription = entityVM.subscriptions.find { it.sensor_id.toString() == subId }
                    subscription?.let {
                        val fields = mapOf(
                            "sensor_id" to it.sensor_id.toString(),
                            "callback_url" to it.callback_url
                        )
                        entityVM.editSubscription(subId, fields)
                    }
                },
                onCreateEntity = { fields -> entityVM.createSubscriptionFromFields(fields) }
            )
        }
    }
}