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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.ui.theme.Typography

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
                insulinPer10gCarbs = mutableStateOf(0.0f),
                insulinPer1mmolL = mutableStateOf(0.0f),
                upperBoundGlucoseLevel = mutableStateOf(8.0f),
                lowerBoundGlucoseLevel = mutableStateOf(4.0f)
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

@Composable
fun UserInfoForm(userProfileState: UserPreferences) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        OutlinedTextField(
            value = userProfileState.name.value,
            onValueChange = { userProfileState.name.value = it },
            label = { Text("User Name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
        )

        FloatTextFieldWithState(
            value = userProfileState.insulinPer10gCarbs,
            label = stringResource(id = R.string.carbs_per_100g)
        )

        FloatTextFieldWithState(
            value = userProfileState.insulinPer1mmolL,
            label = stringResource(id = R.string.insulin_per_1mmol_l)
        )

        FloatTextFieldWithState(
            value = userProfileState.upperBoundGlucoseLevel,
            label = "Maximum Glucose Level",
            keyboardType = KeyboardType.Number
        )

        FloatTextFieldWithState(
            value = userProfileState.lowerBoundGlucoseLevel,
            label = "Minimum Glucose Level",
            keyboardType = KeyboardType.Number
        )
    }
}

@Composable
fun FloatTextFieldWithState(
    value: MutableState<Float>,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var inputValue by remember { mutableStateOf(value.value.toString()) }

    OutlinedTextField(
        value = inputValue,
        onValueChange = {
            inputValue = it
            value.value = it.toFloatOrNull() ?: 0.0f
        },
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType).copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
    )
}

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

@Preview(showBackground = true)
@Composable
fun PreviewWelcomeScreen() {
    WelcomeScreen(onCompleted = { /* do nothing in preview */ })
}