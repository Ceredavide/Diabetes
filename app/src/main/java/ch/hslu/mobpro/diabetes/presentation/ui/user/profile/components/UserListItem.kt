package ch.hslu.mobpro.diabetes.presentation.ui.user.profile.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import ch.hslu.mobpro.diabetes.presentation.common.shared_components.EditDeleteButtons
import ch.hslu.mobpro.diabetes.presentation.navigation.Routes
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.GlucoseReadingsViewModel

@Composable
fun UserListItem(
    navController: NavController,
    glucoseReadingsViewModel: GlucoseReadingsViewModel,
    userName: String,
    userIndex: UInt,
    deletable: Boolean,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = userName,
                style = TextStyle(fontSize = 24.sp, color = MaterialTheme.colors.onSurface)
            )

            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary)
                    .padding(8.dp),
                onClick = {
                    PreferenceManager.instance.setActiveUserIndex(userIndex, context)
                    glucoseReadingsViewModel.updateActiveUser()
                    navController.navigate(Routes.home)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.SwitchAccount,
                    contentDescription = "Switch user",
                    tint = Color.White
                )
            }

            EditDeleteButtons(
                modifier = Modifier.padding(start = 8.dp),
                onEdit = { navController.navigate(Routes.userForm(userName)) },
                onDelete = {
                    PreferenceManager.instance.deleteUser(userName = userName, context = context)
                    navController.navigate(Routes.composeMeal)
                },
                deletable = deletable
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserListItemPreview() {
    val navController = rememberNavController()
    val glucoseReadingsViewModel: GlucoseReadingsViewModel = viewModel()
    UserListItem(
        navController = navController,
        glucoseReadingsViewModel = glucoseReadingsViewModel,
        userName = "User",
        userIndex = 1u,
        deletable = true,
        LocalContext.current
    )
}
