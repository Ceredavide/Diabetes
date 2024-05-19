package ch.hslu.mobpro.diabetes

import android.util.Log
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

data class ProductResponse(
    @SerializedName("product") val product: Product?
)

data class Product(
    @SerializedName("nutriments") val nutriments: Nutriments?
)

data class Nutriments(
    @SerializedName("carbohydrates") val carbohydrates: Double?
)

fun getProductOpenFoodFactsExample() {

    CoroutineScope(Dispatchers.IO).launch {

        val product = fetchProduct("5060337508445")
        if (product != null) {
            Log.d("product", "${product}")
        }
        else {
            Log.d("product","NOT FOUND")
        }
    }
}
suspend fun fetchProduct(barCode: String) : Pair<String, Float>? {

    val client = OkHttpClient()

    val request = Request.Builder()
        .url("https://world.openfoodfacts.org/api/v0/product/$barCode.json")
        .build()

    return try {
        val response: Response = client.newCall(request).execute()
        if (response.isSuccessful) {

            return parseProductInfo(response.body?.string()!!)
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun searchProduct(productName: String) : Pair<String, Float>? {
    val client = OkHttpClient()

    val url = HttpUrl.Builder()
        .scheme("https")
        .host("world.openfoodfacts.org")
        .addPathSegments("api/v0/product/$productName.json")
        .build()

    val request = Request.Builder()
        .url(url)
        .build()

    return try {
        val response: Response = client.newCall(request).execute()
        if (response.isSuccessful) {
            parseProductInfo(response.body?.string()!!)
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun parseProductInfo(jsonString: String): Pair<String, Float>? {
    return try {

        var i = jsonString.indexOf("product_name")
        i = jsonString.indexOf(":" , i + 1) + 2
        val name = jsonString.substring(i, jsonString.indexOf("\"", i + 1))

        i = jsonString.indexOf("carbohydrates")
        i = jsonString.indexOf(":", i + 1) + 1
        val carbs = jsonString.substring(i, i + 10)
            .takeWhile { it.isDigit() || it == '.' }.toFloat()

        return Pair(name, carbs)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

