package ch.hslu.mobpro.diabetes.data.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.ui.screens.welcome.UserPreferences

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
    private var userUid = 0u
    private var activeUser = 0u
    private var userMap = mapOf<String, UInt>()
    companion object {
        lateinit var instance: PreferenceManager
    }

    init {

        userUid = sharedPreferences.getString(context.getString(R.string.userUid), "0")?.toUInt() ?: 0u
        activeUser = sharedPreferences.getString(context.getString(R.string.active_user), "0")?.toUInt() ?: 0u

        for (i in 0..userUid.toInt()) {

            val userName = sharedPreferences.getString(context.getString(R.string.user_name) + "$i", null)
            if (userName != null) {

                userMap += Pair(userName, i.toUInt())
            }
        }

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

        val userInfo = mutableListOf<UserPreferences>()
        for (i in 0..userUid.toInt()) {

            val userName = sharedPreferences.getString(context.getString(R.string.user_name) + "$i", null) ?: continue

            val insulinPer10gCarbs = sharedPreferences.getString(context.getString(R.string.insulin_per_10g) + "$i", "0.0f")?.toFloat()
            val insulinPer1mmol_L = sharedPreferences.getString(context.getString(R.string.insulin_per_1mmol_l) + "$i", "0.0f")?.toFloat()
            val lowerBoundGlucoseLevel = sharedPreferences.getString(context.getString(R.string.lower_bounds_glucose_level) + "$i", "4.0f")?.toFloat()
            val upperBoundGlucoseLevel = sharedPreferences.getString(context.getString(R.string.upper_bounds_glucose_level) + "$i", "8.0f")?.toFloat()
            userInfo += UserPreferences(
                name = mutableStateOf(userName),
                insulinPer10gCarbs = mutableStateOf(insulinPer10gCarbs!!),
                insulinper1mmolL = mutableStateOf(insulinPer1mmol_L!!),
                lowerBoundGlucoseLevel = mutableStateOf(lowerBoundGlucoseLevel!!),
                upperBoundGlucoseLevel = mutableStateOf(upperBoundGlucoseLevel!!)
            )
        }

        return userInfo
    }

    fun getActiveUserInfo(context: Context): MutableState<UserPreferences?> {

        val userName = userMap.entries.find { it.value == activeUser }?.key
        return if (userName != null) mutableStateOf(getUserInfo(userName, context)) else mutableStateOf(null)
    }

    fun getUserInfo(userName: String, context: Context) : UserPreferences {

        val index = userMap[userName]

        val name = sharedPreferences.getString(context.getString(R.string.user_name) + "$index", "Unknown")
        val insulinPer10gCarbs = sharedPreferences.getString(context.getString(R.string.insulin_per_10g) + "$index", "0.0f")?.toFloat()
        val insulinPer1mmol_L = sharedPreferences.getString(context.getString(R.string.insulin_per_1mmol_l) + "$index", "0.0f")?.toFloat()
        val lowerBoundGlucoseLevel = sharedPreferences.getString(context.getString(R.string.lower_bounds_glucose_level) + "$index", "4.0f")?.toFloat()
        val upperBoundGlucoseLevel = sharedPreferences.getString(context.getString(R.string.upper_bounds_glucose_level) + "$index", "8.0f")?.toFloat()
        val userInfo = UserPreferences(
            name = mutableStateOf(name!!),
            insulinPer10gCarbs = mutableStateOf(insulinPer10gCarbs!!),
            insulinper1mmolL = mutableStateOf(insulinPer1mmol_L!!),
            lowerBoundGlucoseLevel = mutableStateOf(lowerBoundGlucoseLevel!!),
            upperBoundGlucoseLevel = mutableStateOf(upperBoundGlucoseLevel!!)
        )

        return userInfo
    }

    fun addUser(userInfo: UserPreferences, context: Context): Boolean {

        if (userMap[userInfo.name.value] != null) {

            return false
        }
        persistUser(userInfo, userUid, context)

        userMap += Pair(userInfo.name.value, userUid++)
        sharedPreferences.edit().putString(context.getString(R.string.userUid), userUid.toString()).apply()

        return true
    }

    fun editUser(userInfo: UserPreferences, context: Context) {

        val userUid = userMap[userInfo.name.value]
        persistUser(userInfo, userUid!!, context)
    }

    private fun persistUser(userInfo: UserPreferences, userUid: UInt, context: Context) {

        val editor = sharedPreferences.edit()
        editor.putString(context.getString(R.string.user_name) + "$userUid", userInfo.name.value)
        editor.putString(context.getString(R.string.insulin_per_10g) + "$userUid", userInfo.insulinPer10gCarbs.value.toString())
        editor.putString(context.getString(R.string.insulin_per_1mmol_l) + "$userUid", userInfo.insulinper1mmolL.value.toString())
        editor.putString(context.getString(R.string.lower_bounds_glucose_level) + "$userUid", userInfo.lowerBoundGlucoseLevel.value.toString())
        editor.putString(context.getString(R.string.upper_bounds_glucose_level) + "$userUid", userInfo.upperBoundGlucoseLevel.value.toString())
        editor.apply()
    }

    fun deleteUser(userName: String, context: Context) {

        val index = userMap[userName]
        userMap -= sharedPreferences.getString(context.getString(R.string.user_name) + "$index", "")!!

        val editor = sharedPreferences.edit()
        editor.remove(context.getString(R.string.user_name) + "$index")
        editor.remove(context.getString(R.string.insulin_per_10g) + "$index")
        editor.remove(context.getString(R.string.insulin_per_1mmol_l) + "$index")
        editor.remove(context.getString(R.string.lower_bounds_glucose_level) + "$index")
        editor.remove(context.getString(R.string.upper_bounds_glucose_level) + "$index")
        editor.apply()
    }

    fun getActiveUserIndex(): UInt {

        return activeUser
    }

    fun setActiveUserIndex(user: UInt, context: Context) {

        activeUser = user
        MainActivity.activeUserInfo = getActiveUserInfo(context)
        sharedPreferences.edit().putString(context.getString(R.string.active_user), activeUser.toString()).apply()
    }

    fun switchToActiveUser(context: Context) {

        val index = sharedPreferences.getString(context.getString(R.string.active_user), null)?.toUIntOrNull() ?: return
        setActiveUserIndex(index, context)
    }

}