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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.database.Product
import ch.hslu.mobpro.diabetes.ui.math.Ingredient
import ch.hslu.mobpro.diabetes.ui.navigation.Routes
import ch.hslu.mobpro.diabetes.ui.viewmodels.IngredientViewModel

@Composable
fun IngredientListItem(ingredient: Ingredient, ingredientViewModel: IngredientViewModel) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .border(2.dp, MaterialTheme.colors.primary, RectangleShape)
            .padding(1.dp),
        //horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        var carbsInput by remember { mutableStateOf("") }

        Column() {

            Text(
                text = ingredient.product.name!!,
                fontSize = 16.sp,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(10.dp)
                    .clickable { }
            )

            FloatTextField(
                value = carbsInput,
                onValueChange = { carbsInput = it },
                label = "ENTER ${stringResource(id = R.string.carbs_per_100g)}"
            )


        }


        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Edit",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray)
                .padding(4.dp)
                .clickable {

                    ingredientViewModel.removeIngredient(ingredient)
                }
        )

    }
}
