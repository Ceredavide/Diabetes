package ch.hslu.mobpro.diabetes.presentation.ui.user.profile.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.hslu.mobpro.diabetes.domain.model.User

@Composable
fun CurrentUserInfo(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text("User Information", fontSize = 20.sp, modifier = Modifier.padding(bottom = 8.dp))

            Text("Name: ${user.name.value}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Insulin per 10g Carbs: ${user.insulinPer10gCarbs.value}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Insulin per 1mmol/L: ${user.insulinPer1mmolL.value}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Upper Bound Glucose Level: ${user.upperBoundGlucoseLevel.value}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Lower Bound Glucose Level: ${user.lowerBoundGlucoseLevel.value}", fontSize = 16.sp)
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun PreviewUserCard() {
    val user = User(
        name = mutableStateOf("John Doe"),
        insulinPer10gCarbs = mutableFloatStateOf(1.5f),
        insulinPer1mmolL = mutableFloatStateOf(0.7f),
        upperBoundGlucoseLevel = mutableFloatStateOf(8.0f),
        lowerBoundGlucoseLevel = mutableFloatStateOf(4.0f)
    )
    CurrentUserInfo(user)
}
