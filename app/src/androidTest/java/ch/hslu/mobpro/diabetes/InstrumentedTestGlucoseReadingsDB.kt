package ch.hslu.mobpro.diabetes

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.hslu.mobpro.diabetes.data.database.AppDatabase
import ch.hslu.mobpro.diabetes.data.database.GlucoseReading
import ch.hslu.mobpro.diabetes.data.database.GlucoseReadingDAO
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class InstrumentedTestGlucoseReadingsDB {
    private lateinit var db: AppDatabase
    private lateinit var glucoseReadingDao: GlucoseReadingDAO
    @Before
    fun createDB() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        glucoseReadingDao = db.glucoseReadingDao()
    }

    @Test
    fun testInsertGlucoseReading() {

        val currentTime = Date()
        val glucoseReading = GlucoseReading(
                userIndex = 0,
                glucoseLevel = 5.0f,
                time =  currentTime)

        glucoseReadingDao.insertGlucoseReading(glucoseReading)

        val result = glucoseReadingDao.getAll()

        assertEquals(1, result.size)
        assertEquals(0, result[0].userIndex)
        assertEquals(5.0f, result[0].glucoseLevel)
        assertEquals(currentTime, result[0].time)
    }

    @Test
    fun textGetAllFromUser() {

        val currentTime = Date()
        val glucoseReading = GlucoseReading(
                userIndex = 0,
                glucoseLevel = 5.0f,
                time =  currentTime)
        val glucoseReading1 = GlucoseReading(
                userIndex = 1,
                glucoseLevel = 5.0f,
                time =  currentTime)


        glucoseReadingDao.insertGlucoseReading(glucoseReading)
        glucoseReadingDao.insertGlucoseReading(glucoseReading1)

        val result = glucoseReadingDao.getAllFromUserByUserIndex(0)

        assertEquals(1, result.size)
    }

    @Test
    fun testGetAllGlucoseReadings() {

        val currentTime = Date()
        val glucoseReading = GlucoseReading(
                userIndex = 0,
                glucoseLevel = 5.0f,
                time =  currentTime)
        val glucoseReading1 = GlucoseReading(
                userIndex = 1,
                glucoseLevel = 5.0f,
                time =  currentTime)


        glucoseReadingDao.insertGlucoseReading(glucoseReading)
        glucoseReadingDao.insertGlucoseReading(glucoseReading1)

        val result = glucoseReadingDao.getAll()

        assertEquals(2, result.size)
    }
}