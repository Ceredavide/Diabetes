package ch.hslu.mobpro.diabetes.presentation.common

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.data.database.Product
import ch.hslu.mobpro.diabetes.utils.Ingredient
import ch.hslu.mobpro.diabetes.presentation.navigation.Routes
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.IngredientViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProductListItem(navController: NavController,
                    product: Product,
                    editable: Boolean,
                    ingredientViewModel: IngredientViewModel?) {

    val context = LocalContext.current

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .border(2.dp, MaterialTheme.colors.primary, RectangleShape)
            .padding(1.dp)
            .clickable {

                if (!editable) {
                    if (ingredientViewModel?.contains(product.name!!)!!) {

                        Toast.makeText(context, "Product already in ingredient list", Toast.LENGTH_LONG).show()
                    }
                    else {
                        ingredientViewModel.addIngredient(
                                Ingredient(
                                        product = product,
                                        weightAmount = 0f
                                )
                        )
                    }
                    navController.navigate(Routes.composeMeal)
                }
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = product.name!!,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(10.dp)
        )

        if (editable) {

            EditDeleteButtons(
                modifier = Modifier.padding(8.dp),
                onEdit = { navController.navigate(Routes.editProduct + "/${product.name}/${product.carbs}") },
                onDelete = {

                    CoroutineScope(Dispatchers.IO).launch {

                        MainActivity.productDao.deleteProductByName(product.name!!)
                    }
                    Toast
                        .makeText(context, "DELETED PRODUCT ${product.name}", Toast.LENGTH_LONG)
                        .show()
                    navController.navigate(Routes.searchLocal)
                },
                deletable = true
            )
        }
    }
}


@Preview
@Composable
fun ProductListItemPreview() {

    val navController = rememberNavController()
    val product = Product("Banane", 2.0f)

    ProductListItem(
        navController = navController,
        product = product,
        editable = true
        , ingredientViewModel = null)
}


