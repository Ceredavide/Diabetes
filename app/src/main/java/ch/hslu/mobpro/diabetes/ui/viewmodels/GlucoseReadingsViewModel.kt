package ch.hslu.mobpro.diabetes.ui.viewmodels

import androidx.collection.mutableIntListOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.data.database.GlucoseReading
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

    init {

        deleteOldGlucoseReadings()
        getGlucoseReadingsOfActiveUser()
    }
    fun addGlucoseReading(reading: GlucoseReading) {

        CoroutineScope(Dispatchers.IO).launch {

            MainActivity.glucoseReadingDao.insertGlucoseReading(reading)
            readings = MainActivity.glucoseReadingDao.getAllFromUserByUserIndex(
                    PreferenceManager.instance.getActiveUserIndex().toInt()
            )
        }
    }

    fun deleteOldGlucoseReadings() {

        val maxDays = 90
        val currentLocalDate = LocalDate.now()

        CoroutineScope(Dispatchers.IO).launch {

            val acitveUser = PreferenceManager.instance.getActiveUserIndex().toInt()
            MainActivity.glucoseReadingDao
                .getAllFromUserByUserIndex(acitveUser).filter {

                val date =
                        Date(it.time.time).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                val daysPassed = ChronoUnit.DAYS.between(date, currentLocalDate)
                daysPassed > maxDays
            }.forEach {

                MainActivity.glucoseReadingDao.deleteGlucoseReading(it)
            }

            readings = MainActivity.glucoseReadingDao.getAllFromUserByUserIndex(acitveUser)
        }
    }

    fun getGlucoseReadingsOfActiveUser(): List<GlucoseReading> {

        return readings
    }
}