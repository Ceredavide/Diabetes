package ch.hslu.mobpro.diabetes.data.pref

import android.content.Context
import android.content.SharedPreferences
import ch.hslu.mobpro.diabetes.ui.screens.welcome.UserPreferences

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

    fun isFirstTime(): Boolean {
        return sharedPreferences.getBoolean("isFirstTime", true)
    }

    fun setFirstTime(isFirstTime: Boolean) {
        sharedPreferences.edit().putBoolean("isFirstTime", isFirstTime).apply()
    }

    fun setUserinfo(userInfo: UserPreferences) {
        sharedPreferences.edit().putString("name", userInfo.name.value).apply()
        sharedPreferences.edit().putString("age", userInfo.age.value).apply()
        sharedPreferences.edit().putString("email", userInfo.email.value).apply()
    }

}