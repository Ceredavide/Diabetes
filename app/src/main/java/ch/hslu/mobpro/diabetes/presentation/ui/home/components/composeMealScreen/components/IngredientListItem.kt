package ch.hslu.mobpro.diabetes.presentation.ui.home.components.composeMealScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.hslu.mobpro.diabetes.data.database.entity.Product
import ch.hslu.mobpro.diabetes.presentation.common.shared_components.FloatTextField
import ch.hslu.mobpro.diabetes.utils.Ingredient
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.IngredientViewModel

@Composable
fun IngredientListItem(ingredient: Ingredient, ingredientViewModel: IngredientViewModel, check: Boolean) {

    var carbsInput by remember { mutableStateOf(if (ingredient.weightAmount == 0.0f) "" else ingredient.weightAmount.toString()) }
    var color by remember { mutableStateOf(Color.Transparent) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(Color.White)
            .border(1.dp, MaterialTheme.colors.primary, RoundedCornerShape(8.dp))
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = ingredient.product.name!!,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(4.dp)
                )
                FloatTextField(
                    value = carbsInput,
                    onValueChange = {
                        carbsInput = it
                        ingredient.weightAmount = carbsInput.toFloatOrNull() ?: 0.0f
                    },
                    label = "Weight Amount in g/ml",
                    modifier = Modifier.background(color)
                ).also {
                    color = if (check && (carbsInput.isEmpty() || ingredient.weightAmount == 0.0f)) {
                        Color.Red
                    } else {
                        Color.Transparent
                    }
                }
            }

            IconButton(
                onClick = { ingredientViewModel.removeIngredient(ingredient) },
                modifier = Modifier
                    .padding(2.dp)
                    .background(Color.Transparent)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colors.error,
                    modifier = Modifier
                        .size(36.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun IngredientListItemPreview() {
    val ingredientViewModel: IngredientViewModel = viewModel()
    val ingredient = Ingredient(Product("Banane", 20.0f), 100.0f)

    IngredientListItem(ingredient = ingredient, ingredientViewModel = ingredientViewModel, check = false)
}
