package ch.hslu.mobpro.diabetes.ui.screens.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.hslu.mobpro.diabetes.ui.theme.Typography

data class Step(val number: Int, val title: String, val content: @Composable () -> Unit)

data class UserPreferences(
    var name: MutableState<String>, var age: MutableState<String>, var email: MutableState<String>
)


@Composable
fun WelcomeScreen(onCompleted: (userInfo : UserPreferences) -> Unit) {

    val userProfileState by remember {
        mutableStateOf(
            UserPreferences(
                name = mutableStateOf("Name"),
                age = mutableStateOf("30"),
                email = mutableStateOf("email@example.com")
            )
        )
    }

    val steps: List<Step> = listOf(Step(0, "Welcome to Diabetes") { Text("Pls use this app") },
        Step(1, "Istruzioni sull'uso dell'app") { UserInfoForm(userProfileState) },
        Step(2, "You're done!") { Text("You're done!") })

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
                    0 -> {
                        currentStep = steps[1]
                    }

                    1 -> {

                        currentStep = steps[2]
                    }

                    2 -> {
                        onCompleted(userProfileState)
                    }
                }
            },
            isFirstStep = currentStep.number == 0,
            isLastStep = currentStep.number == steps.size - 1
        )
    }
}


@Composable
fun UserInfoForm(userProfileState: UserPreferences) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        OutlinedTextField(
            value = userProfileState.name.value,
            onValueChange = { userProfileState.name.value = it },
            label = { Text("Inserisci qualcosa") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            })
        )
        OutlinedTextField(
            value = userProfileState.age.value,
            onValueChange = { userProfileState.age.value = it },
            label = { Text("Inserisci qualcosa 2") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number).copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            })
        )
        OutlinedTextField(
            value = userProfileState.email.value,
            onValueChange = { userProfileState.email.value = it },
            label = { Text("Inserisci qualcosa 3") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            })
        )
    }
}

@Composable
fun StepTitle(title: String) {
    Text(
        title, style = Typography.titleLarge
    )
}

@Composable
fun StepContent(content: @Composable () -> Unit) {
    content()
}

@Composable
fun StepperButtons(
    onBack: () -> Unit, onNext: () -> Unit, isFirstStep: Boolean, isLastStep: Boolean
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
