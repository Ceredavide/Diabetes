package ch.hslu.mobpro.diabetes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.data.database.Product
import ch.hslu.mobpro.diabetes.ui.math.Ingredient
import ch.hslu.mobpro.diabetes.ui.math.calculateInsulinDoseAndTotalCarbs
import ch.hslu.mobpro.diabetes.ui.navigation.Routes
import ch.hslu.mobpro.diabetes.ui.viewmodels.IngredientViewModel
@Composable
fun ResultScreen(navController: NavController, ingredientViewModel: IngredientViewModel) {

    val ingredients = ingredientViewModel.ingredients
    
    Column {
        // temporary values for testing
        val glucoseLevel = 5.0f
        val lowerBoundGlucoseLevel = 4.0f
        val upperBoundGlucoseLevel = 8.0f
        val insulinPer10gCarbs = 1.2f
        val insulinPer1mmol_L = 0.5f
        val userName = "Test User"

        val (insulinDose, totalCarbs) = calculateInsulinDoseAndTotalCarbs(
            insulinPer10gCarbs = insulinPer10gCarbs,
            insulinPer1mmol_L = insulinPer1mmol_L,
            lowerBoundGlucoseLevel = lowerBoundGlucoseLevel,
            upperBoundGlucoseLevel = upperBoundGlucoseLevel,
            glucoseLevel = glucoseLevel,
            ingredients = ingredientViewModel.ingredients
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(text = "User", fontSize = 32.sp)
            Text(text = userName, fontSize = 32.sp)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(text = "Units insulin", fontSize = 32.sp)
            Text(text = "${insulinDose}", fontSize = 32.sp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(text = "Toal carbohydrates", fontSize = 32.sp)
            Text(text = "${totalCarbs}g", fontSize = 32.sp)
        }
        ingredients.map {
            
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = it.product.name!!, fontSize = 32.sp)
                Text(text = "${it.weightAmount}g", fontSize = 32.sp)
            }
        }

        Spacer(modifier = Modifier.padding(top = 60.dp))

        Button(
            onClick = { navController.navigate(Routes.composeMeal) },
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))

        ) {
            Text(text = "BACK TO COMPOSITION")
        }
    }
}

@Preview
@Composable
fun ResultScreenPreview() {

    val ingredientViewModel: IngredientViewModel = viewModel()
    ingredientViewModel.ingredients.add(Ingredient(Product("Banane", 20.0f), 100.0f))
    ingredientViewModel.ingredients.add(Ingredient(Product("Weiss Brot", 50.0f), 100.0f))
    val navController = rememberNavController()

    ResultScreen(navController = navController, ingredientViewModel = ingredientViewModel)
}