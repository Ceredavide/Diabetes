package ch.hslu.mobpro.diabetes.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.database.Product
import ch.hslu.mobpro.diabetes.ui.components.IngredientListItem
import ch.hslu.mobpro.diabetes.ui.components.RoundButton
import ch.hslu.mobpro.diabetes.ui.math.Ingredient
import ch.hslu.mobpro.diabetes.ui.navigation.Routes
import ch.hslu.mobpro.diabetes.ui.viewmodels.IngredientViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ComposeMeal(navController: NavController) {

    val ingredientViewModel: IngredientViewModel = viewModel()
    val ingredientsState = ingredientViewModel.ingredients

    Column {

        Row {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
            ) {
                Text(
                    text = "CALCULATE",
                    color = Color.White
                )
            }

            RoundButton(
                onClick = { navController.navigate(Routes.searchLocal + "/${false}") },
                text = "+"
            ) {}
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))

        LazyColumn {
            items(ingredientsState.size) {index ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    IngredientListItem(ingredient = ingredientsState[index], ingredientViewModel)
                    Spacer(modifier = Modifier.padding(top = 60.dp))
                }
            }
        }
    }
}

fun validateIngredients(ingredientsState: SnapshotStateList<Ingredient>): Boolean {

    ingredientsState.filter {
        it.weightAmount == null || it.weightAmount == 0.0f
    }

    TODO()
}

