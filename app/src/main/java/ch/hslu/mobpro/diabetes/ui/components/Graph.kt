package ch.hslu.mobpro.diabetes.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastMapIndexed
import ch.hslu.mobpro.diabetes.data.database.GlucoseReading
import ch.hslu.mobpro.diabetes.ui.screens.generateData
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import okhttp3.internal.wait
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun Graph(readings: MutableState<List<GlucoseReading>>, modifier: Modifier) {

    val points = remember { mutableStateOf<List<Point>>(emptyList()) }
    val dates = remember { mutableStateOf<List<String>>(emptyList()) }
    convertReadings(readings, points, dates)
    if (points.value.isEmpty()) {

        Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
        ) {

            Text(text = "NO DATA AVAILABLE", fontSize = 36.sp)
            Text(text = "GRAPH OF READINGS WILL APPEAR HERE", fontSize = 34.sp)
        }

        return
    }

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .backgroundColor(Color.White)
        .steps(points.value.size - 1)
        .labelData { i -> dates.value[i] }
        .labelAndAxisLinePadding(15.dp)
        .axisLineColor(MaterialTheme.colors.secondary)
        .axisLabelColor(Color.Black)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(5)
        .backgroundColor(Color.White)
        .labelAndAxisLinePadding(20.dp)
        .labelData {
            val yScale = 25 / 5
            (it * yScale).toString()
        }
        .axisLineColor(MaterialTheme.colors.secondary)
        .axisLabelColor(Color.Black)
        .build()

    val lineChartData = LineChartData(
            linePlotData = LinePlotData(
                    lines = listOf(
                            Line(
                                    dataPoints = points.value,
                                    LineStyle(
                                            color = MaterialTheme.colors.secondary,
                                            lineType = LineType.SmoothCurve(isDotted = false)
                                    ),
                                    IntersectionPoint(
                                            color = MaterialTheme.colors.secondary
                                    ),
                                    SelectionHighlightPoint(color = MaterialTheme.colors.secondary),
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

    LineChart(
            modifier = modifier,
            lineChartData = lineChartData
    )
}

private fun convertReadings(
        readings: MutableState<List<GlucoseReading>>,
        outPoints: MutableState<List<Point>>,
        outDates: MutableState<List<String>>
) {

    outPoints.value = readings.value.fastMapIndexed { i, r ->
        Point(i.toFloat(), r.glucoseLevel)
    }
    readings.value.forEach() {
        val localTime = Instant.ofEpochMilli(it.time.time)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        outDates.value += "${localTime.hour}:${localTime.minute}:${localTime.second}"
    }
}

@Preview
@Composable
fun GraphPreview() {

    val readings = remember { mutableStateOf<List<GlucoseReading>>(emptyList()) }
    generateData(readings)
    Graph(
            readings = readings,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
    )
}