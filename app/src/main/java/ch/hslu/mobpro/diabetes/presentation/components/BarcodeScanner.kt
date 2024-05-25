package ch.hslu.mobpro.diabetes.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.hslu.mobpro.diabetes.data.network.client.RetrofitClient
import ch.hslu.mobpro.diabetes.data.network.model.ProductResponse
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun BarcodeScanner() {
    var scannedCode by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var carbohydrates by remember { mutableStateOf<Float?>(null) }
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result ->
        result.contents?.let {
            scannedCode = it
            fetchProductDetails(scannedCode, onProductFetched = { name, carbs ->
                productName = name
                carbohydrates = carbs
            })
        } ?: run {
            scannedCode = "Scan cancelled or failed"
        }
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(onClick = { startBarcodeScanner(scanLauncher) }) {
                Text("Scan Barcode")
            }
            Text("Scanned Code: $scannedCode")
            if (productName.isNotEmpty()) {
                Text("Product Name: $productName")
                Text("Carbohydrates (per 100g): ${carbohydrates ?: "Not available"}")
            }
        }
    }
}

private fun startBarcodeScanner(scanLauncher: ActivityResultLauncher<ScanOptions>) {
    val options = ScanOptions()
    options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
    options.setPrompt("Scan a barcode")
    options.setCameraId(1)
    options.setBeepEnabled(true)
    options.setBarcodeImageEnabled(true)
    scanLauncher.launch(options)
}

private fun fetchProductDetails(barcode: String, onProductFetched: (String, Float?) -> Unit) {
    val call = RetrofitClient.apiService.getProductByBarcode(barcode)
    call.enqueue(object : Callback<ProductResponse> {
        override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
            if (response.isSuccessful) {
                response.body()?.product?.let { product ->
                    onProductFetched(product.product_name, product.nutriments.carbohydrates_100g)
                }
            } else {
                onProductFetched("Product not found", null)
            }
        }

        override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
            onProductFetched("Error: ${t.message}", null)
        }
    })
}
