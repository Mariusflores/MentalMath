package com.example.mentalmath.ui.components.selectionscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizLengthSlider(
    sliderPosition: Float,
    onSlide: (Float) -> Unit
) {
    val min = 1f
    val max = 50f

    fun snapFloat(float: Float): Float{
        if(float < 10) return float
        else{
            return 5 * ((float / 5).roundToInt()).toFloat()
        }
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "No. of Questions",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Slider(
            value = sliderPosition,
            onValueChange = { onSlide(snapFloat(it).roundToInt().toFloat()) },

            valueRange = min..max,
            thumb = {
                Box(
                    modifier = Modifier
                        .padding(0.dp)
                        .size(25.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ){
                        Text(
                            text = sliderPosition.toInt().toString(),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )


                }
            }
        )
    }
}