package ch.hslu.mobpro.diabetes.domain.model

import androidx.compose.runtime.Composable
data class Step(val number: Int, val title: String, val content: @Composable () -> Unit)