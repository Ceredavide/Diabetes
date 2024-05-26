package ch.hslu.mobpro.diabetes.presentation.common.shared_components

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.ProductFormDialogViewModel
import com.journeyapps.barcodescanner.ScanContract

@Composable
fun ProductFormDialog(
    viewModel: ProductFormDialogViewModel,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {}

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
    if(viewModel.isVisible){
        AlertDialog(
            onDismissRequest = {
                viewModel.hideDialog()
            },
            title = {
                Text(text = if (viewModel.isEditMode) "Edit Product" else "Add Product")
            },
            text = {
                Column(
                    modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Top
                ) {

                    TextField(
                        value = viewModel.productName,
                        onValueChange = { viewModel.updateProductName(it) },
                        label = stringResource(id = R.string.product_name),
                        error = viewModel.productNameError.value,
                        imeAction = ImeAction.Done
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
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            },
            buttons = {
                Row {
                    Button(onClick = {
                        viewModel.hideDialog()
                    }) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = { viewModel.startBarcodeScanner(scanLauncher) }) {
                        Text("Scan Barcode")
                    }
                    Button(onClick = {
                        viewModel.onSave(onSuccess = {
                            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                            viewModel.updateProductName("")
                            viewModel.updateCarbs(0.0f)
                            viewModel.hideDialog()
                        }, onFailure = {
                            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                        })
                    }) {
                        Text(text = "Save")
                    }
                }
            },
        )
    }
}
