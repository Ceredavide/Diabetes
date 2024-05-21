package ch.hslu.mobpro.diabetes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.material.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.hslu.mobpro.diabetes.MainActivity
import ch.hslu.mobpro.diabetes.data.database.GlucoseReading
import ch.hslu.mobpro.diabetes.data.pref.PreferenceManager
import ch.hslu.mobpro.diabetes.ui.components.ActiveUserIndicator
import ch.hslu.mobpro.diabetes.ui.components.Graph
import co.yml.charts.common.model.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.random.Random

@Composable
fun HomeScreen() {

    var readings by remember { mutableStateOf<List<GlucoseReading>>(emptyList()) }
    val points = remember { mutableStateOf<List<Point>>(emptyList()) }

    Column(
            verticalArrangement = Arrangement.Top
    ) {

        ActiveUserIndicator()

        //val points = listOf( Point(0.0f, 40.0f), Point(1.0f, 90.0f), Point(2.0f, 0.0f), Point(3.0f, 60.0f), Point(4.0f, 10.0f) )

        Graph(
                //points = points.value,
                readings = readings,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
        )

        Button(
                modifier = Modifier,
                onClick = {
                    readings = generateData()
                }
        ) {
            Text("Generate Data")
        }
    }
}

private fun generateData(): List<GlucoseReading> {

    var readings = emptyList<GlucoseReading>()
    for (i in 1..30) {
        val randomGlucoseLevel = Random.nextFloat() * (25f - 3f) + 3f
        val time = Date(System.currentTimeMillis() + i * 1000L) // Incrementing timestamp for demo purposes
        readings += GlucoseReading(0,  glucoseLevel = randomGlucoseLevel, time = time)
    }

    return readings
}

@Preview
@Composable
fun HomePreview() {

    HomeScreen()
}
