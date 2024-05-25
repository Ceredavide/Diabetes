package ch.hslu.mobpro.diabetes

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import ch.hslu.mobpro.diabetes.data.database.AppDatabase
import ch.hslu.mobpro.diabetes.data.database.ProductDAO
import ch.hslu.mobpro.diabetes.presentation.navigation.BottomNavigationBar
import ch.hslu.mobpro.diabetes.presentation.navigation.Routes
import ch.hslu.mobpro.diabetes.presentation.ui.*
import ch.hslu.mobpro.diabetes.presentation.ui.adding.AddUser
import ch.hslu.mobpro.diabetes.presentation.ui.adding.ComposeMeal
import ch.hslu.mobpro.diabetes.presentation.ui.addProduct.AddProductScreen
import ch.hslu.mobpro.diabetes.presentation.ui.editing.EditProduct
import ch.hslu.mobpro.diabetes.presentation.ui.editing.EditUser
import ch.hslu.mobpro.diabetes.presentation.ui.welcome.UserPreferences
import ch.hslu.mobpro.diabetes.presentation.ui.welcome.WelcomeScreen
import ch.hslu.mobpro.diabetes.presentation.theme.DiabeticsTheme
import ch.hslu.mobpro.diabetes.presentation.viewmodels.GlucoseReadingsViewModel
import ch.hslu.mobpro.diabetes.presentation.viewmodels.IngredientViewModel

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var db: AppDatabase
        lateinit var productDao: ProductDAO
        var activeUserInfo: MutableState<UserPreferences?> = mutableStateOf(null)
    }

    private lateinit var preferenceManager: PreferenceManager

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        preferenceManager = PreferenceManager(this)
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, AppDatabase.NAME).build()
        productDao = db.productDao()

        setContent {
            DiabeticsTheme {
                if (preferenceManager.isFirstTime()) {
                    WelcomeScreen(onCompleted = {
                        preferenceManager.addUser(it, this)
                        preferenceManager.setFirstTime(false)
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
    val glucoseReadingsViewModel: GlucoseReadingsViewModel = viewModel()
    val glucoseLevel = remember { mutableFloatStateOf(0.0f) }

    Scaffold(bottomBar = { BottomNavigationBar(navController) }) {
        NavHost(navController, startDestination = Routes.home) {
            composable(Routes.home) {
                HomeScreen(navController = navController, glucoseReadingsViewModel = glucoseReadingsViewModel)
            }
            composable(Routes.dashboard) {
                ProductsScreen()
            }
            composable(Routes.enterManually) {
                AddProductScreen()
            }
            composable(Routes.editProduct + "/{name}/{carbs}") { backStackEntry ->
                val productName = backStackEntry.arguments?.getString("name")
                val productCarbs = backStackEntry.arguments?.getString("carbs")?.toFloat()
                if (productName != null && productCarbs != null) {
                    EditProduct(productName = productName, productCarbs = productCarbs)
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