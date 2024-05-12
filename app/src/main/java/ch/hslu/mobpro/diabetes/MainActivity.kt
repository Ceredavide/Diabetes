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
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager

import ch.hslu.mobpro.diabetes.ui.navigation.BottomNavigationBar
import ch.hslu.mobpro.diabetes.ui.screens.HomeScreen
import ch.hslu.mobpro.diabetes.ui.screens.ProductsScreen
import ch.hslu.mobpro.diabetes.ui.screens.ProfileScreen
import ch.hslu.mobpro.diabetes.ui.screens.WelcomeScreen
import ch.hslu.mobpro.diabetes.ui.theme.DiabeticsTheme

class MainActivity : ComponentActivity() {

    private lateinit var preferenceManager: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceManager = PreferenceManager(this)

        setContent {
            DiabeticsTheme {
                if (preferenceManager.isFirstTime()) {
                    WelcomeScreen(onCompleted = {
                        preferenceManager.setFirstTime(false)
                        setContent { App() }
                    })
                } else {
                    App()
                }
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