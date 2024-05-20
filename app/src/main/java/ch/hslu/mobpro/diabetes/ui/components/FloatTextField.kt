package ch.hslu.mobpro.diabetes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun FloatTextField(
    modifier: Modifier = Modifier,
    value: String,
    positiveLimit: Float? = null,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.isValidNumericInput()) {
                if (newValue.startsWith('.')) {

                    onValueChange("0$newValue")
                }
                else {
                    val floatValue = newValue.toFloatOrNull()
                    if (positiveLimit != null && floatValue != null) {
                        if (floatValue > positiveLimit) {

                            return@OutlinedTextField
                        }
                    }
                    onValueChange(newValue)
                }
            }
            else if (newValue.isEmpty()) {

                onValueChange(newValue)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = VisualTransformation.None,
        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
        modifier = modifier
    )
}

fun String.isValidNumericInput(): Boolean {
    return matches("\\d*\\.?\\d*".toRegex())
}

@Preview
@Composable
fun FloatTextFieldPreview() {

    val temo = ""
    FloatTextField(
        value = temo,
        onValueChange = {},
        label = "label",
        modifier = Modifier
    )
}