package ch.hslu.mobpro.diabetes.presentation.ui.welcome.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.hslu.mobpro.diabetes.presentation.theme.Typography

@Composable
fun StepTitle(title: String) {
    Text(
        title,
        style = Typography.h4,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun StepContent(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun StepperButtons(onBack: () -> Unit, onNext: () -> Unit, isFirstStep: Boolean, isLastStep: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = if (isFirstStep) Arrangement.Center else Arrangement.SpaceBetween
    ) {
        if (!isFirstStep) {
            Button(
                onClick = onBack,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go Back"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Go Back")
            }
        }
        Button(
            onClick = onNext,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(if (!isLastStep) "Next" else "Completa")
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next"
            )
        }
    }
}
