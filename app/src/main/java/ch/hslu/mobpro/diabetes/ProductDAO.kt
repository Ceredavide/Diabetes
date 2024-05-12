package ch.hslu.mobpro.diabetes.ui

import androidx.room.Dao
import androidx.room.Query
import ch.hslu.mobpro.diabetes.Product

@Dao
interface ProductDAO {

    @Query("SELECT * FROM product")
    fun getAll(): List<Product>
    @Query("SELECT * FROM product WHERE uid IN (:productIds)")
    fun loadAllById(productIds: IntArray): List<Product>

    @Query("SELECT * FROM product WHERE uid IN (:names)")
    fun loadAllByName(names: List<String>): List<Product>
}