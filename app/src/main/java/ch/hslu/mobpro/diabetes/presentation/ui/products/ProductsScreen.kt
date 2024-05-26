package ch.hslu.mobpro.diabetes.presentation.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.data.database.entity.Product
import ch.hslu.mobpro.diabetes.presentation.common.shared_components.Header
import ch.hslu.mobpro.diabetes.presentation.common.shared_components.ProductFormDialog
import ch.hslu.mobpro.diabetes.presentation.ui.products.components.ProductListItem
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.IngredientViewModel
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.ProductFormDialogViewModel
import ch.hslu.mobpro.diabetes.presentation.navigation.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ProductsScreen(
    navController: NavController,
    editable: Boolean,
    ingredientViewModel: IngredientViewModel? = null
) {
    val productsState = remember { mutableStateOf<List<Product>>(emptyList()) }
    var text by remember { mutableStateOf(TextFieldValue("")) }
    onTextInputChange(text.text, productsState)

    val productFormDialogViewModel = ProductFormDialogViewModel()

    Scaffold(
        topBar = { Header(Routes.products) }
    ) { paddingValues ->
        ProductsContent(
            navController = navController,
            editable = editable,
            ingredientViewModel = ingredientViewModel,
            productsState = productsState,
            text = text,
            onTextChange = { newText ->
                text = newText
                onTextInputChange(newText.text, productsState)
            },
            onAddProductClick = { productFormDialogViewModel.addProduct() },
            paddingValues = paddingValues
        )
    }
}

@Composable
fun ProductsContent(
    navController: NavController,
    editable: Boolean,
    ingredientViewModel: IngredientViewModel?,
    productsState: MutableState<List<Product>>,
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    onAddProductClick: () -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        ProductFormDialog(viewModel = ProductFormDialogViewModel())

        SearchBar(text = text, onTextChange = onTextChange, onAddProductClick = onAddProductClick)

        Spacer(modifier = Modifier.height(16.dp))

        ProductList(
            navController = navController,
            products = productsState.value,
            editable = editable,
            ingredientViewModel = ingredientViewModel,
            onEditProduct = { product -> ProductFormDialogViewModel().editProduct(product) }
        )
    }
}

@Composable
fun SearchBar(
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    onAddProductClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            value = text,
            onValueChange = onTextChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            label = { Text(text = "Search Product:") }
        )
        FloatingActionButton(
            onClick = onAddProductClick,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(text = "+", style = TextStyle(fontSize = 24.sp))
        }
    }
}

@Composable
fun ProductList(
    navController: NavController,
    products: List<Product>,
    editable: Boolean,
    ingredientViewModel: IngredientViewModel?,
    onEditProduct: (Product) -> Unit
) {
    LazyColumn {
        items(products.size) { index ->
            Box(
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                ProductListItem(
                    navController = navController,
                    product = products[index],
                    editable = editable,
                    ingredientViewModel = ingredientViewModel,
                    onEdit = { onEditProduct(products[index]) }
                )
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

fun onTextInputChange(productName: String, productsState: MutableState<List<Product>>) {
    CoroutineScope(Dispatchers.IO).launch {
        val foundProducts = if (productName.isNotEmpty()) {
            MainActivity.database.productDao().findProductsFuzzyOrdered(productName)
        } else {
            MainActivity.database.productDao().getAllOrdered()
        }
        withContext(Dispatchers.Main) {
            productsState.value = foundProducts
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductsScreenPreview() {
    val navController = rememberNavController()
    val ingredientViewModel: IngredientViewModel = viewModel()
    ProductsScreen(navController = navController, editable = true, ingredientViewModel = ingredientViewModel)
}
