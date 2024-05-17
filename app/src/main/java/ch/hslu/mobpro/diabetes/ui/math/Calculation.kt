package ch.hslu.mobpro.diabetes.ui.math

import kotlin.math.pow
import kotlin.math.roundToInt

fun Float.truncate(digits: Int): Float {

    return (this * 10.0f.pow(digits)).roundToInt().toFloat() / 10.0f.pow(digits)
}
fun calculateInsulinDose(insulinPer10gCarbs: Float,
                         insulinPer1mmmol_L: Float,
                         lowerBorderGlucose: Float,
                         upperBorderGlucose: Float,
                         glucoseLevel: Float,
                         ingredients:List<Ingredient>): Float {

    val totalCarbs = ingredients.map {
       it.calculateCarbs()
    }.sum()

    var insulinDose = 0.0f
    if (glucoseLevel < lowerBorderGlucose) {

        insulinDose = calculateTooLow(
            insulinPer10gCarbs = insulinPer10gCarbs,
            insulinePer1mmmol_L = insulinPer1mmmol_L,
            carbs = totalCarbs,
            glucoseLevel = glucoseLevel
        )
    }
    else if (glucoseLevel > upperBorderGlucose) {

        insulinDose = calculateTooHigh(
            insulinPer10gCarbs = insulinPer10gCarbs,
            insulinePer1mmmol_L = insulinPer1mmmol_L,
            carbs = totalCarbs,
            glucoseLevel = glucoseLevel
        )
    }
    else {

        insulinDose = calculateNormal(
            insulinPer10gCarbs = insulinPer10gCarbs,
            carbs = totalCarbs
        )
    }

    return insulinDose.truncate(1)
}

fun calculateNormal(insulinPer10gCarbs: Float, carbs: Float): Float {

    return insulinPer10gCarbs / 10.0f * carbs
}
fun calculateTooLow(insulinPer10gCarbs: Float, insulinePer1mmmol_L: Float, carbs: Float, glucoseLevel: Float): Float {

    return calculateNormal(insulinPer10gCarbs, carbs) - calculateCorrectionTooLow(insulinePer1mmmol_L, glucoseLevel)
}

fun calculateCorrectionTooLow(insulinePer1mmmol_L: Float, glucoseLevel: Float): Float {

    val normalValue = 5.5f
    val differenceToNormalValue = normalValue - glucoseLevel

    return insulinePer1mmmol_L * differenceToNormalValue
}
fun calculateTooHigh(insulinPer10gCarbs: Float, insulinePer1mmmol_L: Float, carbs: Float, glucoseLevel: Float): Float {

    return calculateNormal(insulinPer10gCarbs, carbs) + calculateCorrectionTooHigh(insulinePer1mmmol_L, glucoseLevel)
}

fun calculateCorrectionTooHigh(insulinePer1mmmol_L: Float, glucoseLevel: Float): Float {

    val normalValue = 5.5f
    val differenceToNormalValue = glucoseLevel - normalValue

    return insulinePer1mmmol_L * differenceToNormalValue
}
