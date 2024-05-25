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

    // Validation errors
    val nameError = mutableStateOf<String?>(null)
    val insulinPer10gCarbsError = mutableStateOf<String?>(null)
    val insulinPer1mmolLError = mutableStateOf<String?>(null)
    val upperBoundGlucoseLevelError = mutableStateOf<String?>(null)
    val lowerBoundGlucoseLevelError = mutableStateOf<String?>(null)

    fun updateUserName(newName: String) {
        userProfileState.value.name.value = newName
        validateUserName()
    }

    fun updateInsulinPer10gCarbs(newValue: Float) {
        userProfileState.value.insulinPer10gCarbs.value = newValue
        validateInsulinPer10gCarbs()
    }

    fun updateInsulinPer1mmolL(newValue: Float) {
        userProfileState.value.insulinPer1mmolL.value = newValue
        validateInsulinPer1mmolL()
    }

    fun updateUpperBoundGlucoseLevel(newValue: Float) {
        userProfileState.value.upperBoundGlucoseLevel.value = newValue
        validateUpperBoundGlucoseLevel()
    }

    fun updateLowerBoundGlucoseLevel(newValue: Float) {
        userProfileState.value.lowerBoundGlucoseLevel.value = newValue
        validateLowerBoundGlucoseLevel()
    }

    private fun validateUserName() {
        nameError.value = if (userProfileState.value.name.value.isBlank()) {
            "Name cannot be empty"
        } else {
            null
        }
    }

    private fun validateInsulinPer10gCarbs() {
        insulinPer10gCarbsError.value = if (userProfileState.value.insulinPer10gCarbs.value <= 0) {
            "Value must be greater than 0"
        } else {
            null
        }
    }

    private fun validateInsulinPer1mmolL() {
        insulinPer1mmolLError.value = if (userProfileState.value.insulinPer1mmolL.value <= 0) {
            "Value must be greater than 0"
        } else {
            null
        }
    }

    private fun validateUpperBoundGlucoseLevel() {
        upperBoundGlucoseLevelError.value = if (userProfileState.value.upperBoundGlucoseLevel.value <= 0) {
            "Value must be greater than 0"
        } else {
            null
        }
    }

    private fun validateLowerBoundGlucoseLevel() {
        lowerBoundGlucoseLevelError.value = if (userProfileState.value.lowerBoundGlucoseLevel.value <= 0) {
            "Value must be greater than 0"
        } else {
            null
        }
    }

    fun validateAll(): Boolean {
        validateUserName()
        validateInsulinPer10gCarbs()
        validateInsulinPer1mmolL()
        validateUpperBoundGlucoseLevel()
        validateLowerBoundGlucoseLevel()
        return nameError.value == null &&
                insulinPer10gCarbsError.value == null &&
                insulinPer1mmolLError.value == null &&
                upperBoundGlucoseLevelError.value == null &&
                lowerBoundGlucoseLevelError.value == null
    }

    fun saveUserPreferences(onCompleted: (UserPreferences) -> Unit) {
        if (validateAll()) {
            viewModelScope.launch {
                onCompleted(userProfileState.value)
            }
        }
    }
}
