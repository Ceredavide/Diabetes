package ch.hslu.mobpro.diabetes.presentation.ui.products.components

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.data.database.entity.Product
import com.journeyapps.barcodescanner.ScanContract
import ch.hslu.mobpro.diabetes.presentation.common.shared_components.TextField

@Composable
fun ProductFormDialog(
    viewModel: ProductFormDialogViewModel,
    onSave: (Product) -> Unit
) {
    val context = LocalContext.current

    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result ->
        result.contents?.let {
            viewModel.scannedCode = it
            viewModel.fetchProductDetails(it, context)
        } ?: run {
            Toast.makeText(context, "Scan cancelled or failed", Toast.LENGTH_SHORT).show()
            viewModel.updateFetchedProduct(null, null)
        }
    }

    if (viewModel.isVisible) {
        AlertDialog(
            onDismissRequest = {
                viewModel.hideDialog()
            },
            title = {
                Text(
                    text = if (viewModel.isEditMode) stringResource(id = R.string.edit_product) else stringResource(
                        id = R.string.add_product
                    )
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = viewModel.productName,
                        onValueChange = { viewModel.updateProductName(it) },
                        label = stringResource(id = R.string.product_name),
                        error = viewModel.productNameError.value,
                        imeAction = ImeAction.Done,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = viewModel.carbs.toString(),
                        onValueChange = {
                            val newValue = it.toFloatOrNull() ?: 0.0f
                            viewModel.updateCarbs(newValue)
                        },
                        label = stringResource(id = R.string.carbs_per_100g),
                        error = viewModel.carbsError.value,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        viewModel.hideDialog()
                    }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    Button(onClick = { viewModel.startBarcodeScanner(scanLauncher) }) {
                        Text(text = stringResource(id = R.string.scan_barcode))
                    }
                    Button(onClick = {
                        viewModel.onSave(onSuccess = { product ->
                            onSave(product)
                            viewModel.hideDialog()
                            Toast.makeText(context, "Product saved", Toast.LENGTH_LONG).show()
                        }, onFailure = {
                            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                        })
                    }) {
                        Text(text = stringResource(id = R.string.save))
                    }
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductFormDialogPreview() {
    val viewModel = object : ProductFormDialogViewModel() {}
    viewModel.addProduct()
    ProductFormDialog(viewModel = viewModel, onSave = {})
}