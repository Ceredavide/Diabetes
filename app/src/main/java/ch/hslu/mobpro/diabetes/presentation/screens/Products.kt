package ch.hslu.mobpro.diabetes.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.material.Text

@Composable
fun ProductsScreen() {
    // Observing the LiveData from the ViewModel
    val text = "This is the Products Screen"

    // Displaying the text in a Text composable
    Text(text = text)
}
