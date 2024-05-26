package ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import ch.hslu.mobpro.diabetes.utils.Ingredient

class IngredientViewModel: ViewModel() {

    var ingredients = mutableStateListOf<Ingredient>()
        private set
    fun addIngredient(ingredient: Ingredient) {

        ingredients.add(ingredient)
    }

    fun removeIngredient(ingredient: Ingredient) {

        ingredients.remove(ingredient)
    }
    fun clearIngredients() {

        ingredients.clear()
    }

    fun contains(productName: String): Boolean {

        return ingredients.any { it.product.name == productName }
    }
}