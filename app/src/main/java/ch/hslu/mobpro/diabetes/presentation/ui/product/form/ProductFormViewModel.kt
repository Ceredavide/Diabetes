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

    var isEditMode by mutableStateOf(false)
        private set

    var originalName by mutableStateOf("")
        private set

    var productName by mutableStateOf("")
        private set

    var carbs by mutableFloatStateOf(0.0f)
        private set

    // Validation errors
    val productNameError = mutableStateOf<String?>(null)
    val carbsError = mutableStateOf<String?>(null)

    fun updateProductName(name: String) {
        productName = name
        validateProductName()
    }

    fun updateCarbs(carbsValue: Float) {
        carbs = carbsValue
        validateCarbs()
    }

    fun updateFetchedProduct(name: String?, carbsValue: Float?) {
        productName = name ?: ""
        carbs = carbsValue ?: 0.0f
        validateProductName()
        validateCarbs()
    }

    fun setInitialValues(name: String, carbsValue: Float) {
        isEditMode = name.isNotBlank() && carbsValue != 0.0f
        originalName = name
        productName = name
        carbs = carbsValue
    }

    fun onSave(onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        if (validateAll()) {
            viewModelScope.launch(Dispatchers.IO) {
                if (isEditMode) {
                    val product = MainActivity.productDao.getProductByName(originalName)
                    if (product != null) {
                        product.name = productName
                        product.carbs = carbs
                        MainActivity.productDao.updateProduct(product)
                        withContext(Dispatchers.Main) {
                            onSuccess("$productName updated successfully!")
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            onFailure("Product with name $originalName not found")
                        }
                    }
                } else {
                    val product = Product(productName, carbs)
                    if (!productExistsCheck(productName)) {
                        MainActivity.productDao.insertProduct(product)
                        withContext(Dispatchers.Main) {
                            onSuccess("$productName saved successfully!")
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            onFailure("$productName already exists")
                        }
                    }
                }
            }
        }
    }

    private fun validateProductName() {
        productNameError.value = if (productName.isBlank()) {
            "Product name cannot be empty"
        } else {
            null
        }
    }

    private fun validateCarbs() {
        carbsError.value = if (carbs <= 0.0f) {
            "Carbs must be greater than 0"
        } else {
            null
        }
    }

    private fun validateAll(): Boolean {
        validateProductName()
        validateCarbs()
        return productNameError.value == null && carbsError.value == null
    }

    private fun productExistsCheck(productName: String): Boolean {
        return MainActivity.productDao.getProductByName(productName) != null
    }
}
