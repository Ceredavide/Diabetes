package ch.hslu.mobpro.diabetes.presentation.ui.product.form.components

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import ch.hslu.mobpro.diabetes.data.network.client.RetrofitClient
import ch.hslu.mobpro.diabetes.data.network.model.ProductResponse
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun BarcodeScanner(onProductScanned: (String?, Float?) -> Unit) {

    val context = LocalContext.current

    var scannedCode by remember { mutableStateOf("") }
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result ->
        result.contents?.let {
            scannedCode = it
            fetchProductDetails(scannedCode, onProductFetched = { name, carbs ->
                onProductScanned(name, carbs)
            }, context)
        } ?: run {
            Toast.makeText(context, "Scan cancelled or failed", Toast.LENGTH_SHORT).show()
            onProductScanned(null, null)
        }
    }
    Button(onClick = { startBarcodeScanner(scanLauncher) }) {
        Text("Scan Barcode")
    }
}

private fun startBarcodeScanner(scanLauncher: ActivityResultLauncher<ScanOptions>) {
    val options = ScanOptions()
    options.setDesiredBarcodeFormats(ScanOptions.PRODUCT_CODE_TYPES)
    options.setPrompt("Scan a barcode, you have 15 seconds.")
    // no method available to interrupt scan operation
    // so i set a timeout of 10 seconds
    options.setTimeout(10000)
    options.setCameraId(1)
    options.setBeepEnabled(true)
    options.setBarcodeImageEnabled(true)
    scanLauncher.launch(options)
}

private fun fetchProductDetails(barcode: String, onProductFetched: (String?, Float?) -> Unit, context : Context) {
    val call = RetrofitClient.apiService.getProductByBarcode(barcode)
    call.enqueue(object : Callback<ProductResponse> {
        override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
            if (response.isSuccessful) {
                response.body()?.product?.let { product ->
                    onProductFetched(product.product_name, product.nutriments.carbohydrates_100g)
                }
            } else {
                Toast.makeText(context, "Product not found", Toast.LENGTH_SHORT).show()
                onProductFetched(null, null)
            }
        }

        override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
            onProductFetched(null, null)
        }
    })
}
