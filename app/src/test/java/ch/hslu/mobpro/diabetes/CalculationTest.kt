package ch.hslu.mobpro.diabetes

import ch.hslu.mobpro.diabetes.database.Product
import ch.hslu.mobpro.diabetes.ui.math.Ingredient
import ch.hslu.mobpro.diabetes.ui.math.calculateCorrectionTooHigh
import ch.hslu.mobpro.diabetes.ui.math.calculateInsulinDoseAndTotalCarbs
import ch.hslu.mobpro.diabetes.ui.math.calculateNormal
import ch.hslu.mobpro.diabetes.ui.math.calculateTooHigh
import ch.hslu.mobpro.diabetes.ui.math.calculateTooLow
import ch.hslu.mobpro.diabetes.ui.math.truncate
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculationTest {

    @Test
    fun testTruncate() {

        val x = 1.761279f
        val y = 1.0f

        assertEquals(1.76f, x.truncate(2))
        assertEquals(1.761f, x.truncate(3))
        assertEquals(1.0f, y.truncate(3))
    }
    @Test
    fun testCalculateInsulinDoseNormal() {

        val glucoseLevel = 5.0f
        val insulinPer1mmol_L = 0.5f
        val insulinPer10gCarbs = 1.2f
        val lowrBoundGlucoseLevel = 4.0f
        val upperBoundGlucoseLevel = 8.0f

        val ingredients = listOf(
            Ingredient(
                product = Product(name = "Banane", carbs = 20.0f),
                weightAmount = 200.0f
            ),
            Ingredient(
                product = Product(name = "White Bread", carbs = 50.0f),
                weightAmount = 200.0f
            ),
        )

        val (insulinDose, totalCarbs) = calculateInsulinDoseAndTotalCarbs(
            insulinPer10gCarbs = insulinPer10gCarbs,
            insulinPer1mmmol_L = insulinPer1mmol_L,
            lowerBoundGlucoseLevel = lowrBoundGlucoseLevel,
            upperBoundGlucoseLevel = upperBoundGlucoseLevel,
            glucoseLevel = glucoseLevel,
            ingredients = ingredients
        )

        assertEquals(16.8f, insulinDose)
        assertEquals(140.0f, totalCarbs)
    }
    @Test
    fun testCalculationNormal() {

        val insulinPer10g = 1.2f
        val carbs = 80.0f

        assertEquals(calculateNormal(insulinPer10g, carbs), 9.6f)
    }

    @Test
    fun testCalculationNormalWithZeroGCarbs() {

        val insulinPer10g = 1.2f
        val carbs = 0.0f

        assertEquals(calculateNormal(insulinPer10g, carbs), 0.0f)
    }
    @Test
    fun testCalculateCorrectionTooHigh() {

        val glucoseLevel = 10.0f
        val insulinPer1mmol_L = 0.5f

        assertEquals(calculateCorrectionTooHigh(insulinPer1mmol_L, glucoseLevel), 2.25f)
    }

    @Test
    fun testCalculateTooHigh() {

        val glucoseLevel = 10.0f
        val insulinPer1mmol_L = 0.5f
        val insulinPer10g = 1.2f
        val carbs = 80.0f

        assertEquals(calculateTooHigh(insulinPer10g, insulinPer1mmol_L, carbs, glucoseLevel), 9.6f + 2.25f)
    }

    @Test
    fun testCalculateCorrectionTooLow() {

        val glucoseLevel = 3.0f
        val insulinePer1mmol_L = 0.5f
        val insulinePer10g = 1.2f
        val carbs = 80.0f

        assertEquals(calculateTooLow(insulinePer10g,insulinePer1mmol_L, carbs, glucoseLevel), 9.6f - 1.25f)
    }
}