package ch.hslu.mobpro.diabetes.presentation.common.shared_components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.UserFormViewModel

@Composable
fun UserForm(
    viewModel: UserFormViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier)
    {
        TextField(
            value = viewModel.userProfileState.value.name.value,
            onValueChange = {
                viewModel.updateUserName(it)
            },
            label = stringResource(id = R.string.user_name),
            error = viewModel.nameError.value
        )

        Spacer(modifier = Modifier.height(18.dp))

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

        Spacer(modifier = Modifier.height(18.dp))

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

        Spacer(modifier = Modifier.height(18.dp))

        TextField(
            value = viewModel.userProfileState.value.upperBoundGlucoseLevel.value.toString(),
            onValueChange = {
                val newValue = it.toFloatOrNull() ?: 0.0f
                viewModel.updateUpperBoundGlucoseLevel(newValue)
            },
            label = stringResource(id = R.string.upper_bounds_glucose_level),
            keyboardType = KeyboardType.Number,
            error = viewModel.upperBoundGlucoseLevelError.value
        )

        Spacer(modifier = Modifier.height(18.dp))

        TextField(
            value = viewModel.userProfileState.value.lowerBoundGlucoseLevel.value.toString(),
            onValueChange = {
                val newValue = it.toFloatOrNull() ?: 0.0f
                viewModel.updateLowerBoundGlucoseLevel(newValue)
            },
            label =stringResource(id = R.string.lower_bounds_glucose_level),
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
