package ch.hslu.mobpro.diabetes.presentation.ui.user.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ch.hslu.mobpro.diabetes.domain.model.User
import ch.hslu.mobpro.diabetes.presentation.common.shared_components.UserForm

@Composable
fun UserFormScreen(
    navController: NavController,
    user: User?,
) {
    val context = LocalContext.current
    val userFormScreenViewModel = UserFormScreenViewModel(context, user)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text(text = "Back")
            }
            Text(
                text = if (userFormScreenViewModel.isEditMode) "Edit User" else "Add User",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        UserForm(
            viewModel = userFormScreenViewModel.userFormViewModel,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = Color.Gray, thickness = 1.dp)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { userFormScreenViewModel.validateAndSaveUser(context, navController) },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 16.dp)
        ) {
            Text(text = if (userFormScreenViewModel.isEditMode) "Edit" else "Add")
        }
    }
}
