package ch.hslu.mobpro.diabetes.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.material.Text

@Composable
fun ProfileScreen() {
    // Observing the LiveData from the ViewModel
    val text = "This is the Profile Screen"

    // Displaying the text in a Text composable
    Text(text = text)
}
