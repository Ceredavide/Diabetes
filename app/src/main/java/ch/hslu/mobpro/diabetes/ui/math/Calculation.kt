package ch.hslu.mobpro.diabetes.ui.math

import kotlin.math.pow
import kotlin.math.roundToInt

fun Float.truncate(digits: Int): Float {

    return (this * 10.0f.pow(digits)).roundToInt().toFloat() / 10.0f.pow(digits)
}
fun calculateInsulinDoseAndTotalCarbs(insulinPer10gCarbs: Float,
                                      insulinPer1mmol_L: Float,
                                      lowerBoundGlucoseLevel: Float,
                                      upperBoundGlucoseLevel: Float,
                                      glucoseLevel: Float,
                                      ingredients:List<Ingredient>): Pair<Float, Float> {

    val totalCarbs = ingredients.map {
       it.calculateCarbs()
    }.sum()

    val insulinDose: Float
    if (glucoseLevel < lowerBoundGlucoseLevel) {

        insulinDose = calculateTooLow(
            insulinPer10gCarbs = insulinPer10gCarbs,
            insulinePer1mmol_L = insulinPer1mmol_L,
            carbs = totalCarbs,
            glucoseLevel = glucoseLevel
        )
    }
    else if (glucoseLevel > upperBoundGlucoseLevel) {

        insulinDose = calculateTooHigh(
            insulinPer10gCarbs = insulinPer10gCarbs,
            insulinePer1mmol_L = insulinPer1mmol_L,
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

    return Pair(insulinDose.truncate(1), totalCarbs)
}

fun calculateNormal(insulinPer10gCarbs: Float, carbs: Float): Float {

    return insulinPer10gCarbs / 10.0f * carbs
}
fun calculateTooLow(insulinPer10gCarbs: Float, insulinePer1mmol_L: Float, carbs: Float, glucoseLevel: Float): Float {

    return calculateNormal(insulinPer10gCarbs, carbs) - calculateCorrectionTooLow(insulinePer1mmol_L, glucoseLevel)
}

fun calculateCorrectionTooLow(insulinePer1mmol_L: Float, glucoseLevel: Float): Float {

    val normalValue = 5.5f
    val differenceToNormalValue = normalValue - glucoseLevel

    return insulinePer1mmol_L * differenceToNormalValue
}
fun calculateTooHigh(insulinPer10gCarbs: Float, insulinePer1mmol_L: Float, carbs: Float, glucoseLevel: Float): Float {

    return calculateNormal(insulinPer10gCarbs, carbs) + calculateCorrectionTooHigh(insulinePer1mmol_L, glucoseLevel)
}

fun calculateCorrectionTooHigh(insulinePer1mmol_L: Float, glucoseLevel: Float): Float {

    val normalValue = 5.5f
    val differenceToNormalValue = glucoseLevel - normalValue

    return insulinePer1mmol_L * differenceToNormalValue
}
