package ch.hslu.mobpro.diabetes.presentation.shared_viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.IngredientViewModel
import ch.hslu.mobpro.diabetes.utils.Ingredient
import ch.hslu.mobpro.diabetes.data.database.entity.Product
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class IngredientViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: IngredientViewModel

    @Before
    fun setUp() {
        viewModel = IngredientViewModel()
    }

    @Test
    fun testAddIngredient() {
        val product = Product(name = "Sugar", carbs = 100.0f)
        val ingredient = Ingredient(product, weightAmount = 100.0f)

        viewModel.addIngredient(ingredient)

        assertTrue(viewModel.ingredients.contains(ingredient))
    }

    @Test
    fun testRemoveIngredient() {
        val product = Product(name = "Sugar", carbs = 100.0f)
        val ingredient = Ingredient(product, weightAmount = 100.0f)

        viewModel.addIngredient(ingredient)
        viewModel.removeIngredient(ingredient)

        assertFalse(viewModel.ingredients.contains(ingredient))
    }

    @Test
    fun testClearIngredients() {
        val product1 = Product(name = "Sugar", carbs = 100.0f)
        val ingredient1 = Ingredient(product1, weightAmount = 100.0f)

        val product2 = Product(name = "Salt", carbs = 0.0f)
        val ingredient2 = Ingredient(product2, weightAmount = 50.0f)

        viewModel.addIngredient(ingredient1)
        viewModel.addIngredient(ingredient2)
        viewModel.clearIngredients()

        assertTrue(viewModel.ingredients.isEmpty())
    }

    @Test
    fun testContains() {
        val product = Product(name = "Sugar", carbs = 100.0f)
        val ingredient = Ingredient(product, weightAmount = 100.0f)

        viewModel.addIngredient(ingredient)

        assertTrue(viewModel.contains("Sugar"))
        assertFalse(viewModel.contains("Salt"))
    }

    @Test
    fun testCalculateCarbs() {
        val product = Product(name = "Sugar", carbs = 100.0f)
        val ingredient = Ingredient(product, weightAmount = 100.0f)

        val carbs = ingredient.calculateCarbs()

        assertEquals(100.0f, carbs, 0.0f)
    }
}
