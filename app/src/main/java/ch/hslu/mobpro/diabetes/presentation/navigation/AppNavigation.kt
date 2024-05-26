package ch.hslu.mobpro.diabetes.presentation.navigation

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.GlucoseReadingsViewModel
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.IngredientViewModel
import ch.hslu.mobpro.diabetes.presentation.ui.home.HomeScreen
import ch.hslu.mobpro.diabetes.presentation.ui.home.components.resultScreen.ResultScreen
import ch.hslu.mobpro.diabetes.presentation.ui.home.components.composeMealScreen.ComposeMealScreen
import ch.hslu.mobpro.diabetes.presentation.ui.products.ProductsScreen
import ch.hslu.mobpro.diabetes.presentation.ui.user.profile.ProfileScreen
import ch.hslu.mobpro.diabetes.presentation.ui.user.form.UserFormScreen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppNavigation(context: Context) {
    val navController = rememberNavController()

    val viewModelStoreOwner = LocalViewModelStoreOwner.current

    val ingredientViewModel: IngredientViewModel = viewModel(
        viewModelStoreOwner = viewModelStoreOwner!!,
        key = "ingredientViewModel",
        factory = ViewModelProvider.NewInstanceFactory()
    )

    val glucoseReadingsViewModel: GlucoseReadingsViewModel = viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        key = "glucoseReadingsViewModel",
        factory = ViewModelProvider.NewInstanceFactory()
    )

    Scaffold(bottomBar = { BottomNavigationBar(navController) }) {
        NavHost(navController, startDestination = Routes.home) {
            homeGraph(navController, glucoseReadingsViewModel, ingredientViewModel, context)
            userGraph(navController, glucoseReadingsViewModel, context)

            composable(Routes.products) {
                ProductsScreen(
                    navController = navController,
                    editable = true,
                    ingredientViewModel = null
                )
            }
        }
    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    glucoseReadingsViewModel: GlucoseReadingsViewModel,
    ingredientViewModel: IngredientViewModel,
    context: Context
) {
    navigation(startDestination = Routes.dashboard, route = Routes.home) {
        composable(Routes.dashboard) {
            HomeScreen(
                navController = navController,
                glucoseReadingsViewModel = glucoseReadingsViewModel
            )
        }
        composable(Routes.composeMeal) {
            ComposeMealScreen(
                navController = navController,
                ingredientViewModel = ingredientViewModel,
                glucoseReadingsViewModel = glucoseReadingsViewModel
            )
        }
        composable(Routes.uneditableProducts) {
            ProductsScreen(
                navController = navController,
                editable = false,
                ingredientViewModel = ingredientViewModel
            )
        }
        composable(Routes.resultScreen) { backStackEntry ->
            val glucoseLevel = backStackEntry.arguments?.getFloat("glucoseLevel")
            ResultScreen(
                navController = navController,
                context = context,
                ingredientViewModel = ingredientViewModel,
                glucoseLevel = glucoseLevel!!
            )
        }
    }
}

fun NavGraphBuilder.userGraph(
    navController: NavController,
    glucoseReadingsViewModel: GlucoseReadingsViewModel,
    context: Context
) {
    navigation(startDestination = Routes.profile, route = Routes.user) {
        composable(Routes.profile) {
            ProfileScreen(
                navController = navController,
                glucoseReadingsViewModel = glucoseReadingsViewModel,
                context = context
            )
        }
        composable(Routes.userFormCreate) { _ ->
            UserFormScreen(
                navController = navController,
                user = null,
                originalName = null
            )
        }
        composable(Routes.userFormEdit) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName")
            val userInfo = userName?.let { PreferenceManager.instance.getUserInfo(it, context) }
            UserFormScreen(
                navController = navController,
                user = userInfo,
                originalName = userName
            )
        }
    }
}
