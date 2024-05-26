package ch.hslu.mobpro.diabetes.presentation.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.presentation.common.shared_components.ActiveUserIndicator
import ch.hslu.mobpro.diabetes.presentation.ui.home.components.Graph
import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.GlucoseReadingsViewModel
import ch.hslu.mobpro.diabetes.presentation.navigation.Routes

@Composable
fun HomeScreen(navController: NavController, glucoseReadingsViewModel: GlucoseReadingsViewModel) {

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
            Button(
                onClick = {
                    navController.navigate(Routes.composeMeal)
                }
            ) {
                Text(text = "Add Meal")
            }
        }
    }
}


@Preview
@Composable
fun HomePreview() {

    val navController = rememberNavController()
    val glucoseReadingsViewModel: GlucoseReadingsViewModel = viewModel()
    HomeScreen(navController = navController, glucoseReadingsViewModel = glucoseReadingsViewModel)
}
