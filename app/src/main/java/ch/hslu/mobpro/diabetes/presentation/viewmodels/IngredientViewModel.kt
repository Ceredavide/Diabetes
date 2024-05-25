package ch.hslu.mobpro.diabetes.presentation.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import ch.hslu.mobpro.diabetes.math.Ingredient

class IngredientViewModel: ViewModel() {

    var ingredients = mutableStateListOf<Ingredient>()
    fun addIngredient(ingredient: Ingredient) {

        ingredients += ingredient
    }

    fun removeIngredient(ingredient: Ingredient) {

        ingredients -= ingredient
    }
    fun clearIngredients() {

        ingredients.clear()
    }

    fun contains(productName: String): Boolean {

        return ingredients.any { it.product.name == productName }
    }
}