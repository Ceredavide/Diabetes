package ch.hslu.mobpro.diabetes.data.database

import ch.hslu.mobpro.diabetes.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date

fun deleteOldReadings() {

    CoroutineScope(Dispatchers.IO).launch {

        val readings = MainActivity.glucoseReadingDao.getAll()
        val currentLocalDate = LocalDate.now()
        val maxDays = 90

        readings.filter {

            val date = Date(it.time.time).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val daysPassed =  ChronoUnit.DAYS.between(date, currentLocalDate)
            daysPassed > maxDays
        }.map {

            MainActivity.glucoseReadingDao.deleteGlucoseReading(it)
        }
    }
}