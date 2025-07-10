package com.example.mentalmath.ui.components.selectionscreen

import androidx.compose.ui.graphics.Color
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mentalmath.logic.models.gamemode.GameMode

@Composable
fun GameModeCard(
    selectedGameMode: GameMode,
    gameMode: GameMode,
    onSelect: (GameMode) -> Unit
) {

    val isChecked = selectedGameMode == gameMode
    var showInfo by remember { mutableStateOf(false) }

    val opacity by animateFloatAsState(if (isChecked) 1f else 0.7f)
    val scale by animateFloatAsState(if (isChecked) 1.1f else 1f)


    if(showInfo){
        Dialog(onDismissRequest = {showInfo = false}) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = gameMode.modeEmoji,
                            fontSize = 30.sp,
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = gameMode.displayName,
                            fontSize = 20.sp
                        )
                    }
                    Text(
                        text = gameMode.description,
                        fontSize = 15.sp
                    )

                }


            }
    }
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(opacity)
            .scale(scale)
            .padding(10.dp)
            .height(150.dp)
            .clickable {
                onSelect(gameMode)
            },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
        ) {
        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            contentAlignment = Alignment.Center
        ){
            IconToggleButton(
                modifier = Modifier.align(Alignment.TopEnd),
                checked = showInfo,
                onCheckedChange = { showInfo = it },
            ) {
                Icon(
                    modifier = Modifier.scale(0.7f),
                    imageVector = Icons.Filled.QuestionMark,
                    contentDescription = null,
                    tint = Color.Gray

                )

            }
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Text(
                    text = gameMode.displayName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = gameMode.modeEmoji,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        }


    }
}