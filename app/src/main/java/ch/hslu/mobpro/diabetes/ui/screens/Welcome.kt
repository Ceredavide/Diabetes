package ch.hslu.mobpro.diabetes.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.material.Text

@Composable
fun WelcomeScreen(onCompleted: () -> Unit) {
    // Observing the LiveData from the ViewModel
    val text = "This is the Welcome Screen"

    // Displaying the text in a Text composable
    Text(text = text)
}
