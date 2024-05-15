package ch.hslu.mobpro.diabetes

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "product_name") val name: String?,
    @ColumnInfo(name = "carbs") val carbs: Float?
) {
    constructor(name: String, carbs: Float) : this(0, name, carbs)
}