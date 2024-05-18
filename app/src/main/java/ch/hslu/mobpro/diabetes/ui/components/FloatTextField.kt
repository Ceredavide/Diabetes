package ch.hslu.mobpro.diabetes.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp

@Composable
fun FloatTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.isValidNumericInput()) {
                onValueChange(newValue)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = VisualTransformation.None,
        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black), // Adjust as needed
        modifier = modifier.fillMaxWidth()
    )
}

// Extension function to check if a string is a valid numeric input
fun String.isValidNumericInput(): Boolean {
    return matches("-?\\d*\\.?\\d*".toRegex())
}