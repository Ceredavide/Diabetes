package ch.hslu.mobpro.diabetes.ui.screens.editing

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import ch.hslu.mobpro.diabetes.ui.components.FloatTextField
import ch.hslu.mobpro.diabetes.ui.navigation.Routes
import ch.hslu.mobpro.diabetes.ui.screens.welcome.UserPreferences

@Composable
fun EditUser(navController: NavController, user: UserPreferences, context: Context) {

    val originalUserName = user.name.value
    var userName by remember { mutableStateOf(TextFieldValue(originalUserName)) }
    var insulinPer10gCarbsString by remember { mutableStateOf(user.insulinPer10gCarbs.value.toString()) }
    var insulinPer1mmol_LString by remember { mutableStateOf(user.insulinPer1mmolL.value.toString()) }
    var lowerBoundGlucoseLevelString by remember { mutableStateOf(user.lowerBoundGlucoseLevel.value.toString()) }
    var upperBoundGlucoseLevelString by remember { mutableStateOf(user.upperBoundGlucoseLevel.value.toString()) }
    var color by remember { mutableStateOf(Color.LightGray) }
    var changeDetected by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {

        val keyboardController = LocalSoftwareKeyboardController.current
        OutlinedTextField(
            value = userName,
            onValueChange = {
                changeDetected = hasChanged(originalUserName, it.text)
                userName = it
            },
            label = { Text(text = "Name") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(16.dp))

        FloatTextField(
            value = insulinPer10gCarbsString,
            onValueChange = {
                changeDetected = hasChanged(user.insulinPer10gCarbs.value.toString(), it);
                insulinPer10gCarbsString = it
            },
            label = stringResource(id = R.string.insulin_per_10g)
        )

        Spacer(modifier = Modifier.padding(16.dp))

        FloatTextField(
            value = insulinPer1mmol_LString,
            onValueChange = {
                changeDetected = hasChanged(user.insulinPer1mmolL.value.toString(), it);
                insulinPer1mmol_LString = it
            },
            label = stringResource(id = R.string.insulin_per_1mmol_l)
        )

        Spacer(modifier = Modifier.padding(16.dp))

        FloatTextField(
            value = lowerBoundGlucoseLevelString,
            onValueChange = {
                changeDetected = hasChanged(user.lowerBoundGlucoseLevel.value.toString(), it);
                lowerBoundGlucoseLevelString = it
            },
            label = stringResource(id = R.string.lower_bounds_glucose_level)
        )

        Spacer(modifier = Modifier.padding(16.dp))

        FloatTextField(
            value = upperBoundGlucoseLevelString,
            onValueChange = {
                changeDetected = hasChanged(user.upperBoundGlucoseLevel.value.toString(), it);
                upperBoundGlucoseLevelString = it
            },
            label = stringResource(id = R.string.upper_bounds_glucose_level)
        )

        if (changeDetected) {

            color = Color.Green
        } else {

            color = Color.LightGray
        }
        Column {

            IconButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(color),
                onClick = {

                    val loweBoundGlucoseLevel =
                        if (lowerBoundGlucoseLevelString.toFloatOrNull() == null) 4.0f else lowerBoundGlucoseLevelString.toFloat()
                    val upperBoundGlucoseLevel =
                        if (upperBoundGlucoseLevelString.toFloatOrNull() == null) 4.0f else upperBoundGlucoseLevelString.toFloat()
                    if (changeDetected && onSave(
                            userName = userName.text,
                            insulinPer10gCarbs = insulinPer10gCarbsString.toFloatOrNull(),
                            insulinPer1mmol_L = insulinPer1mmol_LString.toFloatOrNull(),
                            loweBoundGlucoseLevel = loweBoundGlucoseLevel,
                            upperBoundGlucoseLevel = upperBoundGlucoseLevel,
                            context = context
                        )
                    ) {

                        val userInfo = UserPreferences(
                            name = mutableStateOf(userName.text),
                            insulinPer10gCarbs = mutableStateOf(insulinPer10gCarbsString.toFloat()),
                            insulinPer1mmolL = mutableStateOf(insulinPer1mmol_LString.toFloat()),
                            lowerBoundGlucoseLevel = mutableStateOf(loweBoundGlucoseLevel),
                            upperBoundGlucoseLevel = mutableStateOf(upperBoundGlucoseLevel)
                        )

                        PreferenceManager.instance.editUser(userInfo, context)
                        Toast.makeText(context, "Saved changes", Toast.LENGTH_LONG).show()
                        navController.navigate(Routes.notifications)
                    } else {

                        Toast.makeText(context, "Failed to save changes", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            ) {
                androidx.compose.material.Text(
                    text = "SAVE",
                    modifier = Modifier
                        .padding(top = 50.dp)
                )
                Icon(
                    Icons.Default.Save,
                    contentDescription = "Save",
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

        }
    }
}

private fun hasChanged(originalValue: String, newValue: String): Boolean {

    return !originalValue.equals(newValue)
}

private fun onSave(
    userName: String,
    insulinPer10gCarbs: Float?,
    insulinPer1mmol_L: Float?,
    loweBoundGlucoseLevel: Float,
    upperBoundGlucoseLevel: Float,
    context: Context
): Boolean {
    if (validate(
            userName = userName,
            insulinPer10gCarbs = insulinPer10gCarbs,
            insulinPer1mmol_L = insulinPer1mmol_L,
            loweBoundGlucoseLevel = loweBoundGlucoseLevel,
            upperBoundGlucoseLevel = upperBoundGlucoseLevel
        )
    ) {

        val userPreferences = UserPreferences(
            name = mutableStateOf(userName),
            insulinPer10gCarbs = mutableStateOf(insulinPer10gCarbs!!),
            insulinPer1mmolL = mutableStateOf(insulinPer1mmol_L!!),
            lowerBoundGlucoseLevel = mutableStateOf(loweBoundGlucoseLevel),
            upperBoundGlucoseLevel = mutableStateOf(upperBoundGlucoseLevel)
        )

        PreferenceManager.instance.setUserinfo(userPreferences, context)

        return true;
    }

    return false
}

private fun validate(
    userName: String,
    insulinPer10gCarbs: Float?,
    insulinPer1mmol_L: Float?,
    loweBoundGlucoseLevel: Float,
    upperBoundGlucoseLevel: Float
): Boolean {
    if (userName.isEmpty() ||
        insulinPer10gCarbs == null || insulinPer10gCarbs == 0.0f ||
        insulinPer1mmol_L == null || insulinPer1mmol_L == 0.0f ||
        loweBoundGlucoseLevel == 0.0f ||
        upperBoundGlucoseLevel == 0.0f
    ) {

        return false
    }


    return true
}

@Preview
@Composable
fun EditUserPreview() {

    val navController = rememberNavController()

    val name = remember { mutableStateOf("User") }
    val insulinPer10gCarbs = remember { mutableStateOf(1.2f) }
    val inslinePer1mmol_L = remember { mutableStateOf(0.5f) }
    val lowerBoundGlucoseLevel = remember { mutableStateOf(4.0f) }
    val upperBoundGlucoseLevel = remember { mutableStateOf(8.0f) }

    val user = UserPreferences(
        name = name,
        insulinPer10gCarbs = insulinPer10gCarbs,
        insulinPer1mmolL = inslinePer1mmol_L,
        lowerBoundGlucoseLevel = lowerBoundGlucoseLevel,
        upperBoundGlucoseLevel = upperBoundGlucoseLevel
    )

    EditUser(navController, user, LocalContext.current)
}