package ch.hslu.mobpro.diabetes.ui.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SetMeal
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.Home,
        Screen.Products,
        Screen.Profile,
        Screen.EnterManual,
        Screen.SearchLocal,
        Screen.ComposeMeal,
    )
    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black
    ) {
        val currentRoute = currentRoute(navController)
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    return navBackStackEntry.value?.destination?.route
}

enum class Screen(val route: String, val icon: ImageVector, val title: String) {
    Home(Routes.home, Icons.Filled.Home, "Home"),
    Products(Routes.dashboard, Icons.Filled.Restaurant, "Products"),
    Profile(Routes.notifications, Icons.Filled.Person, "Profile"),
    EnterManual(Routes.enterManually, Icons.Filled.FoodBank, "Enter Manual"),
    SearchLocal(Routes.searchLocal, Icons.Filled.Search, "Search"),
    ComposeMeal(Routes.composeMeal, Icons.Filled.SetMeal, "Compose")
}