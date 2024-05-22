package ch.hslu.mobpro.diabetes.ui.screens.adding

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.data.database.GlucoseReading
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import ch.hslu.mobpro.diabetes.ui.components.IngredientListItem
import ch.hslu.mobpro.diabetes.math.Ingredient
import ch.hslu.mobpro.diabetes.ui.components.ActiveUserIndicator
import ch.hslu.mobpro.diabetes.ui.components.FloatTextField
import ch.hslu.mobpro.diabetes.ui.navigation.Routes
import ch.hslu.mobpro.diabetes.ui.viewmodels.IngredientViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun ComposeMeal(
        navController: NavController,
        ingredientViewModel: IngredientViewModel,
        glucoseLevel: MutableState<Float>
) {

    val ingredients = ingredientViewModel.ingredients
    var check by remember { mutableStateOf(false) }
    var glucoseLevelString by remember { mutableStateOf(glucoseLevel.value.toString()) }
    var color by remember { mutableStateOf(Color.Transparent) }

    val context = LocalContext.current

    Column(
            modifier = Modifier.padding(16.dp)
    ) {

        ActiveUserIndicator(navController = navController)

        Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                    onClick = {
                        if (validateIngredients(ingredients) && validateGlucoseLevel(
                                    glucoseLevelString.toFloatOrNull()
                            )
                        ) {

                            CoroutineScope(Dispatchers.IO).launch {

                                val currentTime = Date()
                                val glucoseReading = GlucoseReading(
                                        userIndex = PreferenceManager.instance.getActiveUserIndex()
                                            .toInt(),
                                        glucoseLevel = glucoseLevel.value,
                                        time = currentTime
                                )

                                MainActivity.glucoseReadingDao.insertGlucoseReading(glucoseReading)
                            }

                            navController.navigate(Routes.resultScreen)
                        }
                        else if (ingredients.isEmpty()) {

                            Toast.makeText(
                                    context,
                                    "PLEASE ADD PRODUCTS TO THIS MEAL",
                                    Toast.LENGTH_LONG
                            ).show()
                        }
                        else {

                            Toast.makeText(
                                    context,
                                    "PLEASE ENTER MISSING VALUES",
                                    Toast.LENGTH_LONG
                            )
                                .show()
                            check = true
                        }
                    },
                    modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(text = "CALCULATE", color = Color.White)
            }

            Button(
                    onClick = { ingredientViewModel.clearIngredients() },
                    modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(text = "CLEAR", color = Color.White)
            }

            FloatingActionButton(
                    modifier = Modifier.padding(top = 10.dp),
                    onClick = { navController.navigate(Routes.searchLocal + "/${false}") },
            ) {
                Text(text = "+", style = TextStyle(fontSize = 48.sp))
            }
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))

        FloatTextField(
                value = glucoseLevelString,
                onValueChange = {
                    glucoseLevelString = it
                    if (glucoseLevelString.isNotEmpty() && glucoseLevelString.toFloatOrNull() != null) {
                        glucoseLevel.value = glucoseLevelString.toFloat()
                    }
                },
                label = stringResource(id = R.string.glucose_level),
                modifier = Modifier
                    .background(color)
                    .fillMaxWidth()
        ).also {
            val glucoseLevel = glucoseLevelString.toFloatOrNull()
            if (glucoseLevelString.isEmpty() || glucoseLevel == null || glucoseLevel == 0.0f) {

                color = Color.Red
            }
            else {

                color = Color.Transparent
            }
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))

        LazyColumn {
            items(ingredients.size) { index ->
                Box(
                        modifier = Modifier.fillMaxWidth()
                ) {

                    IngredientListItem(
                            ingredient = ingredients[index],
                            ingredientViewModel = ingredientViewModel,
                            check = check
                    )
                    Spacer(modifier = Modifier.padding(top = 60.dp))
                }
            }
        }
    }
}

private fun validateGlucoseLevel(glucoseLevel: Float?): Boolean {

    return glucoseLevel != null && glucoseLevel > 0.0f
}

private fun validateIngredients(ingredientsState: SnapshotStateList<Ingredient>): Boolean {

    if (ingredientsState.size == 0) {

        return false
    }
    if (ingredientsState.any {
            it.weightAmount == null || it.weightAmount == 0.0f
        }) {
        return false
    }

    return true
}

@Preview
@Composable
fun ComposeMealPreview() {

    val navController = rememberNavController()
    val ingredientViewModel: IngredientViewModel = viewModel()
    val glucoseLevel = remember { mutableStateOf(0.0f) }

    ComposeMeal(
            navController = navController,
            ingredientViewModel = ingredientViewModel,
            glucoseLevel = glucoseLevel
    )
}