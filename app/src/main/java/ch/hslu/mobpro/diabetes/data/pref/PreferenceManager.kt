package ch.hslu.mobpro.diabetes.data.pref

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.domain.model.User

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
    private var userUid = 0u
    private var activeUser = 0u
    private var userMap = mutableMapOf<String, UInt>()

    companion object {
        lateinit var instance: PreferenceManager
    }

    init {
        userUid = sharedPreferences.getString(context.getString(R.string.userUid), "0")?.toUInt() ?: 0u
        activeUser = sharedPreferences.getString(context.getString(R.string.active_user), "0")?.toUInt() ?: 0u

        for (i in 0..userUid.toInt()) {
            val userName = sharedPreferences.getString(context.getString(R.string.user_name) + "$i", null)
            if (userName != null) {
                userMap[userName] = i.toUInt()
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

    fun setUserinfo(userInfo: User, context: Context) {
        addUser(userInfo, context)
    }

    fun getAllUserInfo(context: Context): MutableList<User> {
        val userInfo = mutableListOf<User>()
        for (i in 0..userUid.toInt()) {
            val userName = sharedPreferences.getString(context.getString(R.string.user_name) + "$i", null) ?: continue
            val insulinPer10gCarbs = sharedPreferences.getString(context.getString(R.string.insulin_per_10g) + "$i", "0.0f")?.toFloat()
            val insulinPer1mmol_L = sharedPreferences.getString(context.getString(R.string.insulin_per_1mmol_l) + "$i", "0.0f")?.toFloat()
            val lowerBoundGlucoseLevel = sharedPreferences.getString(context.getString(R.string.lower_bounds_glucose_level) + "$i", "4.0f")?.toFloat()
            val upperBoundGlucoseLevel = sharedPreferences.getString(context.getString(R.string.upper_bounds_glucose_level) + "$i", "8.0f")?.toFloat()
            userInfo += User(
                name = mutableStateOf(userName),
                insulinPer10gCarbs = mutableStateOf(insulinPer10gCarbs!!),
                insulinPer1mmolL = mutableStateOf(insulinPer1mmol_L!!),
                lowerBoundGlucoseLevel = mutableStateOf(lowerBoundGlucoseLevel!!),
                upperBoundGlucoseLevel = mutableStateOf(upperBoundGlucoseLevel!!)
            )
        }
        return userInfo
    }

    fun getActiveUserInfo(context: Context): MutableState<User?> {
        val userName = userMap.entries.find { it.value == activeUser }?.key
        return if (userName != null) mutableStateOf(getUserInfo(userName, context)) else mutableStateOf(null)
    }

    fun getUserInfo(userName: String, context: Context): User {
        val index = userMap[userName]
        val name = sharedPreferences.getString(context.getString(R.string.user_name) + "$index", "Unknown")
        val insulinPer10gCarbs = sharedPreferences.getString(context.getString(R.string.insulin_per_10g) + "$index", "0.0f")?.toFloat()
        val insulinPer1mmol_L = sharedPreferences.getString(context.getString(R.string.insulin_per_1mmol_l) + "$index", "0.0f")?.toFloat()
        val lowerBoundGlucoseLevel = sharedPreferences.getString(context.getString(R.string.lower_bounds_glucose_level) + "$index", "4.0f")?.toFloat()
        val upperBoundGlucoseLevel = sharedPreferences.getString(context.getString(R.string.upper_bounds_glucose_level) + "$index", "8.0f")?.toFloat()
        val userInfo = User(
            name = mutableStateOf(name!!),
            insulinPer10gCarbs = mutableStateOf(insulinPer10gCarbs!!),
            insulinPer1mmolL = mutableStateOf(insulinPer1mmol_L!!),
            lowerBoundGlucoseLevel = mutableStateOf(lowerBoundGlucoseLevel!!),
            upperBoundGlucoseLevel = mutableStateOf(upperBoundGlucoseLevel!!)
        )
        return userInfo
    }

    fun addUser(userInfo: User, context: Context): Boolean {
        if (userMap[userInfo.name.value] != null) {
            return false
        }
        persistUser(userInfo, userUid, context)
        userMap[userInfo.name.value] = userUid++
        sharedPreferences.edit().putString(context.getString(R.string.userUid), userUid.toString()).apply()
        return true
    }

    fun editUser(originalName: String ,userInfo: User, context: Context) {
        val userUid = userMap[originalName]
        persistUser(userInfo, userUid!!, context)
    }

    private fun persistUser(userInfo: User, userUid: UInt, context: Context) {
        val editor = sharedPreferences.edit()
        editor.putString(context.getString(R.string.user_name) + "$userUid", userInfo.name.value)
        editor.putString(context.getString(R.string.insulin_per_10g) + "$userUid", userInfo.insulinPer10gCarbs.value.toString())
        editor.putString(context.getString(R.string.insulin_per_1mmol_l) + "$userUid", userInfo.insulinPer1mmolL.value.toString())
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
        if (!userMap.containsValue(user)) {
            // Set to the first user in the map, or handle appropriately if the map is empty
            activeUser = userMap.values.firstOrNull() ?: 0u
        } else {
            activeUser = user
        }
        MainActivity.activeUserInfo = getActiveUserInfo(context)
        sharedPreferences.edit().putString(context.getString(R.string.active_user), activeUser.toString()).apply()
    }

    fun switchToActiveUser(context: Context) {
        val index = sharedPreferences.getString(context.getString(R.string.active_user), null)?.toUIntOrNull() ?: return
        setActiveUserIndex(index, context)
    }
}