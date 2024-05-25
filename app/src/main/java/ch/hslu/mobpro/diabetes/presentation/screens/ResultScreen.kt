package ch.hslu.mobpro.diabetes.presentation.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.data.database.Product
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import ch.hslu.mobpro.diabetes.math.Ingredient
import ch.hslu.mobpro.diabetes.math.calculateInsulinDoseAndTotalCarbs
import ch.hslu.mobpro.diabetes.presentation.components.ActiveUserIndicator
import ch.hslu.mobpro.diabetes.presentation.navigation.Routes
import ch.hslu.mobpro.diabetes.presentation.viewmodels.IngredientViewModel

@Composable
fun ResultScreen(
        navController: NavController,
        ingredientViewModel: IngredientViewModel,
        glucoseLevel: Float,
        context: Context) {

    val ingredients = ingredientViewModel.ingredients
    val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

    Column(
            modifier = Modifier
                .padding(14.dp),
    ) {

        ActiveUserIndicator(navController = navController)

        val userInfo = PreferenceManager.instance.getActiveUserInfo(context)
        val userName = userInfo.value!!.name.value
        val insulinPer10gCarbs = userInfo.value!!.insulinPer10gCarbs.value
        val insulinPer1mmol_L = userInfo.value!!.insulinPer10gCarbs.value
        val lowerBoundGlucoseLevel = userInfo.value!!.lowerBoundGlucoseLevel.value
        val upperBoundGlucoseLevel = userInfo.value!!.upperBoundGlucoseLevel.value


        val textDecoration: TextDecoration
        val color: Color
        if (glucoseLevel < lowerBoundGlucoseLevel || glucoseLevel > upperBoundGlucoseLevel) {

            textDecoration = TextDecoration.Underline
            color = Color.Red
        }
        else {

            textDecoration = TextDecoration.None
            color = Color.Black
        }

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
            Text(text = userName!!, fontSize = 32.sp)
        }

        Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(text = "Glucose level", fontSize = 32.sp)
            Text(text = "$glucoseLevel",
                    style = TextStyle(
                            textDecoration = TextDecoration.Underline,
                            color = color,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Normal
                    )
            )
        }

        Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(text = "Total carbohydrates", fontSize = 32.sp)
            Text(text = "${totalCarbs}g", fontSize = 32.sp)
        }


        Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(text = "Units insulin", fontSize = 32.sp)

            Text(
                    text = "$insulinDose",
                    style = TextStyle(
                            textDecoration = textDecoration,
                            color = color,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Normal
                    )
            )
        }

        Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 8.dp
        )

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

    ResultScreen(
            navController = navController,
            ingredientViewModel = ingredientViewModel,
            glucoseLevel = 5.0f,
            context = LocalContext.current
    )
}