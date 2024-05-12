package ch.hslu.mobpro.diabetes.data.pref

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

    fun isFirstTime(): Boolean {
        return sharedPreferences.getBoolean("isFirstTime", true)
    }

    fun setFirstTime(isFirstTime: Boolean) {
        sharedPreferences.edit().putBoolean("isFirstTime", isFirstTime).apply()
    }

}