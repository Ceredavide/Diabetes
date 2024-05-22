package ch.hslu.mobpro.diabetes.ui.screens.adding

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
fun AddUser(navController: NavController, context: Context) {

    var userNameInput by remember { mutableStateOf(TextFieldValue("")) }
    var insulinPer10gCarbsInput by remember { mutableStateOf("") }
    var insulinPer1mmol_LInput by remember { mutableStateOf("") }
    var lowerBoundGlucoseLevelInput by remember { mutableStateOf("4.0") }
    var upperBoundGlucoseLevelInput by remember { mutableStateOf("8.0") }
    var color by remember { mutableStateOf(Color.LightGray) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        OutlinedTextField(
            value = userNameInput,
            onValueChange = { userNameInput = it },
            label = { Text(stringResource(id = R.string.user_name)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(16.dp))

        FloatTextField(
            value = insulinPer10gCarbsInput,
            onValueChange = { insulinPer10gCarbsInput = it },
            label = stringResource(id = R.string.insulin_per_10g)
        )

        Spacer(modifier = Modifier.padding(16.dp))

        FloatTextField(
            value = insulinPer1mmol_LInput,
            onValueChange = { insulinPer1mmol_LInput = it },
            label = stringResource(id = R.string.insulin_per_1mmol_l)
        )

        Spacer(modifier = Modifier.padding(16.dp))

        FloatTextField(
            value = lowerBoundGlucoseLevelInput,
            onValueChange = { lowerBoundGlucoseLevelInput = it },
            label = stringResource(id = R.string.lower_bounds_glucose_level)
        )

        Spacer(modifier = Modifier.padding(16.dp))

        FloatTextField(
            value = upperBoundGlucoseLevelInput,
            onValueChange = { upperBoundGlucoseLevelInput = it },
            label = stringResource(id = R.string.upper_bounds_glucose_level)
        )

        Spacer(modifier = Modifier.padding(16.dp))

        if (validate(userName = userNameInput.toString(),
                    insulinPer10gCarbs = insulinPer10gCarbsInput.toFloatOrNull(),
                    insulinPer1mmol_L = insulinPer1mmol_LInput.toFloatOrNull()
            )) {

            color = Color.Green
        }
        else {

            color = Color.LightGray
        }

        IconButton(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(color),
            onClick = {

                if (onSave(
                        userName = userNameInput.toString(),
                        insulinPer10gCarbs = insulinPer10gCarbsInput.toFloatOrNull(),
                        insulinPer1mmol_L = insulinPer1mmol_LInput.toFloatOrNull())
                ) {


                    val userInfo = UserPreferences(
                        name = mutableStateOf(userNameInput.text),
                        insulinPer10gCarbs = mutableStateOf(insulinPer10gCarbsInput.toFloat()),
                        inslinePer1mmol_L = mutableStateOf(insulinPer1mmol_LInput.toFloat()),
                        lowerBoundGlucoseLevel = mutableStateOf(lowerBoundGlucoseLevelInput.toFloat()),
                        upperBoundGlucoseLevel = mutableStateOf(upperBoundGlucoseLevelInput.toFloat())
                    )

                    if (PreferenceManager.instance.addUser(userInfo, context)) {

                        Toast.makeText(context, "Saved user ${userNameInput}", Toast.LENGTH_LONG).show()
                        navController.navigate(Routes.notifications)
                    }
                    else {

                        Toast.makeText(context, "A user with the name ${userNameInput} already exists!", Toast.LENGTH_LONG).show()
                    }
                } else {

                    Toast.makeText(context, "Please make sure you entered values for all mandatory field", Toast.LENGTH_LONG)
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

private fun onSave(userName: String,
                   insulinPer10gCarbs: Float?,
                   insulinPer1mmol_L: Float?): Boolean {

    if (validate(userName = userName,
                insulinPer10gCarbs = insulinPer10gCarbs,
                insulinPer1mmol_L = insulinPer1mmol_L)){

        return true
    }

    return false
}

private fun validate(userName: String,
                     insulinPer10gCarbs: Float?,
                     insulinPer1mmol_L: Float?): Boolean {

    return userName.isNotEmpty() &&
            insulinPer10gCarbs != null && insulinPer10gCarbs > 0.0f &&
            insulinPer1mmol_L != null && insulinPer1mmol_L > 0.0f
}

@Preview
@Composable
fun AddUserPreview() {

    val navController = rememberNavController()

    AddUser(navController, LocalContext.current)
}