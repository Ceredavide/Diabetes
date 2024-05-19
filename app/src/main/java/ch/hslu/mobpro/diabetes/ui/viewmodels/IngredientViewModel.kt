package ch.hslu.mobpro.diabetes.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
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
}