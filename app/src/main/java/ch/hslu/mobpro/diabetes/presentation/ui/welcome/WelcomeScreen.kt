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
import ch.hslu.mobpro.diabetes.domain.model.User
import ch.hslu.mobpro.diabetes.presentation.common.shared_components.UserForm
import ch.hslu.mobpro.diabetes.presentation.ui.welcome.components.*

@Composable
fun WelcomeScreen(
    onCompleted: (userInfo: User) -> Unit,
    viewModel: WelcomeViewModel = viewModel()
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepTitle(title = viewModel.currentStep.title)

        Spacer(modifier = Modifier.height(8.dp))

        StepContent { viewModel.currentStep.content() }

        Spacer(modifier = Modifier.height(16.dp))

        StepperButtons(
            onBack = { viewModel.goToPreviousStep() },
            onNext = { viewModel.goToNextStep(onCompleted) },
            isFirstStep = viewModel.currentStep.number == 0,
            isLastStep = viewModel.currentStep.number == viewModel.steps.size - 1
        )
    }
}
