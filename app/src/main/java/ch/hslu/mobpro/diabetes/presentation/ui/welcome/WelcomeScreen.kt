package ch.hslu.mobpro.diabetes.presentation.ui.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.presentation.common.shared_components.UserInfoForm
import ch.hslu.mobpro.diabetes.presentation.ui.welcome.components.*

data class Step(val number: Int, val title: String, val content: @Composable () -> Unit)

@Composable
fun WelcomeScreen(
    onCompleted: (userInfo: UserPreferences) -> Unit,
    viewModel: WelcomeViewModel = viewModel()
) {
    val userProfileState = viewModel.userProfileState

    val steps: List<Step> = listOf(
        Step(0, "Welcome to Diabetes") { Text(stringResource(R.string.welcome_text)) },
        Step(1, "Istruzioni sull'uso dell'app") { UserInfoForm(userProfileState.value, viewModel) },
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
                    1 -> {
                        if (viewModel.validateAll()) {
                            currentStep = steps[2]
                        }
                    }
                    2 -> viewModel.saveUserPreferences(onCompleted)
                }
            },
            isFirstStep = currentStep.number == 0,
            isLastStep = currentStep.number == steps.size - 1
        )
    }
}
