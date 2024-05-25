package ch.hslu.mobpro.diabetes.presentation.ui.product.form

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.presentation.common.shared_components.TextField
import ch.hslu.mobpro.diabetes.presentation.ui.product.form.components.BarcodeScanner

@Composable
fun ProductFormScreen(
    initialProductName: String = "",
    initialCarbs: Float = 0.0f,
    viewModel: ProductFormViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.setInitialValues(initialProductName, initialCarbs)
    }

    Column(
        modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Top
    ) {

        Spacer(modifier = Modifier.height(16.dp))

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

        Row {
            BarcodeScanner(onProductScanned = { name, carbohydrates ->
                viewModel.updateFetchedProduct(name, carbohydrates)
            })
            Button(onClick = {
                viewModel.onAdd(onSuccess = {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    viewModel.updateProductName("")
                    viewModel.updateCarbs(0.0f)
                }, onFailure = {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                })
            }) {
                Text(text = "Save Product")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
