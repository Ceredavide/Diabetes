package ch.hslu.mobpro.diabetes.presentation.common.shared_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        FloatingActionButton(
                onClick = { navController.navigate(Routes.home)},
                modifier = Modifier
                    .wrapContentSize()
                    .background(Color(red = 0, green = 205, blue = 255)),
                shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                    text = userName[0].uppercaseChar().toString(),
                    style = TextStyle(fontSize = 48.sp)
            )
        }

        Text(
                text = userName,
                style = TextStyle(fontSize = 48.sp)
        )

    }
}
