package ch.hslu.mobpro.diabetes.presentation.common.shared_components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun Header(title : String) {
    TopAppBar(
        title = { Text(text = title, fontSize = 25.sp) },
        backgroundColor = MaterialTheme.colors.primary
    )
}