package ch.hslu.mobpro.diabetes.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import ch.hslu.mobpro.diabetes.ui.custom_components.AddButton
import ch.hslu.mobpro.diabetes.ui.custom_components.FloatTextField

@Composable
fun EnterManualScreen() {

    var text by remember { mutableStateOf(TextFieldValue("")) }
    var carbs by remember { mutableStateOf("0") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("PRODUCT NAME") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        FloatTextField(
            value = carbs,
            onValueChange = { carbs = it },
            label = "CARBOHYDRATES / 100G"
        )

        Spacer(modifier = Modifier.height(16.dp))

        AddButton(
            onClick = {
                Log.d("Mine", "ADD")
             }
        ) {
            Text(text = "+", color = Color.White) // Button content
        }
    }
}