package ch.hslu.mobpro.diabetes.data.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.ui.screens.welcome.UserPreferences

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
    private var userCount = 0
    private var activeUser = 0

    companion object {
        lateinit var instance: PreferenceManager
    }

    init {

        instance = this
    }

    fun isFirstTime(): Boolean {
        return sharedPreferences.getBoolean("isFirstTime", true)
    }

    fun setFirstTime(isFirstTime: Boolean) {
        sharedPreferences.edit().putBoolean("isFirstTime", isFirstTime).apply()
    }

    fun setUserinfo(userInfo: UserPreferences, context: Context) {
        //sharedPreferences.edit().putString(context.getString(R.string.user_name) + "$userCount", userInfo.name.value).apply()
        //sharedPreferences.edit().putString(context.getString(R.string.insulin_per_10g) + "$userCount", userInfo.insulinPer10gCarbs.value.toString()).apply()
        //sharedPreferences.edit().putString(context.getString(R.string.insulin_per_1mmol_l) + "$userCount", userInfo.inslinePer1mmol_L.value.toString()).apply()
        //sharedPreferences.edit().putString(context.getString(R.string.upper_bounds_glucose_level) + "$userCount", userInfo.upperBoundGlucoseLevel.value.toString()).apply()
        //sharedPreferences.edit().putString(context.getString(R.string.lower_bounds_glucose_level) + "$userCount", userInfo.lowerBoundGlucoseLevel.value.toString()).apply()
        //userCount++
        addUser(userInfo, context)
    }

    fun getAllUserInfo(context: Context): MutableList<UserPreferences> {

        var userInfo = mutableListOf<UserPreferences>()
        for (i in 0..userCount) {

            val userName = sharedPreferences.getString(context.getString(R.string.user_name) + "$i", "Unknown")
            val insulinPer10gCarbs = sharedPreferences.getString(context.getString(R.string.insulin_per_10g) + "$i", "0.0f")?.toFloat()
            val insulinPer1mmol_L = sharedPreferences.getString(context.getString(R.string.insulin_per_1mmol_l) + "$i", "0.0f")?.toFloat()
            val lowerBoundGlucoseLevel = sharedPreferences.getString(context.getString(R.string.lower_bounds_glucose_level) + "$i", "4.0f")?.toFloat()
            val upperBoundGlucoseLevel = sharedPreferences.getString(context.getString(R.string.upper_bounds_glucose_level) + "$i", "8.0f")?.toFloat()
            userInfo += UserPreferences(
                name = mutableStateOf(userName!!),
                insulinPer10gCarbs = mutableStateOf(insulinPer10gCarbs!!),
                inslinePer1mmol_L = mutableStateOf(insulinPer1mmol_L!!),
                lowerBoundGlucoseLevel = mutableStateOf(lowerBoundGlucoseLevel!!),
                upperBoundGlucoseLevel = mutableStateOf(upperBoundGlucoseLevel!!)
            )
        }

        return userInfo
    }

    fun getActiveUserInfo(context: Context): UserPreferences {

        val userName = sharedPreferences.getString(context.getString(R.string.user_name) + "$activeUser", "Unknown")
        val insulinPer10gCarbs = sharedPreferences.getString(context.getString(R.string.insulin_per_10g) + "$activeUser", "0.0f")?.toFloat()
        val insulinPer1mmol_L = sharedPreferences.getString(context.getString(R.string.insulin_per_1mmol_l) + "$activeUser", "0.0f")?.toFloat()
        val lowerBoundGlucoseLevel = sharedPreferences.getString(context.getString(R.string.lower_bounds_glucose_level) + "$activeUser", "4.0f")?.toFloat()
        val upperBoundGlucoseLevel = sharedPreferences.getString(context.getString(R.string.upper_bounds_glucose_level) + "$activeUser", "8.0f")?.toFloat()
        val userInfo = UserPreferences(
            name = mutableStateOf(userName!!),
            insulinPer10gCarbs = mutableStateOf(insulinPer10gCarbs!!),
            inslinePer1mmol_L = mutableStateOf(insulinPer1mmol_L!!),
            lowerBoundGlucoseLevel = mutableStateOf(lowerBoundGlucoseLevel!!),
            upperBoundGlucoseLevel = mutableStateOf(upperBoundGlucoseLevel!!)
        )

        return userInfo
    }

    fun getUserByIndex(index: Int, context: Context) : UserPreferences {

        val userName = sharedPreferences.getString(context.getString(R.string.user_name) + "$index", "Unknown")
        val insulinPer10gCarbs = sharedPreferences.getString(context.getString(R.string.insulin_per_10g) + "$index", "0.0f")?.toFloat()
        val insulinPer1mmol_L = sharedPreferences.getString(context.getString(R.string.insulin_per_1mmol_l) + "$index", "0.0f")?.toFloat()
        val lowerBoundGlucoseLevel = sharedPreferences.getString(context.getString(R.string.lower_bounds_glucose_level) + "$index", "4.0f")?.toFloat()
        val upperBoundGlucoseLevel = sharedPreferences.getString(context.getString(R.string.upper_bounds_glucose_level) + "$index", "8.0f")?.toFloat()
        val userInfo = UserPreferences(
            name = mutableStateOf(userName!!),
            insulinPer10gCarbs = mutableStateOf(insulinPer10gCarbs!!),
            inslinePer1mmol_L = mutableStateOf(insulinPer1mmol_L!!),
            lowerBoundGlucoseLevel = mutableStateOf(lowerBoundGlucoseLevel!!),
            upperBoundGlucoseLevel = mutableStateOf(upperBoundGlucoseLevel!!)
        )

        return userInfo
    }

    fun addUser(userInfo: UserPreferences, context: Context) {

        val editor = sharedPreferences.edit()
        editor.putString(context.getString(R.string.user_name) + "$userCount", userInfo.name.value)
        editor.putString(context.getString(R.string.insulin_per_10g) + "$userCount", userInfo.insulinPer10gCarbs.value.toString())
        editor.putString(context.getString(R.string.insulin_per_1mmol_l) + "$userCount", userInfo.inslinePer1mmol_L.value.toString())
        editor.putString(context.getString(R.string.lower_bounds_glucose_level) + "$userCount", userInfo.lowerBoundGlucoseLevel.value.toString())
        editor.putString(context.getString(R.string.upper_bounds_glucose_level) + "$userCount", userInfo.upperBoundGlucoseLevel.value.toString())
        editor.apply()

        userCount++
    }
    fun deleteUser(index: Int, context: Context) {

        val editor = sharedPreferences.edit()
        editor.remove(context.getString(R.string.user_name) + "$index")
        editor.remove(context.getString(R.string.insulin_per_10g) + "$index")
        editor.remove(context.getString(R.string.insulin_per_1mmol_l) + "$index")
        editor.remove(context.getString(R.string.lower_bounds_glucose_level) + "$index")
        editor.remove(context.getString(R.string.upper_bounds_glucose_level) + "$index")
        editor.apply()
    }

    fun getUserCount(): Int {

        return userCount
    }

    fun getActiveUserIndex(): Int {

    return activeUser
    }

}