package ch.hslu.mobpro.diabetes.presentation.ui.welcome

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class UserPreferences(
    var name: MutableState<String> = mutableStateOf(""),
    var insulinPer10gCarbs: MutableState<Float> = mutableFloatStateOf(0.0f),
    var insulinPer1mmolL: MutableState<Float> = mutableFloatStateOf(0.0f),
    var upperBoundGlucoseLevel: MutableState<Float> = mutableFloatStateOf(8.0f),
    var lowerBoundGlucoseLevel: MutableState<Float> = mutableFloatStateOf(4.0f)
)

class WelcomeScreenViewModel : ViewModel() {
    val userProfileState = mutableStateOf(UserPreferences())

    fun updateUserName(newName: String) {
        userProfileState.value.name.value = newName
    }

    fun updateInsulinPer10gCarbs(newValue: Float) {
        userProfileState.value.insulinPer10gCarbs.value = newValue
    }

    fun updateInsulinPer1mmolL(newValue: Float) {
        userProfileState.value.insulinPer1mmolL.value = newValue
    }

    fun updateUpperBoundGlucoseLevel(newValue: Float) {
        userProfileState.value.upperBoundGlucoseLevel.value = newValue
    }

    fun updateLowerBoundGlucoseLevel(newValue: Float) {
        userProfileState.value.lowerBoundGlucoseLevel.value = newValue
    }

    fun saveUserPreferences(onCompleted: (UserPreferences) -> Unit) {
        viewModelScope.launch {
            onCompleted(userProfileState.value)
        }
    }
}
