package ch.hslu.mobpro.diabetes.ui.screens.adding

import android.content.Context
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.data.database.Product
import ch.hslu.mobpro.diabetes.ui.components.FloatTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Preview
@Composable
fun EnterManualScreen() {

    var text by remember { mutableStateOf(TextFieldValue("")) }
    var carbs by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(stringResource(id = R.string.product_name)) },
            modifier = Modifier
                .fillMaxWidth()
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

                val productName = text.text
                val carbsFloat = carbs.toFloatOrNull()

                onAdd(
                    productName = productName,
                    carbs = carbsFloat,
                    context = context,
                    onSuccess  = {

                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                        text = TextFieldValue()
                        carbs = ""
                    },
                    onFailure = {

                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    })
            }
        ) {
            Text(text = "+", style = TextStyle(fontSize = 48.sp))

        }

        Spacer(modifier = Modifier.height(16.dp))

        // For easy way to delete all products for now
        FloatingActionButton(
            onClick = {

                CoroutineScope(Dispatchers.IO).launch {

                    val products = MainActivity.productDao.getAll()
                    for (product in products) {

                        MainActivity.productDao.deleteProduct(product)
                    }
                    Toast.makeText(context, "DELETED ALL PRODUCTS", Toast.LENGTH_LONG).show()
                }
            }
        ) {
            Text(text = "-", style = TextStyle(fontSize = 48.sp))
        }
    }
}

fun onAdd(productName: String,
          carbs: Float?,
          context: Context,
          onSuccess: (String) -> Unit,
          onFailure: (String) -> Unit) {


    if (validate(productName, carbs, context)) {

        val product = Product(productName, carbs!!)

        CoroutineScope(Dispatchers.IO).launch {

            if (!productExistsCheck(productName = productName, context = context)) {

                MainActivity.productDao.insertProduct(product)

                val runnable = Runnable { onSuccess("${productName} SAVED") }
                android.os.Handler(Looper.getMainLooper()).post(runnable)
            }
            else {

                val runnable = Runnable { onFailure("${productName} ALREADY EXISTS\nPLEASE MODIFY EXISTING ONE") }
                android.os.Handler(Looper.getMainLooper()).post(runnable)
            }
        }
    }
}




fun validate(productName: String, carbs: Float?, context: Context): Boolean {

    val productNameEmpty = productName.isEmpty()
    val carbsIsNull = carbs == null
    if (productNameEmpty && carbsIsNull) {

        Toast.makeText(context,
            "PLEASE ENTER A VALUE FOR ${context.getString(R.string.product_name)} and ${context.getString(R.string.carbs_per_100g)}",
            Toast.LENGTH_LONG).show()
    }
    else {
        if (carbsIsNull) {

            Toast.makeText(
                context,
                "PLEASE ENTER A VALUE FOR ${context.getString(R.string.carbs_per_100g)}",
                Toast.LENGTH_LONG
            ).show()
        }
        if (productNameEmpty) {
            Toast.makeText(
                context,
                "PLEASE ENTER A VALUE FOR ${context.getString(R.string.product_name)}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    return !productNameEmpty && !carbsIsNull
}

fun productExistsCheck(productName: String, context: Context): Boolean {

    return MainActivity.productDao.getProductByName(productName) != null
}
