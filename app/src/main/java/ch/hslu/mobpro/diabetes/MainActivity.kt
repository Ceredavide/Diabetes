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
import ch.hslu.mobpro.diabetes.domain.model.User
import ch.hslu.mobpro.diabetes.presentation.navigation.AppNavigation
import ch.hslu.mobpro.diabetes.presentation.ui.welcome.WelcomeScreen
import ch.hslu.mobpro.diabetes.presentation.theme.DiabeticsTheme

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var database: AppDatabase
        var activeUserInfo: MutableState<User?> = mutableStateOf(null)
    }

    private lateinit var preferenceManager: PreferenceManager

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        preferenceManager = PreferenceManager(this)
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, AppDatabase.NAME).build()

        setContent {
            DiabeticsTheme {
                if (preferenceManager.isFirstTime()) {
                    WelcomeScreen(onCompleted = {
                        preferenceManager.addUser(it, context = this)
                        preferenceManager.switchToActiveUser(context = this)
                        preferenceManager.setFirstTime(false)
                        setContent { AppNavigation(this) }
                    })
                } else {
                    preferenceManager.switchToActiveUser(context = this)
                    AppNavigation(this)
                }
            }
        }
    }
}