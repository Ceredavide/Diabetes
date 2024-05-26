package ch.hslu.mobpro.diabetes.presentation.ui.home.components.composeMealScreen

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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.presentation.common.IngredientListItem
import ch.hslu.mobpro.diabetes.presentation.common.ActiveUserIndicator
import ch.hslu.mobpro.diabetes.presentation.common.FloatTextField
import ch.hslu.mobpro.diabetes.presentation.navigation.Routes
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.GlucoseReadingsViewModel
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.IngredientViewModel

@Composable
fun ComposeMealScreen(
    navController: NavController,
    ingredientViewModel: IngredientViewModel,
    glucoseReadingsViewModel: GlucoseReadingsViewModel,
    composeMealViewModel: ComposeMealViewModel = viewModel()
) {
    val context = LocalContext.current
    val ingredients = ingredientViewModel.ingredients

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
                    composeMealViewModel.onCalculateClicked(navController, ingredients, glucoseReadingsViewModel, context)
                },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(text = "CALCULATE", color = Color.White)
            }

            Button(
                onClick = { composeMealViewModel.onClearClicked(ingredientViewModel) },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(text = "CLEAR", color = Color.White)
            }

            FloatingActionButton(
                modifier = Modifier.padding(top = 10.dp),
                onClick = { navController.navigate(Routes.uneditableProducts) },
            ) {
                Text(text = "+", style = TextStyle(fontSize = 48.sp))
            }
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))

        FloatTextField(
            value = composeMealViewModel.glucoseLevelString.value,
            onValueChange = { composeMealViewModel.onGlucoseLevelChanged(it) },
            label = stringResource(id = R.string.glucose_level),
            modifier = Modifier
                .background(composeMealViewModel.color.value)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        LazyColumn {
            items(ingredients.size) { index ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IngredientListItem(
                        ingredient = ingredients[index],
                        ingredientViewModel = ingredientViewModel,
                        check = composeMealViewModel.check.value
                    )
                    Spacer(modifier = Modifier.padding(top = 60.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun ComposeMealPreview() {
    val navController = rememberNavController()
    val ingredientViewModel: IngredientViewModel = viewModel()
    val glucoseReadingsViewModel: GlucoseReadingsViewModel = viewModel()
    val composeMealViewModel = ComposeMealViewModel()

    ComposeMealScreen(
        navController = navController,
        ingredientViewModel = ingredientViewModel,
        glucoseReadingsViewModel = glucoseReadingsViewModel,
        composeMealViewModel = composeMealViewModel
    )
}

