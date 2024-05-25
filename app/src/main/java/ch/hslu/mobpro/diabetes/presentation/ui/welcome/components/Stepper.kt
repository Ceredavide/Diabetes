package ch.hslu.mobpro.diabetes.presentation.ui.welcome.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ch.hslu.mobpro.diabetes.presentation.theme.Typography

@Composable
fun StepTitle(title: String) {
    Text(title, style = Typography.h1)
}

@Composable
fun StepContent(content: @Composable () -> Unit) {
    content()
}

@Composable
fun StepperButtons(onBack: () -> Unit, onNext: () -> Unit, isFirstStep: Boolean, isLastStep: Boolean) {
    Row {
        if (!isFirstStep) {
            Button(onClick = onBack) {
                Text("Indietro")
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        Button(onClick = onNext) {
            Text(if (!isLastStep) "Next" else "Completa")
        }
    }
}
