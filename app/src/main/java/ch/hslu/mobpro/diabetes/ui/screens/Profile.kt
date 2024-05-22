package ch.hslu.mobpro.diabetes.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.material.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import ch.hslu.mobpro.diabetes.ui.components.ActiveUserIndicator
import ch.hslu.mobpro.diabetes.ui.components.UserListItem
import ch.hslu.mobpro.diabetes.ui.navigation.Routes

@Composable
fun ProfileScreen(navController: NavController,  context: Context) {

    val users = remember { PreferenceManager.instance.getAllUserInfo(context) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        ActiveUserIndicator(navController = navController)

        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
        ) {

            items(users.size) { index ->
                UserListItem(
                    navController = navController,
                    userName = users[index].name.value,
                    userIndex = index.toUInt(),
                    deletable = users.size > 1,
                    context
                )
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(Routes.addUser) }
        ) {
            Text(text = "+", style = TextStyle(fontSize = 48.sp))

        }
    }
}


@Preview
@Composable
fun ProfileScreenPreview() {

    val navController = rememberNavController()
    ProfileScreen(navController, LocalContext.current)
}