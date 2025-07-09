package com.example.mentalmath.ui.components.quizscreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ButtonRow(
    onSubmitClick: () -> Unit,
    onEndClick: () -> Unit
) {

    Row(
        modifier = Modifier.padding(12.dp)
    ) {

        Button(onClick = {
            onSubmitClick()
        })
        {
            Text("Submit")
        }

            Button(onClick = {
                onEndClick()
            }) { Text("Stop Playing") }
    }

}