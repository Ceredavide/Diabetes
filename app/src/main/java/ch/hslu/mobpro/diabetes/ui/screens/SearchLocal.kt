package ch.hslu.mobpro.diabetes.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.data.database.Product
import ch.hslu.mobpro.diabetes.http.getProductOpenFoodFactsExample
import ch.hslu.mobpro.diabetes.ui.components.ProductListItem
import ch.hslu.mobpro.diabetes.ui.viewmodels.IngredientViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SearchLocalScreen(navController: NavController,
                      editable: Boolean,
                      ingredientViewModel: IngredientViewModel?) {

    val productsState = remember { mutableStateOf<List<Product>>(emptyList()) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        var text by remember { mutableStateOf(TextFieldValue("")) }

        Button(
            onClick = {
                getProductOpenFoodFactsExample()
            }
        ) {
            Text(text = "SEND REQUEST")
        }

        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                onTextInputChange(it.text, productsState)
            },
            label = { Text(stringResource(id = R.string.product_name)) },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        val currentContext = LocalContext.current
        LazyColumn {
            items(productsState.value.size) { index ->
                Box(
                    Modifier
                        .fillMaxWidth()
                ) {

                    ProductListItem(
                        navController = navController,
                        product = productsState.value[index],
                        editable = editable,
                        ingredientViewModel = ingredientViewModel
                    )
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }
}
fun onTextInputChange(productName: String, productsState: MutableState<List<Product>>) {


    CoroutineScope(Dispatchers.IO).launch {

        if (productName.isNotEmpty()) {

            val foundProducts = MainActivity.productDao.findProductsFuzzyOrdered(productName)
            withContext(Dispatchers.Main) {

                productsState.value = foundProducts
            }
        } else {

            withContext(Dispatchers.Main) {
                productsState.value = emptyList()
            }
        }
    }
}

@Preview
@Composable
fun SearchLocalPreview() {

    val navController = rememberNavController()
    val ingredientViewModel: IngredientViewModel = viewModel()
    SearchLocalScreen(navController = navController, editable = true, ingredientViewModel = ingredientViewModel)
}