package ch.hslu.mobpro.diabetes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.diabetes.database.Product

@Composable
fun EditProduct() {

    val navController = rememberNavController()
    val productJson = navController.currentBackStackEntry?.arguments?.getString("productJson")
    //val product = Gson().fromJson(productJson, Product::class.java)


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        //var text by remember { mutableStateOf(TextFieldValue(product.name!!)) }

        Box(modifier = Modifier
            .fillMaxWidth()
        ) {
            /*Text(text = product.name!!)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "${product.carbs}/100g")
        */}
    } 
}