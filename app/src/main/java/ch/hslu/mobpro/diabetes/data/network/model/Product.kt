package ch.hslu.mobpro.diabetes.data.network.model

data class Nutriments(
    val carbohydrates_100g: Float?
)

data class Product(
    val code: String,
    val product_name: String,
    val nutriments: Nutriments
)

data class ProductResponse(
    val product: Product
)
