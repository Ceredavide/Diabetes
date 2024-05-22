package ch.hslu.mobpro.diabetes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.data.database.Product
import ch.hslu.mobpro.diabetes.math.Ingredient
import ch.hslu.mobpro.diabetes.ui.viewmodels.IngredientViewModel

@Composable
fun IngredientListItem(ingredient: Ingredient, ingredientViewModel: IngredientViewModel, check: Boolean) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .border(2.dp, MaterialTheme.colors.primary, RectangleShape),
        verticalAlignment = Alignment.CenterVertically
    ) {


        val amount = if (ingredient.weightAmount == 0.0f) "" else ingredient.weightAmount.toString()
        var carbsInput by remember { mutableStateOf(amount) }
        var color by remember { mutableStateOf(Color.Transparent) }

        Column() {

            Text(
                text = ingredient.product.name!!,
                fontSize = 16.sp,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(10.dp)
            )

            FloatTextField(
                value = carbsInput,
                onValueChange = {
                    carbsInput = it
                    ingredient.weightAmount = carbsInput.toFloatOrNull()
                },
                label = "Weight Amount in g/ml",
                modifier = Modifier.background(color)
            ) .also {

                if (check) {
                    if (carbsInput.isEmpty() || ingredient.weightAmount == 0.0f) {

                        color = Color.Red
                    } else {

                        color = Color.Transparent
                    }
                }
                else {

                    color = Color.Transparent
                }
            }
        }


        IconButton(
            onClick = { ingredientViewModel.removeIngredient(ingredient) },
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Transparent)
        ){
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.0f)

            )
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
