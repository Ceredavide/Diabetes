package ch.hslu.mobpro.diabetes.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import ch.hslu.mobpro.diabetes.ui.navigation.Routes
import ch.hslu.mobpro.diabetes.ui.viewmodels.GlucoseReadingsViewModel

@Composable
fun UserListItem(navController: NavController,
                glucoseReadingsViewModel: GlucoseReadingsViewModel,
                 userName: String,
                 userIndex: UInt,
                 deletable: Boolean,
                 context: Context) {

    val modifier = Modifier.padding(16.dp)
    Column(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, MaterialTheme.colors.primary, RoundedCornerShape(2.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = userName,
                Modifier.padding(start = 16.dp),
                style = TextStyle(fontSize = 32.sp)
            )

            IconButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.LightGray)
                        .padding(4.dp),
                    onClick = {

                        PreferenceManager.instance.setActiveUserIndex(userIndex, context)
                        glucoseReadingsViewModel.updateActiveUser()
                        navController.navigate(Routes.home)
                    }
            ) {
                Icon(imageVector = Icons.Default.SwitchAccount, contentDescription = "Switch user")
            }

            EditDeleteButtons(
                modifier = modifier,
                onEdit = { navController.navigate(Routes.editUser + "/$userName") },
                onDelete = {

                    PreferenceManager.instance.deleteUser(userName = userName, context = context)
                    navController.navigate(Routes.notifications)
                           },
                deletable = deletable
            )
        }
    }
}

@Preview
@Composable
fun UserListItemPreview() {

    val navController = rememberNavController()
    val glucoseReadingsViewModel: GlucoseReadingsViewModel = viewModel()

    UserListItem(navController = navController,
            glucoseReadingsViewModel = glucoseReadingsViewModel,
            userName = "User",
            userIndex = 1u,
            deletable = true,
            LocalContext.current)
}