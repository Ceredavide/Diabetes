package ch.hslu.mobpro.diabetes.data.network.service

import ch.hslu.mobpro.diabetes.data.network.model.ProductResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsApiService {
    @GET("api/v0/product/{barcode}.json")
    fun getProductByBarcode(@Path("barcode") barcode: String): Call<ProductResponse>
}