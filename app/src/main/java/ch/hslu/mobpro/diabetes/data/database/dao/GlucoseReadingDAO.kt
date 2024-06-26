package ch.hslu.mobpro.diabetes.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ch.hslu.mobpro.diabetes.data.database.entity.GlucoseReading

@Dao
interface GlucoseReadingDAO {

    @Query("SELECT * FROM glucosereading")
    fun getAll(): List<GlucoseReading>

    @Query("SELECT * FROM glucosereading WHERE user_index LIKE '%' || :userIndex || '%'")
    fun getAllFromUserByUserIndex(userIndex: Int): List<GlucoseReading>

    @Insert
    fun insertGlucoseReading(vararg reading: GlucoseReading)

    @Delete
    fun deleteGlucoseReading(vararg reading: GlucoseReading)
}