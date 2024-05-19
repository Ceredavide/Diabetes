package ch.hslu.mobpro.diabetes.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.ui.components.IngredientListItem
import ch.hslu.mobpro.diabetes.ui.components.RoundButton
import ch.hslu.mobpro.diabetes.ui.math.Ingredient
import ch.hslu.mobpro.diabetes.ui.navigation.Routes
import ch.hslu.mobpro.diabetes.ui.viewmodels.IngredientViewModel

@Composable
fun ComposeMeal(navController: NavController, ingredientViewModel: IngredientViewModel) {

    val ingredients = ingredientViewModel.ingredients
    var check by remember { mutableStateOf(false) }
    val comtext = LocalContext.current

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    if (validateIngredients(ingredients)) {

                        navController.navigate(Routes.resultScreen)
                    }
                    else if (ingredients.isEmpty()) {

                        Toast.makeText(comtext, "PLEASE ADD PRODUCTS TO THIS MEAL", Toast.LENGTH_LONG).show()
                    }
                    else {

                        Toast.makeText(comtext, "PLEASE ENTER MISSING VALUES", Toast.LENGTH_LONG).show()
                        check = true
                    }
                          },
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
            ) {
                Text(text = "CALCULATE", color = Color.White)
            }

            Button(
                onClick = { ingredientViewModel.clearIngredients() }
            ) {
                Text(text = "CLEAR", color = Color.White)
            }

            RoundButton(
                onClick = { navController.navigate(Routes.searchLocal + "/${false}") },
                text = "+"
            ) {}
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))

        LazyColumn {
            items(ingredients.size) { index ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    IngredientListItem(ingredient = ingredients[index],
                        ingredientViewModel = ingredientViewModel,
                        check = check
                        )
                    Spacer(modifier = Modifier.padding(top = 60.dp))
                }
            }
        }
    }
}

fun validateIngredients(ingredientsState: SnapshotStateList<Ingredient>): Boolean {

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

    ComposeMeal(navController = navController, ingredientViewModel = ingredientViewModel)
}