package ch.hslu.mobpro.diabetes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Product::class, GlucoseReading::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    companion object {
        const val NAME = "Product_DB"
    }
    abstract fun productDao(): ProductDAO
    abstract fun glucoseReadingDao(): GlucoseReadingDAO
}