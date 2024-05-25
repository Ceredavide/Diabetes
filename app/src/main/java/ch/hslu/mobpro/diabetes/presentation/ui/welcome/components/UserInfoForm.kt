package ch.hslu.mobpro.diabetes.presentation.ui.welcome.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.presentation.ui.welcome.UserPreferences

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
