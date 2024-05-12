package ch.hslu.mobpro.diabetes

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
data class Product(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "product_name") val name: String?,
    @ColumnInfo(name = "carbs") val carbs: Int?
)