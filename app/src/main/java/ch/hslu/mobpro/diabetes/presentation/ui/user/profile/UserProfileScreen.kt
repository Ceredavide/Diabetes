package ch.hslu.mobpro.diabetes.presentation.ui.user.profile

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import ch.hslu.mobpro.diabetes.presentation.common.shared_components.ActiveUserIndicator
import ch.hslu.mobpro.diabetes.presentation.ui.user.profile.components.UserListItem
import ch.hslu.mobpro.diabetes.presentation.navigation.Routes
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.GlucoseReadingsViewModel
import ch.hslu.mobpro.diabetes.presentation.ui.user.profile.components.CurrentUserInfo

@Composable
fun ProfileScreen(
    navController: NavController,
    glucoseReadingsViewModel: GlucoseReadingsViewModel,
    context: Context
) {

    val users = remember { PreferenceManager.instance.getAllUserInfo(context) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
    ) {

        val user = MainActivity.activeUserInfo.value!!

        CurrentUserInfo(user)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        )
        {
            Text(
                text = "Registered Users: ${users.size}",
                style = TextStyle(fontSize = 24.sp),
                modifier = Modifier.padding(16.dp)
            )
            FloatingActionButton(
                onClick = { navController.navigate(Routes.userFormCreate) }
            ) {
                Text(text = "+", style = TextStyle(fontSize = 48.sp))

            }
        }
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
        ) {

            items(users.size) { index ->

                UserListItem(
                    navController = navController,
                    glucoseReadingsViewModel = glucoseReadingsViewModel,
                    userName = users[index].name.value,
                    userIndex = index.toUInt(),
                    deletable = users.size > 1,
                    context
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {

    val navController = rememberNavController()
    val glucoseReadingsViewModel: GlucoseReadingsViewModel = viewModel()
    ProfileScreen(navController, glucoseReadingsViewModel, LocalContext.current)
}