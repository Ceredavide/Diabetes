package ch.hslu.mobpro.diabetes.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun ComposeMeal(navController: NavController,
                ingredientViewModel: IngredientViewModel,
                productName: String?) {

    val ingredientsState = ingredientViewModel.ingredients

    Column {

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

        Spacer(modifier = Modifier.padding(top = 16.dp))

        RoundButton(
            onClick = { navController.navigate(Routes.searchLocal + "/${Routes.composeMeal}") },
            text = "+"
        ) {}
    }
}

