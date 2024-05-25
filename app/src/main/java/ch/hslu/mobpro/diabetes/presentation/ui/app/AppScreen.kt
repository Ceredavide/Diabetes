package ch.hslu.mobpro.diabetes.presentation.ui.app

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.GlucoseReadingsViewModel
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.IngredientViewModel
import ch.hslu.mobpro.diabetes.presentation.navigation.BottomNavigationBar
import ch.hslu.mobpro.diabetes.presentation.navigation.Routes
import ch.hslu.mobpro.diabetes.presentation.ui.user.profile.ProfileScreen
import ch.hslu.mobpro.diabetes.presentation.ui.ResultScreen
import ch.hslu.mobpro.diabetes.presentation.ui.adding.AddUser
import ch.hslu.mobpro.diabetes.presentation.ui.adding.ComposeMeal
import ch.hslu.mobpro.diabetes.presentation.ui.editing.EditUser
import ch.hslu.mobpro.diabetes.presentation.ui.home.HomeScreen
import ch.hslu.mobpro.diabetes.presentation.ui.product.form.ProductFormScreen
import ch.hslu.mobpro.diabetes.presentation.ui.product.list.SearchLocalScreen


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppScreen(context: Context) {
    val navController = rememberNavController()
    val ingredientViewModel: IngredientViewModel = viewModel()
    val glucoseReadingsViewModel: GlucoseReadingsViewModel = viewModel()
    val glucoseLevel = remember { mutableFloatStateOf(0.0f) }

    Scaffold(bottomBar = { BottomNavigationBar(navController) }) {
        NavHost(navController, startDestination = Routes.home) {
            composable(Routes.home) {
                HomeScreen(navController = navController, glucoseReadingsViewModel = glucoseReadingsViewModel)
            }
            composable(Routes.enterManually) {
                ProductFormScreen()
            }
            composable(Routes.editProduct + "/{name}/{carbs}") { backStackEntry ->
                val productName = backStackEntry.arguments?.getString("name")
                val productCarbs = backStackEntry.arguments?.getString("carbs")?.toFloat()
                if (productName != null && productCarbs != null) {
                    ProductFormScreen(initialProductName = productName, initialCarbs = productCarbs)
                }
            }
            composable(Routes.searchLocal + "/{editable}") { backStackEntry ->
                val editable = backStackEntry.arguments?.getString("editable")?.toBoolean() ?: false
                SearchLocalScreen(navController = navController, editable = editable, ingredientViewModel = ingredientViewModel)
            }
            composable(Routes.searchLocal) {
                SearchLocalScreen(navController = navController, editable = true, ingredientViewModel = ingredientViewModel)
            }
            composable(Routes.composeMeal) {
                ComposeMeal(
                    navController = navController,
                    ingredientViewModel = ingredientViewModel,
                    glucoseReadingsViewModel = glucoseReadingsViewModel,
                    glucoseLevel = glucoseLevel
                )
            }
            composable(Routes.resultScreen) {
                ResultScreen(
                    navController = navController,
                    ingredientViewModel = ingredientViewModel,
                    glucoseLevel = glucoseLevel.floatValue,
                    context = context
                )
            }
            composable(Routes.notifications) {
                ProfileScreen(navController = navController, glucoseReadingsViewModel = glucoseReadingsViewModel, context = context)
            }
            composable(Routes.editUser + "/{username}") { backStackEntry ->
                val userName = backStackEntry.arguments?.getString("username")
                val userInfo = userName?.let { PreferenceManager.instance.getUserInfo(it, context) }
                if (userInfo != null) {
                    EditUser(navController = navController, user = userInfo, context = context)
                }
            }
            composable(Routes.addUser) {
                AddUser(navController = navController, context = context)
            }
        }
    }
}