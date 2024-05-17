package ch.hslu.mobpro.diabetes.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.database.Product
import ch.hslu.mobpro.diabetes.ui.components.FloatTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EditProduct(productName: String, productCarbs: Float) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {

        var nameImput by remember { mutableStateOf(TextFieldValue(productName)) }
        var carbsInput by remember { mutableStateOf((productCarbs.toString())) }
        var color by remember { mutableStateOf(Color.LightGray) }
        var changeDetected by remember { mutableStateOf(false) }

        TextField(
            value = nameImput,
            onValueChange = {
                changeDetected = hasChanged(productName, it.text, productCarbs, carbsInput.toFloatOrNull())
                if (changeDetected) {

                    color = Color.Green
                }
                else {

                    color = Color.LightGray
                }
                nameImput  = it },
            label = { Text(stringResource(id = R.string.product_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        FloatTextField(
            value = carbsInput,
            onValueChange = {
                changeDetected = hasChanged(productName, nameImput.text, productCarbs, it.toFloat())
                if (changeDetected) {

                    color = Color.Green
                }
                else {

                    color = Color.LightGray
                }
                carbsInput = it
                            },
            label = stringResource(id = R.string.carbs_per_100g)
        )

        Spacer(modifier = Modifier.height(15.dp))

        val context = LocalContext.current
        IconButton(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(color),
            onClick = {

                if (changeDetected && onSave(nameImput.text, carbsInput.toFloatOrNull(), context)) {

                    Toast.makeText(context, "SAVED CHANGES", Toast.LENGTH_LONG).show()
                }
                else {

                    Toast.makeText(context, "FAILED TO SAVE CHANGES", Toast.LENGTH_LONG).show()
                }
            }
        ) {

            Icon(Icons.Default.Save, contentDescription = "Save" )
        }
    }
}

fun hasChanged(originalName: String,
               newName: String,
               originalCarbs: Float,
               newCarbs: Float?): Boolean {

    return originalName != newName || originalCarbs != newCarbs
}

fun onSave(productName: String, carbs: Float?, context: Context): Boolean {

    if (validate(productName, carbs, context)) {

        val product = Product(productName, carbs!!)
        CoroutineScope(Dispatchers.IO).launch {

            val productDao = MainActivity.productDao
            productDao.insertProduct(product)
        }

        return true
    }

    return false

}