package com.example.mentalmath.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mentalmath.ui.viewmodel.SettingsViewModel

@Composable
fun OperatorCheckBox(
    symbol: String,
    isChecked: Boolean,
    settingsViewModel: SettingsViewModel,
) {

    Row(
        modifier = Modifier
            .padding(10.dp)
            .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(12.dp))
            .background(color = Color.White)
        //.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 5.dp),
            text = symbol,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold

        )
        //Spacer(Modifier.weight(1f))

        Checkbox(
            checked = isChecked,
            onCheckedChange = { settingsViewModel.toggleOperator(symbol) },
            enabled = true,
            modifier = Modifier.align(Alignment.CenterVertically)

        )

    }
}