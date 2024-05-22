package ch.hslu.mobpro.diabetes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.material.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.data.database.GlucoseReading
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import ch.hslu.mobpro.diabetes.ui.components.ActiveUserIndicator
import ch.hslu.mobpro.diabetes.ui.components.Graph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import kotlin.random.Random

@Composable
fun HomeScreen(navController: NavController) {


    val readings = remember { mutableStateOf<List<GlucoseReading>>(emptyList()) }
    getGlucoseReadings(readings)

    Column(
            verticalArrangement = Arrangement.Top
    ) {

        ActiveUserIndicator(navController = navController)

        Graph(
                readings = readings,
                height = 500.dp
        )

        /*
        Button(
                modifier = Modifier,
                onClick = {
                }
        ) {
            Text("Generate Data")
        }

         */
    }
}

private fun getGlucoseReadings(readings: MutableState<List<GlucoseReading>>) {

    CoroutineScope(Dispatchers.IO).launch {

        var foundReadings =
                MainActivity.glucoseReadingDao.getAllFromUserByUserIndex(
                        PreferenceManager.instance.getActiveUserIndex().toInt())
        if (foundReadings.size == 1) {

            foundReadings = listOf(GlucoseReading(0, 0f, Date()), foundReadings[0])
        }
        else {
            withContext(Dispatchers.Main) {

                readings.value = foundReadings
            }
        }
    }

}

fun generateData(readings: MutableState<List<GlucoseReading>>) {

    for (i in 1..30) {
        val randomGlucoseLevel = Random.nextFloat() * (25f - 3f) + 3f
        val time = Date(System.currentTimeMillis() + i * 1000L) // Incrementing timestamp for demo purposes
        readings.value += GlucoseReading(0,  glucoseLevel = randomGlucoseLevel, time = time)
    }

}

@Preview
@Composable
fun HomePreview() {

    val navController = rememberNavController()
    HomeScreen(navController = navController)
}
