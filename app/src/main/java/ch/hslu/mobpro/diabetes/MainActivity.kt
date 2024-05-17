package ch.hslu.mobpro.diabetes

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import ch.hslu.mobpro.diabetes.database.AppDatabase
import ch.hslu.mobpro.diabetes.database.Product
import ch.hslu.mobpro.diabetes.database.ProductDAO

import ch.hslu.mobpro.diabetes.ui.navigation.BottomNavigationBar
import ch.hslu.mobpro.diabetes.ui.navigation.Routes
import ch.hslu.mobpro.diabetes.ui.screens.ComposeMeal
import ch.hslu.mobpro.diabetes.ui.screens.EditProduct
import ch.hslu.mobpro.diabetes.ui.screens.EnterManualScreen
import ch.hslu.mobpro.diabetes.ui.screens.HomeScreen
import ch.hslu.mobpro.diabetes.ui.screens.ProductsScreen
import ch.hslu.mobpro.diabetes.ui.screens.ProfileScreen
import ch.hslu.mobpro.diabetes.ui.screens.SearchLocalScreen
import ch.hslu.mobpro.diabetes.ui.screens.welcome.WelcomeScreen
import ch.hslu.mobpro.diabetes.ui.theme.DiabeticsTheme
import ch.hslu.mobpro.diabetes.ui.viewmodels.IngredientViewModel

class MainActivity : ComponentActivity() {

    //private lateinit var binding: ActivityMainBinding
    companion object {
        lateinit var db: AppDatabase
        lateinit var productDao: ProductDAO
    }
    private lateinit var preferenceManager: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceManager = PreferenceManager(this)
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            AppDatabase.NAME
        ).build()

        productDao = db.productDao()

        setContent {
            DiabeticsTheme {
                if (preferenceManager.isFirstTime()) {
                    WelcomeScreen(onCompleted = {
                        preferenceManager.setUserinfo(it)
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
    val ingredientViewModel: IngredientViewModel = viewModel()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        NavHost(navController, startDestination = Routes.home) {
            composable(Routes.home) { HomeScreen() }
            composable(Routes.dashboard) { ProductsScreen() }
            composable(Routes.notifications) { ProfileScreen() }
            composable(Routes.enterManually) { EnterManualScreen() }
            composable(Routes.editProduct + "/{name}/{carbs}") {

                val productName = it.arguments?.getString("name")
                val productCarbs = it.arguments?.getString("carbs")?.toFloat()
                EditProduct(productName!!, productCarbs!!)
            }

            composable(Routes.searchLocal + "/{route}") {

                SearchLocalScreen(navController = navController, ingredientViewModel)
            }
            composable(Routes.searchLocal) {

                SearchLocalScreen(navController = navController, ingredientViewModel)
            }
            composable(Routes.composeMeal) {

                ComposeMeal(
                    navController = navController,
                    ingredientViewModel = ingredientViewModel,
                    productName = null)
            }
            composable(Routes.composeMeal + "/{product}") {

                ComposeMeal(
                    navController = navController,
                    ingredientViewModel = ingredientViewModel,
                    productName = it.arguments?.getString("product")
                )
            }
        }
    }

}
