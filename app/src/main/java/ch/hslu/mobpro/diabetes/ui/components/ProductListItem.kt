package ch.hslu.mobpro.diabetes.ui.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.database.Product
import ch.hslu.mobpro.diabetes.ui.math.Ingredient
import ch.hslu.mobpro.diabetes.ui.navigation.Routes
import ch.hslu.mobpro.diabetes.ui.viewmodels.IngredientViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProductListItem(navController: NavController,
                    product: Product,
                    editable: Boolean,
                    ingredientViewModel: IngredientViewModel?) {

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .border(2.dp, MaterialTheme.colors.primary, RectangleShape)
            .padding(1.dp)
            .clickable {

                if (!editable) {
                    ingredientViewModel?.addIngredient(
                        Ingredient(
                            product = product,
                            weightAmount = 0f
                        )
                    )
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
            EditDeleteIcons(
                navController = navController,
                product = product,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
private fun EditDeleteIcons(navController: NavController,
                            product: Product,
                            modifier: Modifier = Modifier) {

    val context = LocalContext.current

    Row(modifier = modifier) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit",
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray)
                .padding(4.dp)
                .clickable { navController.navigate(Routes.editProduct + "/${product.name}/${product.carbs}") }
        )

        Spacer(modifier = Modifier.padding(3.dp))

        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Red)
                .padding(4.dp)
                .clickable {

                    CoroutineScope(Dispatchers.IO).launch {

                        MainActivity.productDao.deleteProductByName(product.name!!)
                    }
                    Toast.makeText(context, "DELETED PRODUCT ${product.name}", Toast.LENGTH_LONG).show()
                    navController.navigate(Routes.searchLocal)
                }
        )
    }
}



