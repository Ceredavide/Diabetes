package ch.hslu.mobpro.diabetes.presentation.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.presentation.common.shared_components.Header
import ch.hslu.mobpro.diabetes.presentation.ui.home.components.Graph
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.GlucoseReadingsViewModel
import ch.hslu.mobpro.diabetes.presentation.navigation.Routes

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    glucoseReadingsViewModel: GlucoseReadingsViewModel = viewModel()
) {
    Scaffold(
        topBar = { Header(Routes.home) }
    ) {
        HomeContent(navController, glucoseReadingsViewModel)
    }
}

@Composable
fun HomeContent(
    navController: NavController,
    glucoseReadingsViewModel: GlucoseReadingsViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        GraphCard(glucoseReadingsViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        AddMealButton(navController)
    }
}

@Composable
fun GraphCard(glucoseReadingsViewModel: GlucoseReadingsViewModel) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Glucose Level Over Time:",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Graph(
                glucoseReadingsViewModel = glucoseReadingsViewModel,
                height = 300.dp
            )
        }
    }
}

@Composable
fun AddMealButton(navController: NavController) {
    Button(
        onClick = { navController.navigate(Routes.composeMeal) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        Text(text = "Add Meal")
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val navController = rememberNavController()
    val glucoseReadingsViewModel: GlucoseReadingsViewModel = viewModel()
    HomeScreen(navController = navController, glucoseReadingsViewModel = glucoseReadingsViewModel)
}
