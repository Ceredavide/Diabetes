package ch.hslu.mobpro.diabetes.presentation.ui.product.form

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.R
import ch.hslu.mobpro.diabetes.data.database.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductFormViewModel : ViewModel() {

    var productName by mutableStateOf("")
        private set

    var carbs by mutableFloatStateOf(0.0f)
        private set

    fun updateProductName(name: String) {
        productName = name
    }

    fun updateCarbs(carbsValue: Float) {
        carbs = carbsValue
    }

    fun updateFetchedProduct(name: String?, carbsValue: Float?) {
        productName = name ?: ""
        carbs = carbsValue ?: 0.0f
    }

    fun setInitialValues(name: String, carbs: Float) {
        productName = name
        this.carbs = carbs
    }

    fun onAdd(context: Context, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        if (validateInput(productName, carbs, context)) {
            val product = Product(productName, carbs)
            viewModelScope.launch(Dispatchers.IO) {
                if (!productExistsCheck(productName)) {
                    MainActivity.productDao.insertProduct(product)
                    withContext(Dispatchers.Main) {
                        onSuccess("$productName SAVED")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onFailure("$productName ALREADY EXISTS\nPLEASE MODIFY EXISTING ONE")
                    }
                }
            }
        }
    }

    private fun validateInput(productName: String, carbs: Float, context: Context): Boolean {
        val productNameEmpty = productName.isEmpty()
        val carbsIsNull = carbs == 0.0f

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

    private fun showToast(context: Context, vararg messages: String) {
        Toast.makeText(context, "PLEASE ENTER A VALUE FOR ${messages.joinToString(" and ")}", Toast.LENGTH_LONG).show()
    }

    private fun productExistsCheck(productName: String): Boolean {
        return MainActivity.productDao.getProductByName(productName) != null
    }
}
