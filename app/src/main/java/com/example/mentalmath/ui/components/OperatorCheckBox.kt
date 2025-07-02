package com.example.mentalmath.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun OperatorCheckBox(
    symbol: String,

    modifier: Modifier
) {


    Row (
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = symbol,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Checkbox(

            TODO("Not yet implemented"),
            onCheckedChange = TODO(),
            modifier = TODO(),
            enabled = TODO(),
            colors = TODO(),
            interactionSource = TODO()
        )
        
    }
}