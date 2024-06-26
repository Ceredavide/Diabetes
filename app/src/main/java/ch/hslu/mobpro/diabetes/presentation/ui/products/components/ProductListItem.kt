package ch.hslu.mobpro.diabetes.presentation.ui.products.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.data.database.entity.Product
import ch.hslu.mobpro.diabetes.presentation.common.shared_components.EditDeleteButtons
import ch.hslu.mobpro.diabetes.utils.Ingredient
import ch.hslu.mobpro.diabetes.presentation.navigation.Routes
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.IngredientViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProductListItem(
    navController: NavController,
    product: Product,
    editable: Boolean,
    ingredientViewModel: IngredientViewModel?,
    onEdit: (Product) -> Unit,
) {

    val context = LocalContext.current

    Log.d("ProductListItem", "Ingredient count: ${ingredientViewModel?.ingredients?.size}")

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .shadow(2.dp, RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable {

                if (!editable) {
                    if (ingredientViewModel!!.contains(product.name!!)) {
                        Toast
                            .makeText(
                                context,
                                "Product already in ingredient list",
                                Toast.LENGTH_LONG
                            )
                            .show()
                    } else {
                        ingredientViewModel.addIngredient(
                            Ingredient(
                                product = product,
                                weightAmount = 0f
                            )
                        )
                    }
                    navController.navigate(Routes.composeMeal)
                }
            }
            .border(1.dp, MaterialTheme.colors.primary, RoundedCornerShape(8.dp))
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                    onEdit = { onEdit(product) },
                    onDelete = {
                        CoroutineScope(Dispatchers.IO).launch {
                            MainActivity.database.productDao().deleteProductByName(product.name!!)
                        }
                        Toast
                            .makeText(context, "DELETED PRODUCT ${product.name}", Toast.LENGTH_LONG)
                            .show()
                        navController.navigate(Routes.products)
                    },
                    deletable = true
                )
            }
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
        editable = true,
        ingredientViewModel = IngredientViewModel(),
        onEdit = {}
    )
}
