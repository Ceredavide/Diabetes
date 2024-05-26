package ch.hslu.mobpro.diabetes.presentation.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastMapIndexed
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.hslu.mobpro.diabetes.data.database.entity.GlucoseReading
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.GlucoseReadingsViewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.*
import java.time.Instant
import java.time.ZoneId
import java.util.*

@Composable
fun Graph(glucoseReadingsViewModel: GlucoseReadingsViewModel, height: Dp) {
    val readings = glucoseReadingsViewModel.getGlucoseReadingsOfActiveUser()
    val pointsState = remember { mutableStateOf<List<Point>>(emptyList()) }
    val datesState = remember { mutableStateOf<List<String>>(emptyList()) }
    val (lowestReading, highestReading) = convertReadings(readings, pointsState, datesState)

    if (pointsState.value.isEmpty()) {
        NoDataAvailable()
        return
    }

    val lineChartData = createLineChartData(pointsState.value, datesState.value, lowestReading, highestReading)
    RenderGraph(lineChartData, height)
}

@Composable
fun NoDataAvailable() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "NO DATA AVAILABLE", fontSize = 36.sp)
        Text(text = "GRAPH OF READINGS WILL APPEAR HERE", fontSize = 34.sp)
    }
}

@Composable
fun createLineChartData(points: List<Point>, dates: List<String>, lowestReading: Float, highestReading: Float): LineChartData {
    val steps = 5

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .backgroundColor(Color.White)
        .steps(points.size - 1)
        .labelData { i -> dates[i] }
        .labelAndAxisLinePadding(15.dp)
        .axisLineColor(MaterialTheme.colors.secondary)
        .axisLabelColor(Color.Black)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(Color.White)
        .labelAndAxisLinePadding(20.dp)
        .shouldDrawAxisLineTillEnd(true)
        .labelData { (lowestReading + it / steps.toFloat() * (highestReading - lowestReading)).toString() }
        .axisLineColor(MaterialTheme.colors.secondary)
        .axisLabelColor(Color.Black)
        .build()

    return LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = points,
                    LineStyle(
                        color = MaterialTheme.colors.secondary,
                        lineType = LineType.SmoothCurve(isDotted = false)
                    ),
                    IntersectionPoint(
                        color = MaterialTheme.colors.secondary
                    ),
                    SelectionHighlightPoint(color = MaterialTheme.colors.primary),
                    ShadowUnderLine(
                        color = MaterialTheme.colors.primary,
                        alpha = 0.5f,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colors.secondary,
                                Color.Transparent
                            )
                        )
                    ),
                    SelectionHighlightPopUp()
                )
            )
        ),
        backgroundColor = Color.White,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = Color.LightGray),
        isZoomAllowed = true,
    )
}

@Composable
fun RenderGraph(lineChartData: LineChartData, height: Dp) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "mmol/L", modifier = Modifier.padding(16.dp))
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            lineChartData = lineChartData
        )
    }
}

private fun convertReadings(
    readings: List<GlucoseReading>,
    outPoints: MutableState<List<Point>>,
    outDates: MutableState<List<String>>
): Pair<Float, Float> {
    var highestReading = 0.0f
    var lowestReading = Float.MAX_VALUE

    outPoints.value = readings.fastMapIndexed { i, reading ->
        highestReading = maxOf(highestReading, reading.glucoseLevel)
        lowestReading = minOf(lowestReading, reading.glucoseLevel)
        Point(i.toFloat(), reading.glucoseLevel)
    }

    if (lowestReading == highestReading) {
        lowestReading = 0.0f
    }

    outDates.value = readings.map {
        val localTime = Instant.ofEpochMilli(it.time.time)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        "${localTime.hour}:${localTime.minute}"
    }

    return Pair(lowestReading, highestReading)
}

@Preview
@Composable
fun GraphPreview() {
    val readings = listOf(
        GlucoseReading(0, 25.0f, Date()),
        GlucoseReading(0, 20.0f, Date()),
        GlucoseReading(0, 12.4f, Date()),
        GlucoseReading(0, 15.0f, Date()),
        GlucoseReading(0, 10.0f, Date()),
        GlucoseReading(0, 5.0f, Date())
    )
    val glucoseReadingsViewModel: GlucoseReadingsViewModel = viewModel()
    Graph(
        glucoseReadingsViewModel = glucoseReadingsViewModel,
        height = 300.dp
    )
}
