package ch.hslu.mobpro.diabetes.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Product::class), version = 1)
abstract class AppDatabase: RoomDatabase() {

    companion object {
        const val NAME = "Product_DB"
    }
    abstract fun productDao(): ProductDAO
}