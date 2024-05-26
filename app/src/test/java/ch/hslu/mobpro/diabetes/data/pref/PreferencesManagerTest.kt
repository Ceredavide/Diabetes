package ch.hslu.mobpro.diabetes.data.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import ch.hslu.mobpro.diabetes.domain.model.User
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import ch.hslu.mobpro.diabetes.R

class PreferenceManagerTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    private lateinit var preferenceManager: PreferenceManager

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(mockContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)
        `when`(mockSharedPreferences.edit()).thenReturn(mockEditor)
        `when`(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor)
        `when`(mockEditor.putBoolean(anyString(), anyBoolean())).thenReturn(mockEditor)

        `when`(mockContext.getString(R.string.userUid)).thenReturn("userUid")
        `when`(mockContext.getString(R.string.active_user)).thenReturn("active_user")
        `when`(mockContext.getString(R.string.user_name)).thenReturn("user_name")
        `when`(mockContext.getString(R.string.insulin_per_10g)).thenReturn("insulin_per_10g")
        `when`(mockContext.getString(R.string.insulin_per_1mmol_l)).thenReturn("insulin_per_1mmol_l")
        `when`(mockContext.getString(R.string.lower_bounds_glucose_level)).thenReturn("lower_bounds_glucose_level")
        `when`(mockContext.getString(R.string.upper_bounds_glucose_level)).thenReturn("upper_bounds_glucose_level")

        preferenceManager = PreferenceManager(mockContext)
    }

    @Test
    fun testIsFirstTime() {
        `when`(mockSharedPreferences.getBoolean("isFirstTime", true)).thenReturn(true)
        val result = preferenceManager.isFirstTime()
        assert(result)
    }

    @Test
    fun testSetFirstTime() {
        preferenceManager.setFirstTime(false)
        verify(mockEditor).putBoolean("isFirstTime", false)
        verify(mockEditor).apply()
    }

    @Test
    fun testSetUserinfo() {
        val user = User(
            name = mutableStateOf("testUser"),
            insulinPer10gCarbs = mutableStateOf(1.0f),
            insulinPer1mmolL = mutableStateOf(2.0f),
            lowerBoundGlucoseLevel = mutableStateOf(3.0f),
            upperBoundGlucoseLevel = mutableStateOf(4.0f)
        )
        preferenceManager.setUserinfo(user, mockContext)
        verify(mockEditor).putString("user_name0", "testUser")
        verify(mockEditor).putString("insulin_per_10g0", "1.0")
        verify(mockEditor).putString("insulin_per_1mmol_l0", "2.0")
        verify(mockEditor).putString("lower_bounds_glucose_level0", "3.0")
        verify(mockEditor).putString("upper_bounds_glucose_level0", "4.0")
        verify(mockEditor).apply()
    }

    @Test
    fun testGetAllUserInfo() {
        `when`(mockSharedPreferences.getString("user_name0", null)).thenReturn("testUser")
        `when`(mockSharedPreferences.getString("insulin_per_10g0", "0.0f")).thenReturn("1.0f")
        `when`(mockSharedPreferences.getString("insulin_per_1mmol_l0", "0.0f")).thenReturn("2.0f")
        `when`(mockSharedPreferences.getString("lower_bounds_glucose_level0", "4.0f")).thenReturn("3.0f")
        `when`(mockSharedPreferences.getString("upper_bounds_glucose_level0", "8.0f")).thenReturn("4.0f")

        val userInfo = preferenceManager.getAllUserInfo(mockContext)
        assert(userInfo.isNotEmpty())
        assert(userInfo[0].name.value == "testUser")
    }

    @Test
    fun testGetActiveUserInfo() {
        `when`(mockSharedPreferences.getString("active_user", "0")).thenReturn("0")
        `when`(mockSharedPreferences.getString("user_name0", null)).thenReturn("testUser")
        `when`(mockSharedPreferences.getString("insulin_per_10g0", "0.0f")).thenReturn("1.0f")
        `when`(mockSharedPreferences.getString("insulin_per_1mmol_l0", "0.0f")).thenReturn("2.0f")
        `when`(mockSharedPreferences.getString("lower_bounds_glucose_level0", "4.0f")).thenReturn("3.0f")
        `when`(mockSharedPreferences.getString("upper_bounds_glucose_level0", "8.0f")).thenReturn("4.0f")

        preferenceManager.switchToActiveUser(mockContext)
        val activeUserInfo = preferenceManager.getActiveUserInfo(mockContext).value
        assert(activeUserInfo != null)
        assert(activeUserInfo?.name?.value == "testUser")
    }

    @Test
    fun testGetUserInfo() {
        val userName = "testUser"
        val userIndex = 0u

        preferenceManager.addUser(
            User(
                name = mutableStateOf(userName),
                insulinPer10gCarbs = mutableStateOf(1.0f),
                insulinPer1mmolL = mutableStateOf(2.0f),
                lowerBoundGlucoseLevel = mutableStateOf(3.0f),
                upperBoundGlucoseLevel = mutableStateOf(4.0f)
            ),
            mockContext
        )

        `when`(mockSharedPreferences.getString("user_name$userIndex", null)).thenReturn(userName)
        `when`(mockSharedPreferences.getString("insulin_per_10g$userIndex", "0.0f")).thenReturn("1.0f")
        `when`(mockSharedPreferences.getString("insulin_per_1mmol_l$userIndex", "0.0f")).thenReturn("2.0f")
        `when`(mockSharedPreferences.getString("lower_bounds_glucose_level$userIndex", "4.0f")).thenReturn("3.0f")
        `when`(mockSharedPreferences.getString("upper_bounds_glucose_level$userIndex", "8.0f")).thenReturn("4.0f")

        val userInfo = preferenceManager.getUserInfo(userName, mockContext)
        assert(userInfo.name.value == userName)
        assert(userInfo.insulinPer10gCarbs.value == 1.0f)
        assert(userInfo.insulinPer1mmolL.value == 2.0f)
        assert(userInfo.lowerBoundGlucoseLevel.value == 3.0f)
        assert(userInfo.upperBoundGlucoseLevel.value == 4.0f)
    }

    @Test
    fun testAddUser() {
        val user = User(
            name = mutableStateOf("newUser"),
            insulinPer10gCarbs = mutableStateOf(1.0f),
            insulinPer1mmolL = mutableStateOf(2.0f),
            lowerBoundGlucoseLevel = mutableStateOf(3.0f),
            upperBoundGlucoseLevel = mutableStateOf(4.0f)
        )
        val result = preferenceManager.addUser(user, mockContext)
        assert(result)

        verify(mockEditor).putString("user_name0", "newUser")
        verify(mockEditor).putString("insulin_per_10g0", "1.0")
        verify(mockEditor).putString("insulin_per_1mmol_l0", "2.0")
        verify(mockEditor).putString("lower_bounds_glucose_level0", "3.0")
        verify(mockEditor).putString("upper_bounds_glucose_level0", "4.0")
        verify(mockEditor).putString("userUid", "1")
        verify(mockEditor).apply()
    }

    @Test
    fun testEditUser() {
        val user = User(
            name = mutableStateOf("editUser"),
            insulinPer10gCarbs = mutableStateOf(1.5f),
            insulinPer1mmolL = mutableStateOf(2.5f),
            lowerBoundGlucoseLevel = mutableStateOf(3.5f),
            upperBoundGlucoseLevel = mutableStateOf(4.5f)
        )

        preferenceManager.addUser(user, mockContext)
        preferenceManager.editUser(user, mockContext)

        verify(mockEditor).putString("user_name0", "editUser")
        verify(mockEditor).putString("insulin_per_10g0", "1.5")
        verify(mockEditor).putString("insulin_per_1mmol_l0", "2.5")
        verify(mockEditor).putString("lower_bounds_glucose_level0", "3.5")
        verify(mockEditor).putString("upper_bounds_glucose_level0", "4.5")
        verify(mockEditor, times(2)).apply()
    }

    @Test
    fun testDeleteUser() {
        val userName = "deleteUser"
        val user = User(
            name = mutableStateOf(userName),
            insulinPer10gCarbs = mutableStateOf(1.0f),
            insulinPer1mmolL = mutableStateOf(2.0f),
            lowerBoundGlucoseLevel = mutableStateOf(3.0f),
            upperBoundGlucoseLevel = mutableStateOf(4.0f)
        )

        preferenceManager.addUser(user, mockContext)
        preferenceManager.deleteUser(userName, mockContext)

        verify(mockEditor).remove("user_name0")
        verify(mockEditor).remove("insulin_per_10g0")
        verify(mockEditor).remove("insulin_per_1mmol_l0")
        verify(mockEditor).remove("lower_bounds_glucose_level0")
        verify(mockEditor).remove("upper_bounds_glucose_level0")
        verify(mockEditor).apply()
    }

    @Test
    fun testSetActiveUserIndex() {
        val userIndex = 1u
        preferenceManager.setActiveUserIndex(userIndex, mockContext)
        assert(preferenceManager.getActiveUserIndex() == userIndex)
        verify(mockEditor).putString("active_user", userIndex.toString())
        verify(mockEditor).apply()
    }

    @Test
    fun testSwitchToActiveUser() {
        `when`(mockSharedPreferences.getString("active_user", null)).thenReturn("1")
        val userName = "activeUser"
        val user = User(
            name = mutableStateOf(userName),
            insulinPer10gCarbs = mutableStateOf(1.0f),
            insulinPer1mmolL = mutableStateOf(2.0f),
            lowerBoundGlucoseLevel = mutableStateOf(3.0f),
            upperBoundGlucoseLevel = mutableStateOf(4.0f)
        )
        preferenceManager.addUser(user, mockContext)
        preferenceManager.setActiveUserIndex(1u, mockContext)

        preferenceManager.switchToActiveUser(mockContext)
        assert(preferenceManager.getActiveUserIndex() == 1u)
    }
}
