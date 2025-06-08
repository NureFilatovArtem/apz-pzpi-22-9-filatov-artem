package com.artemfilatov.environmentmonitor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.artemfilatov.environmentmonitor.ui.entity.EntityListScreen
import com.artemfilatov.environmentmonitor.ui.entity.EntityViewModel
import com.artemfilatov.environmentmonitor.ui.history.HistoryScreen
import com.artemfilatov.environmentmonitor.ui.login.LoginScreen
import com.artemfilatov.environmentmonitor.ui.main.CurrentScreen
import com.artemfilatov.environmentmonitor.ui.main.MainScreen
import com.artemfilatov.environmentmonitor.utils.RetrofitInstance
import com.artemfilatov.environmentmonitor.viewmodel.EntityViewModelFactory
import com.artemfilatov.environmentmonitor.viewmodel.OverviewViewModel
import com.artemfilatov.environmentmonitor.viewmodel.OverviewViewModelFactory

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(onLoginSuccess = { navController.navigate("main") })
        }

        composable("main") {
            MainScreen(navController)
        }

        composable("current") {
            val overviewVM: OverviewViewModel = viewModel(factory = OverviewViewModelFactory(
                RetrofitInstance.api))
            CurrentScreen(overviewVM)
        }

        composable("history") {
            HistoryScreen()
        }

        // === ENTITY ROUTES ===

        // 🏢 OFFICES
        composable("offices") {
            val entityVM: EntityViewModel = viewModel(factory = EntityViewModelFactory(RetrofitInstance.api))
            LaunchedEffect(Unit) { entityVM.loadOffices() }

            EntityListScreen(
                title = "Офіси",
                entityType = "office",
                items = entityVM.offices.map { mapOf("id" to it.id.toString(), "name" to it.name, "buildingId" to it.buildingId.toString()) },
                onDelete = { fields -> fields["id"]?.toIntOrNull()?.let { entityVM.deleteOffice(it) } },
                onEdit = { fields -> fields["id"]?.toIntOrNull()?.let { entityVM.editOffice(it, fields) } },
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
                items = entityVM.buildings.map { mapOf("id" to it.id.toString(), "name" to it.name, "address" to it.address) },
                onDelete = { fields -> fields["id"]?.toIntOrNull()?.let { entityVM.deleteBuilding(it) } },
                onEdit = { fields -> fields["id"]?.toIntOrNull()?.let { entityVM.editBuilding(it, fields) } },
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
                items = entityVM.sensors.map { mapOf("id" to it.id.toString(), "type" to it.type, "officeId" to it.officeId?.toString().orEmpty()) },
                onDelete = { fields -> fields["id"]?.toIntOrNull()?.let { entityVM.deleteSensor(it) } },
                onEdit = { fields -> fields["id"]?.toIntOrNull()?.let { entityVM.editSensor(it, fields) } },
                onCreateEntity = { entityVM.createSensor(it) }
            )
        }

        // 🔔 SUBSCRIPTIONS
        composable("subscriptions") {
            val entityVM: EntityViewModel = viewModel(factory = EntityViewModelFactory(RetrofitInstance.api))
            LaunchedEffect(Unit) { entityVM.loadSubscriptions() }

            EntityListScreen(
                title = "🔔 Підписки",
                entityType = "subscription",
                items = entityVM.subscriptions.map { mapOf("id" to it.sensor_id.toString(), "sensor_id" to it.sensor_id.toString(), "callback_url" to it.callback_url) },
                onDelete = { fields -> fields["id"]?.toIntOrNull()?.let { entityVM.deleteSubscription(it) } },
                onEdit = { fields -> fields["id"]?.toIntOrNull()?.let { entityVM.editSubscription(it, fields) } },
                onCreateEntity = { entityVM.createSubscriptionFromFields(it) }
            )
        }
    }
}
