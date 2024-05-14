package ch.hslu.mobpro.diabetes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ch.hslu.mobpro.diabetes.Product

@Dao
interface ProductDAO {

    @Query("SELECT * FROM product")
    fun getAll(): List<Product>
    @Query("SELECT * FROM product WHERE uid IN (:productIds)")
    fun loadAllById(productIds: IntArray): List<Product>

    @Query("SELECT * FROM product WHERE uid IN (:names)")
    fun loadAllByName(names: List<String>): List<Product>

    @Query("SELECT * FROM product WHERE product_name = :name")
    fun getProductByName(name: String): Product?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(vararg product: Product)

    @Update
    fun updateProduct(vararg product: Product)

    @Delete
    fun deleteProduct(vararg product: Product)
}