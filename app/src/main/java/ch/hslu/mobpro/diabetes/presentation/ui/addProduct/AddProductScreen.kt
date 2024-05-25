package ch.hslu.mobpro.diabetes.presentation.ui.addProduct

import android.content.Context
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.data.database.Product
import ch.hslu.mobpro.diabetes.presentation.common.FloatTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Preview
@Composable
fun AddProductScreen() {
    var productName by remember { mutableStateOf(TextFieldValue("")) }
    var carbs by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text(stringResource(id = R.string.product_name)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        FloatTextField(
            value = carbs,
            onValueChange = { carbs = it },
            label = stringResource(id = R.string.carbs_per_100g),
            positiveLimit = 100.0f,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        FloatingActionButton(
            onClick = {
                val productNameText = productName.text
                val carbsValue = carbs.toFloatOrNull()

                onAdd(
                    productName = productNameText,
                    carbs = carbsValue,
                    context = context,
                    coroutineScope = coroutineScope,
                    onSuccess = {
                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                        productName = TextFieldValue()
                        carbs = ""
                    },
                    onFailure = {
                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    }
                )
            }
        ) {
            Text(text = "+", style = TextStyle(fontSize = 48.sp))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

fun onAdd(
    productName: String,
    carbs: Float?,
    context: Context,
    coroutineScope: CoroutineScope,
    onSuccess: (String) -> Unit,
    onFailure: (String) -> Unit
) {
    if (validateInput(productName, carbs, context)) {
        val product = Product(productName, carbs!!)

        coroutineScope.launch(Dispatchers.IO) {
            if (!productExistsCheck(productName)) {
                MainActivity.productDao.insertProduct(product)
                postToMainThread { onSuccess("$productName SAVED") }
            } else {
                postToMainThread { onFailure("$productName ALREADY EXISTS\nPLEASE MODIFY EXISTING ONE") }
            }
        }
    }
}

fun validateInput(productName: String, carbs: Float?, context: Context): Boolean {
    val productNameEmpty = productName.isEmpty()
    val carbsIsNull = carbs == null

    when {
        productNameEmpty && carbsIsNull -> {
            showToast(context, context.getString(R.string.product_name), context.getString(R.string.carbs_per_100g))
        }
        productNameEmpty -> {
            showToast(context, context.getString(R.string.product_name))
        }
        carbsIsNull -> {
            showToast(context, context.getString(R.string.carbs_per_100g))
        }
    }

    return !productNameEmpty && !carbsIsNull
}

fun showToast(context: Context, vararg messages: String) {
    Toast.makeText(context, "PLEASE ENTER A VALUE FOR ${messages.joinToString(" and ")}", Toast.LENGTH_LONG).show()
}

fun postToMainThread(action: () -> Unit) {
    android.os.Handler(Looper.getMainLooper()).post { action() }
}

fun productExistsCheck(productName: String): Boolean {
    return MainActivity.productDao.getProductByName(productName) != null
}
