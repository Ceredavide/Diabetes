package ch.hslu.mobpro.diabetes.presentation.common.shared_components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.UserFormViewModel

@Composable
fun UserForm(
    viewModel: UserFormViewModel,
) {
    Column {
        TextField(
            value = viewModel.userProfileState.value.name.value,
            onValueChange = {
                viewModel.updateUserName(it)
            },
            label = "User Name",
            error = viewModel.nameError.value
        )

        TextField(
            value = viewModel.userProfileState.value.insulinPer10gCarbs.value.toString(),
            onValueChange = {
                val newValue = it.toFloatOrNull() ?: 0.0f
                viewModel.updateInsulinPer10gCarbs(newValue)
            },
            label = stringResource(id = R.string.carbs_per_100g),
            keyboardType = KeyboardType.Number,
            error = viewModel.insulinPer10gCarbsError.value
        )

        TextField(
            value = viewModel.userProfileState.value.insulinPer1mmolL.value.toString(),
            onValueChange = {
                val newValue = it.toFloatOrNull() ?: 0.0f
                viewModel.updateInsulinPer1mmolL(newValue)
            },
            label = stringResource(id = R.string.insulin_per_1mmol_l),
            keyboardType = KeyboardType.Number,
            error = viewModel.insulinPer1mmolLError.value
        )

        TextField(
            value = viewModel.userProfileState.value.upperBoundGlucoseLevel.value.toString(),
            onValueChange = {
                val newValue = it.toFloatOrNull() ?: 0.0f
                viewModel.updateUpperBoundGlucoseLevel(newValue)
            },
            label = "Maximum Glucose Level",
            keyboardType = KeyboardType.Number,
            error = viewModel.upperBoundGlucoseLevelError.value
        )

        TextField(
            value = viewModel.userProfileState.value.lowerBoundGlucoseLevel.value.toString(),
            onValueChange = {
                val newValue = it.toFloatOrNull() ?: 0.0f
                viewModel.updateLowerBoundGlucoseLevel(newValue)
            },
            label = "Minimum Glucose Level",
            keyboardType = KeyboardType.Number,
            error = viewModel.lowerBoundGlucoseLevelError.value
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewUserInfoForm() {
    val viewModel = UserFormViewModel()
    UserForm(viewModel)
}