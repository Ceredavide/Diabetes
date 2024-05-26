package ch.hslu.mobpro.diabetes.presentation.common.shared_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.presentation.navigation.Routes

@Composable
fun ActiveUserIndicator(navController: NavController) {

    val userName = MainActivity.activeUserInfo.value?.name?.value ?: return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = Color(0xFFF0F0F0),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FloatingActionButton(
            onClick = { navController.navigate(Routes.home) },
            modifier = Modifier
                .size(56.dp)
                .padding(end = 16.dp),
            backgroundColor = MaterialTheme.colors.primary,
            shape = CircleShape
        ) {
            Text(
                text = userName[0].uppercaseChar().toString(),
                style = TextStyle(fontSize = 24.sp, color = Color.White)
            )
        }

        Text(
            text = userName,
            style = TextStyle(fontSize = 24.sp, color = Color.Black)
        )
    }
}
