package com.example.mentalmath.ui.components.SelectionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(10.dp)
            .border(width = 0.5.dp, color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()

    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { settingsViewModel.toggleOperator(symbol) },
            enabled = true,
            modifier = Modifier.align(Alignment.CenterVertically)

        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 5.dp),
            text = symbol,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold

        )

    }
}