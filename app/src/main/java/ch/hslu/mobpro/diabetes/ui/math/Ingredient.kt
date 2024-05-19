package ch.hslu.mobpro.diabetes.ui.math

import androidx.compose.ui.geometry.times
import ch.hslu.mobpro.diabetes.data.database.Product

class Ingredient(val product: Product, var weightAmount: Float?) {

    fun calculateCarbs(): Float {

        return (product.carbs?.div(100.0f) ?: 1.0f) * weightAmount!!
    }
}