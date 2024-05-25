package ch.hslu.mobpro.diabetes.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.data.database.GlucoseReading
import ch.hslu.mobpro.diabetes.presentation.common.ActiveUserIndicator
import ch.hslu.mobpro.diabetes.presentation.common.FloatTextField
import ch.hslu.mobpro.diabetes.presentation.common.Graph
import ch.hslu.mobpro.diabetes.presentation.ui.adding.persistGlucoseReading
import ch.hslu.mobpro.diabetes.presentation.viewmodels.GlucoseReadingsViewModel
import java.util.Date
import kotlin.random.Random

@Composable
fun HomeScreen(navController: NavController, glucoseReadingsViewModel: GlucoseReadingsViewModel) {

    var readingInput by remember { mutableStateOf("") }

    Column(
            verticalArrangement = Arrangement.Top
    ) {

        ActiveUserIndicator(navController = navController)

        Graph(
                glucoseReadingsViewModel = glucoseReadingsViewModel,
                height = 500.dp
        )

        Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {

            FloatTextField(
                    value = readingInput,
                    onValueChange = { readingInput = it },
                    label = "Glucose reading(mmol/L)"
            )

            Button(
                    onClick = {
                        val reading = readingInput.toFloatOrNull()
                        if (reading != null && reading > 0.0f) {

                            persistGlucoseReading(glucoseLevel =  reading, glucoseReadingsViewModel = glucoseReadingsViewModel)
                            readingInput = ""
                        }
                    }
            ) {

                Text(text = "ADD GLUCOSE READING")
            }
        }

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

/*
private fun getGlucoseReadings(readings: List<GlucoseReading>) {

    CoroutineScope(Dispatchers.IO).launch {

        var foundReadings =
                MainActivity.glucoseReadingDao.getAllFromUserByUserIndex(
                        PreferenceManager.instance.getActiveUserIndex().toInt()
                )
        if (foundReadings.size == 1) {

            foundReadings = listOf(GlucoseReading(0, 0f, Date()), foundReadings[0])
        }
        else {
            withContext(Dispatchers.Main) {

                readings = foundReadings
            }
        }
    }

}
 */

fun generateData(readings: MutableState<List<GlucoseReading>>) {

    for (i in 1..30) {
        val randomGlucoseLevel = Random.nextFloat() * (25f - 3f) + 3f
        val time =
                Date(System.currentTimeMillis() + i * 1000L) // Incrementing timestamp for demo purposes
        readings.value += GlucoseReading(0, glucoseLevel = randomGlucoseLevel, time = time)
    }

}

@Preview
@Composable
fun HomePreview() {

    val navController = rememberNavController()
    val glucoseReadingsViewModel: GlucoseReadingsViewModel = viewModel()
    HomeScreen(navController = navController, glucoseReadingsViewModel = glucoseReadingsViewModel)
}
