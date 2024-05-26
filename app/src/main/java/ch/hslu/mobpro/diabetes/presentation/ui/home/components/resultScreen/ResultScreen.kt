package ch.hslu.mobpro.diabetes.presentation.ui.home.components.resultScreen

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import ch.hslu.mobpro.diabetes.data.database.entity.Product
import ch.hslu.mobpro.diabetes.presentation.common.shared_components.ActiveUserIndicator
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.IngredientViewModel
import ch.hslu.mobpro.diabetes.presentation.navigation.Routes
import ch.hslu.mobpro.diabetes.utils.Ingredient

@Composable
fun ResultScreen(
    navController: NavController,
    ingredientViewModel: IngredientViewModel,
    glucoseLevel: Float,
    context: Context,
    resultScreenViewModel: ResultScreenViewModel = viewModel()
) {
    val ingredients = ingredientViewModel.ingredients
    resultScreenViewModel.loadUserInfo(context)
    resultScreenViewModel.calculateInsulinAndCarbs(glucoseLevel, ingredients)

    Column(modifier = Modifier.padding(14.dp)) {
        ActiveUserIndicator(navController = navController)

        UserInfoRow(label = "User", value = resultScreenViewModel.userName.value)
        UserInfoRow(
            label = "Glucose level",
            value = "$glucoseLevel",
            textDecoration = resultScreenViewModel.textDecoration.value,
            color = resultScreenViewModel.color.value
        )
        UserInfoRow(label = "Total carbohydrates", value = "${resultScreenViewModel.totalCarbs.value}g")
        UserInfoRow(
            label = "Units insulin",
            value = "${resultScreenViewModel.insulinDose.value}",
            textDecoration = resultScreenViewModel.textDecoration.value,
            color = resultScreenViewModel.color.value
        )

        Divider(modifier = Modifier.fillMaxWidth(), thickness = 8.dp)

        ingredients.forEach {
            IngredientRow(ingredient = it)
        }

        Spacer(modifier = Modifier.padding(top = 60.dp))

        NavigationButton(navController = navController, route = Routes.dashboard)
    }
}

@Composable
fun UserInfoRow(label: String, value: String, textDecoration: TextDecoration = TextDecoration.None, color: Color = Color.Black) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, fontSize = 32.sp)
        Text(
            text = value,
            style = TextStyle(
                textDecoration = textDecoration,
                color = color,
                fontSize = 32.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}

@Composable
fun IngredientRow(ingredient: Ingredient) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = ingredient.product.name ?: "", fontSize = 32.sp)
        Text(text = "${ingredient.weightAmount}g", fontSize = 32.sp)
    }
}

@Composable
fun NavigationButton(navController: NavController, route: String) {
    Button(
        onClick = { navController.navigate(route) },
        modifier = Modifier.clip(RoundedCornerShape(4.dp))
    ) {
        Text(text = "BACK TO COMPOSITION")
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
