package com.example.mentalmath.ui.components.SelectionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mentalmath.logic.models.gamemode.GameMode

@Composable
fun GameModeCard(
    selectedGameMode: GameMode,
    gameMode: GameMode,
    onSelect: (GameMode) -> Unit
) {

    val isChecked = selectedGameMode == gameMode

    @Composable
    fun iconByGameMode(): ImageVector{
        return when(gameMode){
            GameMode.Casual -> {
                Icons.Filled.Checklist
            }
            GameMode.TimeAttack -> {
                Icons.Filled.Bolt
            }
            GameMode.Survival -> {
                Icons.Filled.HeartBroken
            }
            GameMode.Practice -> {
                Icons.Filled.Psychology
            }
        }
    }
    @Composable
    fun colorByCheckedState(): Color{
        return if(isChecked) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.background
    }

    Box(
        modifier = Modifier
            .padding(10.dp)
            .background(colorByCheckedState(), RoundedCornerShape(12.dp))
            .border(width = 10.dp, color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(12.dp))
            .height(100.dp)
            .fillMaxWidth(0.75f)
            .clickable{
                onSelect(gameMode)
            },
        contentAlignment = Alignment.Center,


    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = gameMode.displayName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Icon(
                imageVector = iconByGameMode(),
                contentDescription = iconByGameMode().toString()
            )

        }


    }
}