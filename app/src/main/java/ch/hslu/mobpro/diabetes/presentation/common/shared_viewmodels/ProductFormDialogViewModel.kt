package ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels

import android.content.Context
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.data.database.entity.Product
import ch.hslu.mobpro.diabetes.data.network.client.RetrofitClient
import ch.hslu.mobpro.diabetes.data.network.model.ProductResponse
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class ProductFormDialogViewModel : ViewModel() {

    var isVisible by mutableStateOf(false)
        private set

    var isEditMode by mutableStateOf(false)
        private set

    private var originalName by mutableStateOf("")

    var productName by mutableStateOf("")
        private set

    var carbs by mutableFloatStateOf(0.0f)
        private set

    var scannedCode by mutableStateOf("")

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

    fun addProduct() {
        isVisible = true
    }

    fun editProduct( product: Product) {
        isEditMode = true
        originalName = product.name ?: ""
        productName = originalName
        carbs = product.carbs ?: 0.0f
        isVisible = true
    }

    fun hideDialog(){
        isVisible = false
        isEditMode = false
        productName = ""
        carbs = 0.0f
        productNameError.value = null
        carbsError.value = null
    }

    fun onSave(onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        if (validateAll()) {
            viewModelScope.launch(Dispatchers.IO) {
                if (isEditMode) {
                    val product = MainActivity.database.productDao().getProductByName(originalName)
                    if (product != null) {
                        product.name = productName
                        product.carbs = carbs
                        MainActivity.database.productDao().updateProduct(product)
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
                        MainActivity.database.productDao().insertProduct(product)
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
        return MainActivity.database.productDao().getProductByName(productName) != null
    }

    fun startBarcodeScanner(scanLauncher: ActivityResultLauncher<ScanOptions>) {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.PRODUCT_CODE_TYPES)
        options.setPrompt("Scan a barcode, you have 15 seconds.")
        options.setTimeout(10000)
        options.setCameraId(1)
        options.setBeepEnabled(true)
        options.setBarcodeImageEnabled(true)
        scanLauncher.launch(options)
    }

    fun fetchProductDetails(barcode: String, context: Context) {
        val call = RetrofitClient.apiService.getProductByBarcode(barcode)
        call.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    response.body()?.product?.let { product ->
                        updateFetchedProduct(product.product_name, product.nutriments.carbohydrates_100g)
                    }
                } else {
                    Toast.makeText(context, "Product not found", Toast.LENGTH_SHORT).show()
                    updateFetchedProduct(null, null)
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
                updateFetchedProduct(null, null)
            }
        })
    }
}
