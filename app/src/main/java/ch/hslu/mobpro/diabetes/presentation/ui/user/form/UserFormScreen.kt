package ch.hslu.mobpro.diabetes.presentation.ui.user.form

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
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

    Column {
        UserForm(viewModel = userFormScreenViewModel.userFormViewModel)
        Button(onClick = { userFormScreenViewModel.validateAndSaveUser(context, navController) }) {
            Text(text = if (userFormScreenViewModel.isEditMode) "Edit" else "Add")
        }
    }

}
