package ch.hslu.mobpro.diabetes.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EditDeleteButtons(modifier: Modifier,
                      onEdit: () -> Unit,
                      onDelete: () -> Unit,
                      deletable: Boolean) {

    Row(modifier = modifier) {

        IconButton(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray)
                .padding(4.dp),
            onClick = onEdit
        ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
        }

        Spacer(modifier = Modifier.padding(3.dp))

        if (deletable) {

            IconButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Red)
                    .padding(4.dp),
                onClick = onDelete
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }

}

@Preview
@Composable
fun EditDeleteButtonsPreview() {

    val modifier = Modifier.padding(16.dp)
    val onEdit =  {}
    val onDelete =  {}

    EditDeleteButtons(modifier, onEdit, onDelete, true)
}
