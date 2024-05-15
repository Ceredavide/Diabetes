package ch.hslu.mobpro.diabetes.ui.components

import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.hslu.mobpro.diabetes.database.Product

@Composable
fun ProductListItem(product: Product, editable: Boolean) {

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .border(2.dp, MaterialTheme.colors.primary, RectangleShape)
            .clickable {

            }
            .padding(1.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = product.name!!,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(10.dp)
                .clickable {

                }
        )
        Text(
            text ="${product.carbs}/100g"
        )
        if (editable) {
            EditDeleteIcons(
                modifier = Modifier.padding(8.dp) // Adjust padding as needed
            )
        }
    }
}

@Composable
private fun EditDeleteIcons(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit",
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray)
                .padding(4.dp)
                .clickable { }
        )
        Spacer(modifier = Modifier.padding(3.dp))
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Red)
                .padding(4.dp)
                .clickable { }
        )
    }
}