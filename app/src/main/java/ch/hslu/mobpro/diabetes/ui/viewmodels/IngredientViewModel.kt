package ch.hslu.mobpro.diabetes.ui.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ch.hslu.mobpro.diabetes.ui.math.Ingredient

class IngredientViewModel: ViewModel() {

    var ingredients = mutableStateListOf<Ingredient>()

    fun addIngredient(ingredient: Ingredient) {

        ingredients.add(ingredient)
    }

    fun removeIngredient(ingredient: Ingredient) {

        ingredients.remove(ingredient)
    }
    fun clearIngredients() {

        ingredients.clear()
    }
}