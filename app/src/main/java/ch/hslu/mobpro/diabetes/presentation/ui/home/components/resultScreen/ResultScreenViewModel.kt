package ch.hslu.mobpro.diabetes.presentation.ui.home.components.resultScreen

import android.content.Context
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.ViewModel
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import ch.hslu.mobpro.diabetes.utils.Ingredient
import ch.hslu.mobpro.diabetes.utils.calculateInsulinDoseAndTotalCarbs

class ResultScreenViewModel : ViewModel() {
    val userName = mutableStateOf("")
    private val insulinPer10gCarbs = mutableFloatStateOf(0f)
    private val insulinPer1mmol_L = mutableFloatStateOf(0f)
    private val lowerBoundGlucoseLevel = mutableFloatStateOf(0f)
    private val upperBoundGlucoseLevel = mutableFloatStateOf(0f)
    val insulinDose = mutableIntStateOf(0)
    val totalCarbs = mutableFloatStateOf(0f)
    val textDecoration = mutableStateOf(TextDecoration.None)
    val color = mutableStateOf(Color.Black)

    fun loadUserInfo(context: Context) {
        val userInfo = PreferenceManager.instance.getActiveUserInfo(context)
        userName.value = userInfo.value?.name?.value ?: ""
        insulinPer10gCarbs.floatValue = userInfo.value?.insulinPer10gCarbs?.value ?: 0f
        insulinPer1mmol_L.floatValue = userInfo.value?.insulinPer1mmolL?.value ?: 0f
        lowerBoundGlucoseLevel.floatValue = userInfo.value?.lowerBoundGlucoseLevel?.value ?: 0f
        upperBoundGlucoseLevel.floatValue = userInfo.value?.upperBoundGlucoseLevel?.value ?: 0f
    }

    fun calculateInsulinAndCarbs(glucoseLevel: Float, ingredients: List<Ingredient>) {
        val (insulin, carbs) = calculateInsulinDoseAndTotalCarbs(
            insulinPer10gCarbs = insulinPer10gCarbs.floatValue,
            insulinPer1mmol_L = insulinPer1mmol_L.floatValue,
            lowerBoundGlucoseLevel = lowerBoundGlucoseLevel.floatValue,
            upperBoundGlucoseLevel = upperBoundGlucoseLevel.floatValue,
            glucoseLevel = glucoseLevel,
            ingredients = ingredients
        )
        insulinDose.intValue = insulin
        totalCarbs.floatValue = carbs
        val decorationColor = getTextDecorationAndColor(glucoseLevel)
        textDecoration.value = decorationColor.first
        color.value = decorationColor.second
    }

    private fun getTextDecorationAndColor(glucoseLevel: Float): Pair<TextDecoration, Color> {
        return if (glucoseLevel < lowerBoundGlucoseLevel.floatValue || glucoseLevel > upperBoundGlucoseLevel.floatValue) {
            TextDecoration.Underline to Color.Red
        } else {
            TextDecoration.None to Color.Black
        }
    }
}
