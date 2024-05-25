package ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels

import androidx.lifecycle.ViewModel
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.data.database.GlucoseReading
import ch.hslu.mobpro.diabetes.data.database.GlucoseReadingDAO
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date

class GlucoseReadingsViewModel : ViewModel() {

    private var readings = emptyList<GlucoseReading>()
     var glucoseReadingDao: GlucoseReadingDAO
    init {

        glucoseReadingDao = MainActivity.db.glucoseReadingDao()
        deleteOldGlucoseReadings()
        getGlucoseReadingsOfActiveUser()
    }
    fun addGlucoseReading(reading: GlucoseReading) {

        CoroutineScope(Dispatchers.IO).launch {

            glucoseReadingDao.insertGlucoseReading(reading)
            readings = glucoseReadingDao.getAllFromUserByUserIndex(
                    PreferenceManager.instance.getActiveUserIndex().toInt()
            )
        }
    }

    fun deleteOldGlucoseReadings() {

        val maxDays = 90
        val currentLocalDate = LocalDate.now()

        CoroutineScope(Dispatchers.IO).launch {

            val acitveUser = PreferenceManager.instance.getActiveUserIndex().toInt()
            glucoseReadingDao
                .getAllFromUserByUserIndex(acitveUser).filter {

                val date =
                        Date(it.time.time).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                val daysPassed = ChronoUnit.DAYS.between(date, currentLocalDate)
                daysPassed > maxDays
            }.forEach {

                glucoseReadingDao.deleteGlucoseReading(it)
            }

            readings = glucoseReadingDao.getAllFromUserByUserIndex(acitveUser)
        }
    }

    fun getGlucoseReadingsOfActiveUser(): List<GlucoseReading> {

        return readings
    }

    fun updateActiveUser() {

        CoroutineScope(Dispatchers.IO).launch {

            readings = glucoseReadingDao
                .getAllFromUserByUserIndex(
                        PreferenceManager.instance.getActiveUserIndex().toInt()
                )
        }
    }
}