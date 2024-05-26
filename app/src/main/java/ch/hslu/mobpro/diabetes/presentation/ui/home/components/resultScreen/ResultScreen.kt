package ch.hslu.mobpro.diabetes.presentation.ui.home.components.resultScreen

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        Text(text = "Results:", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        UserInfoCard(
            label = "User:",
            value = resultScreenViewModel.userName.value
        )

        Spacer(modifier = Modifier.height(8.dp))

        UserInfoCard(
            label = "Total carbohydrates:",
            value = "${resultScreenViewModel.totalCarbs.floatValue}g"
        )

        Spacer(modifier = Modifier.height(8.dp))

        UserInfoCard(
            label = "Units insulin:",
            value = "${resultScreenViewModel.insulinDose.intValue}",
            textDecoration = resultScreenViewModel.textDecoration.value,
            color = resultScreenViewModel.color.value
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp), thickness = 1.dp, color = Color.Gray
        )

        Text(text = "Ingredients:", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        ingredients.forEach {
            IngredientCard(ingredient = it)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        NavigationButton(navController = navController, route = Routes.dashboard, ingredientViewModel = ingredientViewModel)
    }
}

@Composable
fun UserInfoCard(
    label: String,
    value: String,
    textDecoration: TextDecoration = TextDecoration.None,
    color: Color = Color.Black
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(
                text = value,
                style = TextStyle(
                    textDecoration = textDecoration,
                    color = color,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

@Composable
fun IngredientCard(ingredient: Ingredient) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = ingredient.product.name ?: "", fontSize = 20.sp)
            Text(text = "${ingredient.weightAmount}g", fontSize = 20.sp)
        }
    }
}

@Composable
fun NavigationButton(navController: NavController, route: String, ingredientViewModel: IngredientViewModel) {
    Button(
        onClick = {
            ingredientViewModel.clearIngredients()
            navController.navigate(route)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
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