package ch.hslu.mobpro.diabetes.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "product_name") var name: String?,
    @ColumnInfo(name = "carbs") var carbs: Float?
) {
    constructor(name: String, carbs: Float) : this(0, name, carbs)
}