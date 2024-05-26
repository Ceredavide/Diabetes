package ch.hslu.mobpro.diabetes.presentation.ui.user.profile

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import ch.hslu.mobpro.diabetes.domain.model.User
import ch.hslu.mobpro.diabetes.presentation.common.shared_components.Header
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

    Scaffold(
        topBar = { Header(Routes.profile) },
        content = { paddingValues ->
            ProfileContent(
                navController = navController,
                glucoseReadingsViewModel = glucoseReadingsViewModel,
                users = users,
                paddingValues = paddingValues,
                context = context
            )
        }
    )
}

@Composable
fun ProfileContent(
    navController: NavController,
    glucoseReadingsViewModel: GlucoseReadingsViewModel,
    users: List<User>,
    paddingValues: PaddingValues,
    context: Context
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
            .fillMaxWidth(),
    ) {

        val user = PreferenceManager.instance.getActiveUserInfo(context)

        CurrentUserInfo(user.value!!)

        Spacer(modifier = Modifier.height(16.dp))

        RegisteredUsersHeader(navController = navController, userCount = users.size)

        Spacer(modifier = Modifier.height(16.dp))

        UserList(navController = navController, glucoseReadingsViewModel = glucoseReadingsViewModel, users = users, context = context)
    }
}

@Composable
fun RegisteredUsersHeader(navController: NavController, userCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Registered Users: $userCount",
            style = TextStyle(fontSize = 24.sp),
            modifier = Modifier.padding(16.dp)
        )
        FloatingActionButton(
            onClick = { navController.navigate(Routes.userFormCreate) }
        ) {
            Text(text = "+", style = TextStyle(fontSize = 48.sp))
        }
    }
}

@Composable
fun UserList(
    navController: NavController,
    glucoseReadingsViewModel: GlucoseReadingsViewModel,
    users: List<User>,
    context: Context
) {
    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        items(users.size) { index ->
            UserListItem(
                navController = navController,
                glucoseReadingsViewModel = glucoseReadingsViewModel,
                userName = users[index].name.value,
                userIndex = index.toUInt(),
                deletable = users.size > 1,
                context = context
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    val glucoseReadingsViewModel: GlucoseReadingsViewModel = viewModel()
    ProfileScreen(navController, glucoseReadingsViewModel, LocalContext.current)
}
