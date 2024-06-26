package ch.hslu.mobpro.diabetes.presentation.ui.welcome

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.domain.model.User
import ch.hslu.mobpro.diabetes.presentation.common.shared_components.UserForm
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.UserFormViewModel
import kotlinx.coroutines.launch

data class Step(val number: Int, val title: String, val content: @Composable () -> Unit)

class WelcomeViewModel : ViewModel() {

    private val userFormViewModel = UserFormViewModel()

    val steps: List<Step> = listOf(
        Step(0, "Welcome to Diabetes") { Text(stringResource(id = R.string.welcome_text)) },
        Step(1, "Set User Infos") { UserForm(userFormViewModel) },
        Step(2, "You're done!") { Text(stringResource(id = R.string.welcome_steup_complete)) }
    )

    var currentStep by mutableStateOf(steps.first())
        private set

    fun goToNextStep(onCompleted: (User) -> Unit) {
        when (currentStep.number) {
            0 -> currentStep = steps[1]
            1 -> {
                if (userFormViewModel.validateAll()) {
                    currentStep = steps[2]
                }
            }

            2 -> saveUserPreferences(userFormViewModel.userProfileState.value, onCompleted)
        }
    }

    fun goToPreviousStep() {
        if (currentStep.number > 0) {
            currentStep = steps[currentStep.number - 1]
        }
    }

    private fun saveUserPreferences(user: User, onCompleted: (User) -> Unit) {
        viewModelScope.launch {
            onCompleted(user)
        }
    }
}
