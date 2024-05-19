package ch.hslu.mobpro.diabetes

import ch.hslu.mobpro.diabetes.data.database.Product
import ch.hslu.mobpro.diabetes.ui.math.Ingredient
import org.junit.Assert.assertEquals
import org.junit.Test

class IngredientTest {

    @Test
    fun testCalculateTotalCarbs() {

        val ingredient = Ingredient(
            product = Product(name = "Banane", carbs = 20.0f),
            weightAmount = 200.0f
        )

        assertEquals(40.0f, ingredient.calculateCarbs())
    }
}