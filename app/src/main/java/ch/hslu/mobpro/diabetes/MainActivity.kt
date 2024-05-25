package ch.hslu.mobpro.diabetes

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.Room
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import ch.hslu.mobpro.diabetes.data.database.AppDatabase
import ch.hslu.mobpro.diabetes.data.database.ProductDAO
import ch.hslu.mobpro.diabetes.presentation.ui.welcome.UserPreferences
import ch.hslu.mobpro.diabetes.presentation.ui.welcome.WelcomeScreen
import ch.hslu.mobpro.diabetes.presentation.theme.DiabeticsTheme
import ch.hslu.mobpro.diabetes.presentation.ui.app.AppScreen

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
                        setContent { AppScreen(this) }
                    })
                } else {
                    preferenceManager.switchToActiveUser(this)
                    AppScreen(this)
                }
            }
        }
    }
}