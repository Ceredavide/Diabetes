package ch.hslu.mobpro.diabetes.data.pref

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.compose.ui.res.stringResource
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.ui.screens.welcome.UserPreferences

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

    fun isFirstTime(): Boolean {
        return sharedPreferences.getBoolean("isFirstTime", true)
    }

    fun setFirstTime(isFirstTime: Boolean) {
        sharedPreferences.edit().putBoolean("isFirstTime", isFirstTime).apply()
    }

    fun setUserinfo(userInfo: UserPreferences, context: Context) {
        sharedPreferences.edit().putString(context.getString(R.string.user_name), userInfo.name.value).apply()
        sharedPreferences.edit().putString(context.getString(R.string.insulin_per_10g), userInfo.insulinPer10gCarbs.value.toString()).apply()
        sharedPreferences.edit().putString(context.getString(R.string.insulin_per_1mmol_l), userInfo.inslinePer1mmol_L.value.toString()).apply()
        sharedPreferences.edit().putString(context.getString(R.string.upper_bounds_glucose_level), userInfo.upperBoundGlucoseLevel.value.toString()).apply()
        sharedPreferences.edit().putString(context.getString(R.string.lower_bounds_glucose_level), userInfo.lowerBoundsGlucoseLevel.value.toString()).apply()
    }

}