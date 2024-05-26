package ch.hslu.mobpro.diabetes.presentation.common.shared_components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.UserFormViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserFormTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Composable
    fun getStringResource(id: Int): String {
        return stringResource(id = id)
    }

    @Test
    fun userForm_updatesUserName() {
        val viewModel = UserFormViewModel()
        composeTestRule.setContent {
            UserForm(viewModel = viewModel)
        }

        val newName = "Test User"
        composeTestRule.onNodeWithText("User Name").performTextInput(newName)

        assert(viewModel.userProfileState.value.name.value == newName)
    }

    @Test
    fun userForm_updatesInsulinPer10gCarbs() {
        val viewModel = UserFormViewModel()
        composeTestRule.setContent {
            UserForm(viewModel = viewModel)
        }

        val newValue = "1.5"
        composeTestRule.onNodeWithText("Carbohydrates per 100g/ml").performTextInput(newValue)

        assert(viewModel.userProfileState.value.insulinPer10gCarbs.value == newValue.toFloat())
    }

    @Test
    fun userForm_updatesInsulinPer1mmolL() {
        val viewModel = UserFormViewModel()
        composeTestRule.setContent {
            UserForm(viewModel = viewModel)
        }

        val newValue = "2.0"
        composeTestRule.onNodeWithText("Insulin per 1mmol_l glucose").performTextInput(newValue)

        assert(viewModel.userProfileState.value.insulinPer1mmolL.value == newValue.toFloat())
    }

    @Test
    fun userForm_updatesUpperBoundGlucoseLevel() {
        val viewModel = UserFormViewModel()
        composeTestRule.setContent {
            UserForm(viewModel = viewModel)
        }

        val newValue = "7.5"
        composeTestRule.onNodeWithText("Maximum Glucose Level").performTextClearance()
        composeTestRule.onNodeWithText("Maximum Glucose Level").performTextInput(newValue)

        assert(viewModel.userProfileState.value.upperBoundGlucoseLevel.value == newValue.toFloat())
    }

    @Test
    fun userForm_updatesLowerBoundGlucoseLevel() {
        val viewModel = UserFormViewModel()
        composeTestRule.setContent {
            UserForm(viewModel = viewModel)
        }

        val newValue = "4.5"
        composeTestRule.onNodeWithText("Minimum Glucose Level").performTextClearance()
        composeTestRule.onNodeWithText("Minimum Glucose Level").performTextInput(newValue)

        assert(viewModel.userProfileState.value.lowerBoundGlucoseLevel.value == (newValue.toFloat()))
    }
}