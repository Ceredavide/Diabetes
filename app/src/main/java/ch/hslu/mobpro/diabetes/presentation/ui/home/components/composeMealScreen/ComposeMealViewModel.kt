package ch.hslu.mobpro.diabetes.presentation.ui.home.components.composeMealScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import ch.hslu.mobpro.diabetes.data.database.entity.GlucoseReading
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.GlucoseReadingsViewModel
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.IngredientViewModel
import ch.hslu.mobpro.diabetes.presentation.navigation.Routes
import ch.hslu.mobpro.diabetes.utils.Ingredient
import java.util.Date

class ComposeMealViewModel : ViewModel() {

    var glucoseLevelString = mutableStateOf("0.0")
    private var glucoseLevel = mutableFloatStateOf(0.0f)
    var color = mutableStateOf(Color.Transparent)
    var check = mutableStateOf(false)

    fun onCalculateClicked(
        navController: NavController,
        ingredients: SnapshotStateList<Ingredient>,
        glucoseReadingsViewModel: GlucoseReadingsViewModel,
        context: Context
    ) {
        if (validateIngredients(ingredients) && validateGlucoseLevel(glucoseLevelString.value.toFloatOrNull())) {
            glucoseLevel.floatValue = glucoseLevelString.value.toFloat()
            persistGlucoseReading(glucoseLevel.floatValue, glucoseReadingsViewModel)
            navController.navigate(Routes.resultScreen(glucoseLevel.floatValue))
        } else {
            check.value = true
            handleInvalidInput(context, ingredients)
        }
    }

    private fun handleInvalidInput(context: Context, ingredients: SnapshotStateList<Ingredient>) {
        if (ingredients.isEmpty()) {
            Toast.makeText(context, "PLEASE ADD PRODUCTS TO THIS MEAL", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "PLEASE ENTER MISSING VALUES", Toast.LENGTH_LONG).show()
        }
    }

    fun onClearClicked(ingredientViewModel: IngredientViewModel) {
        ingredientViewModel.clearIngredients()
    }

    fun onGlucoseLevelChanged(newValue: String) {
        glucoseLevelString.value = newValue
        val glucoseLevelFloat = newValue.toFloatOrNull()
        if (newValue.isEmpty() || glucoseLevelFloat == null || glucoseLevelFloat == 0.0f) {
            color.value = Color.Red
        } else {
            color.value = Color.Transparent
            glucoseLevel.floatValue = glucoseLevelFloat
        }
    }

    private fun persistGlucoseReading(glucoseLevel: Float, glucoseReadingsViewModel: GlucoseReadingsViewModel) {
        val currentTime = Date()
        val glucoseReading = GlucoseReading(
            userIndex = PreferenceManager.instance.getActiveUserIndex().toInt(),
            glucoseLevel = glucoseLevel,
            time = currentTime
        )
        glucoseReadingsViewModel.addGlucoseReading(glucoseReading)
    }

    private fun validateGlucoseLevel(glucoseLevel: Float?): Boolean {
        return glucoseLevel != null && glucoseLevel > 0.0f
    }

    private fun validateIngredients(ingredientsState: SnapshotStateList<Ingredient>): Boolean {
        if (ingredientsState.isEmpty()) {
            return false
        }
        if (ingredientsState.any { it.weightAmount == null || it.weightAmount == 0.0f }) {
            return false
        }
        return true
    }
}
