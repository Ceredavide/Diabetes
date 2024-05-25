package ch.hslu.mobpro.diabetes.presentation.ui.product.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.data.database.Product
import ch.hslu.mobpro.diabetes.presentation.common.ProductListItem
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.IngredientViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SearchLocalScreen(
        navController: NavController,
        editable: Boolean,
        ingredientViewModel: IngredientViewModel?
) {

    val productsState = remember { mutableStateOf<List<Product>>(emptyList()) }
    var text by remember { mutableStateOf(TextFieldValue("")) }
    onTextInputChange(text.text, productsState)

    Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                    onTextInputChange(text.text, productsState)
                },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                label = { Text(stringResource(id = R.string.product_name)) },
                modifier = Modifier
                    .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

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
        }
        else {

            val allProducts = MainActivity.productDao.getAllOrdered()
            withContext(Dispatchers.Main) {

                productsState.value = allProducts
            }
        }
    }
}

@Preview
@Composable
fun SearchLocalPreview() {

    val navController = rememberNavController()
    val ingredientViewModel: IngredientViewModel = viewModel()
    SearchLocalScreen(
            navController = navController,
            editable = true,
            ingredientViewModel = ingredientViewModel
    )
}