package ch.hslu.mobpro.diabetes.utils

import ch.hslu.mobpro.diabetes.data.database.entity.Product

class Ingredient(val product: Product, var weightAmount: Float?) {

    fun calculateCarbs(): Float {

        return (product.carbs?.div(100.0f) ?: 1.0f) * weightAmount!!
    }
}