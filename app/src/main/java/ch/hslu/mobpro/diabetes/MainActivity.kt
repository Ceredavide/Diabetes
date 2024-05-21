package ch.hslu.mobpro.diabetes

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import ch.hslu.mobpro.diabetes.data.database.AppDatabase
import ch.hslu.mobpro.diabetes.data.database.GlucoseReadingDAO
import ch.hslu.mobpro.diabetes.data.database.ProductDAO
import ch.hslu.mobpro.diabetes.ui.navigation.BottomNavigationBar
import ch.hslu.mobpro.diabetes.ui.navigation.Routes
import ch.hslu.mobpro.diabetes.ui.screens.adding.ComposeMeal
import ch.hslu.mobpro.diabetes.ui.screens.editing.EditProduct
import ch.hslu.mobpro.diabetes.ui.screens.editing.EditUser
import ch.hslu.mobpro.diabetes.ui.screens.adding.EnterManualScreen
import ch.hslu.mobpro.diabetes.ui.screens.HomeScreen
import ch.hslu.mobpro.diabetes.ui.screens.ProductsScreen
import ch.hslu.mobpro.diabetes.ui.screens.ProfileScreen
import ch.hslu.mobpro.diabetes.ui.screens.ResultScreen
import ch.hslu.mobpro.diabetes.ui.screens.SearchLocalScreen
import ch.hslu.mobpro.diabetes.ui.screens.adding.AddUser
import ch.hslu.mobpro.diabetes.ui.screens.welcome.UserPreferences
import ch.hslu.mobpro.diabetes.ui.screens.welcome.WelcomeScreen
import ch.hslu.mobpro.diabetes.ui.theme.DiabeticsTheme
import ch.hslu.mobpro.diabetes.ui.viewmodels.IngredientViewModel

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var db: AppDatabase
        lateinit var productDao: ProductDAO
        lateinit var glucoseReadingDao: GlucoseReadingDAO
        var activeUserInfo: MutableState<UserPreferences?> = mutableStateOf(null)
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
        glucoseReadingDao = db.glucoseReadingDao()

        setContent {
            DiabeticsTheme {
                if (preferenceManager.isFirstTime()) {
                    WelcomeScreen(onCompleted = {
                        preferenceManager.addUser(it, this)
                        preferenceManager.setFirstTime(false)
                        preferenceManager.setActiveUserIndex(0u, this)
                        setContent { App(this) }
                    })
                } else {
                    preferenceManager.switchToActiveUser(this)
                    App(this)
                }
            }
        }
    }
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun App(context: Context) {
    val navController = rememberNavController()
    val ingredientViewModel: IngredientViewModel = viewModel()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        NavHost(navController, startDestination = Routes.home) {
            composable(Routes.home) { HomeScreen() }
            composable(Routes.dashboard) { ProductsScreen() }
            composable(Routes.enterManually) { EnterManualScreen() }
            composable(Routes.editProduct + "/{name}/{carbs}") {

                val productName = it.arguments?.getString("name")
                val productCarbs = it.arguments?.getString("carbs")?.toFloat()
                EditProduct(productName = productName!!, productCarbs = productCarbs!!)
            }
            composable(Routes.searchLocal + "/{editable}") {

                val editable = it.arguments?.getString("editable").toBoolean()
                SearchLocalScreen(navController = navController, editable = editable, ingredientViewModel = ingredientViewModel)
            }
            composable(Routes.searchLocal) {

                SearchLocalScreen(navController = navController, editable =  true, null)
            }
            composable(Routes.composeMeal) {

                ComposeMeal(navController = navController, ingredientViewModel = ingredientViewModel)
            }
            composable(Routes.resultScreen) {

                ResultScreen(navController = navController, ingredientViewModel = ingredientViewModel, context = context)
            }
            composable(Routes.searchLocal) { SearchLocalScreen(
                navController = navController,
                editable = true,
                ingredientViewModel = ingredientViewModel)}
            composable(Routes.notifications) { ProfileScreen(navController = navController, context = context) }
            composable(Routes.editUser + "/{username}") {

                val userName = it.arguments?.getString("username")
                val userInfo = PreferenceManager.instance.getUserInfo(userName!!, context)
                EditUser(navController = navController, user = userInfo, context = context)
            }
            composable(Routes.addUser) { AddUser(navController = navController, context = context) }
        }
    }

}
