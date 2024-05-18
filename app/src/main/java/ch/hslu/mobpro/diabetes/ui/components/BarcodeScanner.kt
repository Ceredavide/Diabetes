package ch.hslu.mobpro.diabetes.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun BarcodeScanner() {
    var scannedCode by remember { mutableStateOf("") }
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result ->
        result.contents?.let {
            scannedCode = it
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