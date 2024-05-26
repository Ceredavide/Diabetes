package ch.hslu.mobpro.diabetes.presentation.ui.user.form

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import ch.hslu.mobpro.diabetes.domain.model.User
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.UserFormViewModel
import ch.hslu.mobpro.diabetes.presentation.navigation.Routes

class UserFormScreenViewModel(
    context: Context,
    user: User?
) : ViewModel() {

    val isEditMode = (user != null)
    val userFormViewModel = UserFormViewModel(user ?: User())

    private val preferenceManager = PreferenceManager(context)

    fun validateAndSaveUser(
        context: Context,
        navController: NavController,
        originalName: String? = null
    ) {
        if (userFormViewModel.validateAll()) {
            if (isEditMode) {
                editUser(
                    context,
                    userFormViewModel.userProfileState.value,
                    navController,
                    originalName!!
                )
            } else {
                addUser(
                    context,
                    userFormViewModel.userProfileState.value,
                    navController,
                )
            }
        }
    }

    private fun addUser(
        context: Context,
        user: User,
        navController: NavController
    ) {
        if (PreferenceManager.instance.addUser(user, context)) {
            Toast.makeText(context, "Saved user ${user.name.value}", Toast.LENGTH_LONG).show()
            navController.navigate(Routes.user)
        } else {
            Toast.makeText(
                context,
                "A user with the name ${user.name.value} already exists!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun editUser(
        context: Context,
        user: User,
        navController: NavController,
        originalName: String
    ) {
        PreferenceManager.instance.editUser(originalName,user, context)
        Toast.makeText(context, "Saved changes", Toast.LENGTH_LONG).show()
        navController.navigate(Routes.user)
    }
}