package ch.hslu.mobpro.diabetes

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import ch.hslu.mobpro.diabetes.ui.navigation.BottomNavigationBar
import ch.hslu.mobpro.diabetes.ui.screens.HomeScreen
import ch.hslu.mobpro.diabetes.ui.screens.ProductsScreen
import ch.hslu.mobpro.diabetes.ui.screens.ProfileScreen
import ch.hslu.mobpro.diabetes.ui.screens.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val isFirstTime = sharedPref.getBoolean("isFirstTime", false)

        setContent {
            if (isFirstTime) {
                WelcomeScreen(onCompleted = {
                    sharedPref.edit().putBoolean("isFirstTime", false).apply()
                    setContent { App() }
                })
            } else {
                App()
            }
        }
    }
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun App() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        NavHost(navController, startDestination = "home") {
            composable("home") { HomeScreen() }
            composable("dashboard") { ProductsScreen() }
            composable("notifications") { ProfileScreen() }
        }
    }
}