package ch.hslu.mobpro.diabetes.presentation.ui.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.presentation.ui.welcome.components.*

data class Step(val number: Int, val title: String, val content: @Composable () -> Unit)

data class UserPreferences(
    var name: MutableState<String>,
    var insulinPer10gCarbs: MutableState<Float>,
    var insulinPer1mmolL: MutableState<Float>,
    var upperBoundGlucoseLevel: MutableState<Float>,
    var lowerBoundGlucoseLevel: MutableState<Float>
)

@Composable
fun WelcomeScreen(onCompleted: (userInfo: UserPreferences) -> Unit) {
    val userProfileState = remember {
        mutableStateOf(
            UserPreferences(
                name = mutableStateOf(""),
                insulinPer10gCarbs = mutableFloatStateOf(0.0f),
                insulinPer1mmolL = mutableFloatStateOf(0.0f),
                upperBoundGlucoseLevel = mutableFloatStateOf(8.0f),
                lowerBoundGlucoseLevel = mutableFloatStateOf(4.0f)
            )
        )
    }

    val steps: List<Step> = listOf(
        Step(0, "Welcome to Diabetes") { Text(stringResource(R.string.welcome_text)) },
        Step(1, "Istruzioni sull'uso dell'app") { UserInfoForm(userProfileState.value) },
        Step(2, "You're done!") { Text(stringResource(R.string.welcome_steup_complete)) }
    )

    var currentStep by remember { mutableStateOf(steps.first()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepTitle(title = currentStep.title)

        Spacer(modifier = Modifier.height(8.dp))

        StepContent { currentStep.content() }

        Spacer(modifier = Modifier.height(16.dp))

        StepperButtons(
            onBack = { currentStep = steps[currentStep.number - 1] },
            onNext = {
                when (currentStep.number) {
                    0 -> currentStep = steps[1]
                    1 -> currentStep = steps[2]
                    2 -> onCompleted(userProfileState.value)
                }
            },
            isFirstStep = currentStep.number == 0,
            isLastStep = currentStep.number == steps.size - 1
        )
    }
}
