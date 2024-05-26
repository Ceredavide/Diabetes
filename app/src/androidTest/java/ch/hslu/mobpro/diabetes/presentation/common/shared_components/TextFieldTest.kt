package ch.hslu.mobpro.diabetes.presentation.common.shared_components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import org.junit.Rule
import org.junit.Test

class TextFieldTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun textField_displayLabelAndError() {
        val label = "Test Label"
        val error = "Test Error"

        composeTestRule.setContent {
            TextField(
                value = "",
                onValueChange = {},
                label = label,
                error = error
            )
        }

        composeTestRule.onNodeWithText(label).assertIsDisplayed()

        composeTestRule.onNodeWithText(error).assertIsDisplayed()
    }

    @Test
    fun textField_updatesValue() {
        val label = "Test Label"
        val newValue = "New Value"

        var textFieldValue by mutableStateOf("")

        composeTestRule.setContent {
            TextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                label = label
            )
        }

        composeTestRule.onNodeWithText(label).performTextInput(newValue)

        composeTestRule.onNodeWithText(newValue).assertIsDisplayed()
    }
}