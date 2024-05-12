package ch.hslu.mobpro.diabetes.ui.screens.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Step(val number: Int, val title: String, val content: @Composable () -> Unit)

@Composable
fun WelcomeScreen(onCompleted: () -> Unit) {
    val steps: List<Step> = listOf(
        Step(0, "Step 1: Welcome to Diabetes") { Text("Pls use this app") },
        Step(1, "Step 2: Istruzioni sull'uso dell'app") { Text("Pls insert data") },
        Step(2, "Step 3: You're done!") { Text("You're done!") }
    )

    var currentStep by remember { mutableStateOf(steps.first()) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = currentStep.title)

        Spacer(modifier = Modifier.height(8.dp))

        currentStep.content()

        Spacer(modifier = Modifier.height(16.dp))

        NavigationButtons(
            currentStep = currentStep,
            onBack = { currentStep = steps[currentStep.number - 1] },
            onNext = {
                if (currentStep.number < steps.size - 1) {
                    currentStep = steps[currentStep.number + 1]
                } else {
                    onCompleted()
                }
            },
            isFirstStep = currentStep.number == 0,
            isLastStep = currentStep.number == steps.size - 1
        )
    }
}

@Composable
fun NavigationButtons(
    currentStep: Step,
    onBack: () -> Unit,
    onNext: () -> Unit,
    isFirstStep: Boolean,
    isLastStep: Boolean
) {
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

@Preview(showBackground = true)
@Composable
fun PreviewWelcomeScreen() {
    WelcomeScreen(onCompleted = { /* do nothing in preview */ })
}
