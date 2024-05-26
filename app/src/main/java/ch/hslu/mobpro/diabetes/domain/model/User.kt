package ch.hslu.mobpro.diabetes.domain.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf

data class User(
    var name: MutableState<String> = mutableStateOf(""),
    var insulinPer10gCarbs: MutableState<Float> = mutableFloatStateOf(0.0f),
    var insulinPer1mmolL: MutableState<Float> = mutableFloatStateOf(0.0f),
    var upperBoundGlucoseLevel: MutableState<Float> = mutableFloatStateOf(8.0f),
    var lowerBoundGlucoseLevel: MutableState<Float> = mutableFloatStateOf(4.0f)
)