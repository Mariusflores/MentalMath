package com.example.mentalmath.ui.components.selectionscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mentalmath.ui.viewmodel.SettingsViewModel

@Composable
fun ToggleOperator(
    symbol: String,
    isChecked: Boolean,
    settingsViewModel: SettingsViewModel,
) {

    fun getOperatorLabel(): String{
        return when (symbol){
            "+" -> "Addition"
            "-" -> "Subtraction"
            "×" -> "Multiplication"
            "÷" -> "Division"
            else -> ""
        }
    }

    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .padding(10.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()

    ) {
        Switch(
            modifier = Modifier.align(Alignment.CenterVertically)
                .padding(end = 5.dp)
                .scale(0.75f),
            checked = isChecked,
            onCheckedChange = { settingsViewModel.toggleOperator(symbol) },
            thumbContent = if (isChecked){
                {
                    Text(symbol)
                }
            }else null

        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = getOperatorLabel(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold

        )



    }
}