package ch.hslu.mobpro.diabetes.ui.math

import ch.hslu.mobpro.diabetes.database.Product

class Ingredient(val product: Product, val weightAmount: Float) {

    fun calculateCarbs(): Float {

        return (product.carbs?.div(100.0f) ?: 1.0f) * weightAmount
    }
}