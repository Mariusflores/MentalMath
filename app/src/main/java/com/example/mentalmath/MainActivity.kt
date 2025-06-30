package com.example.mentalmath

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mentalmath.ui.theme.MentalMathTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MentalMathTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MathProblemView(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MathProblemView( modifier: Modifier = Modifier) {
    var problem by remember { mutableStateOf(MathProblemGenerator.generateAdditionProblem()) }
    var answer by remember{ mutableStateOf("") }
    var feedback by remember{ mutableStateOf("") }
    var correctAnswer by remember { mutableStateOf(false) }

    Column  (
        modifier = modifier
            .padding(24.dp)
            .fillMaxSize()
    ){
        Text(
            text = problem.question,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold

        )
        OutlinedTextField(
            value = answer,
            onValueChange = {answer = it},
            label = { Text("Write answer here.") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row (
            modifier = Modifier.padding(12.dp)
        ) {

        Button(onClick = {
             correctAnswer = answer.toInt() == problem.correctAnswer
            feedback = if (correctAnswer){
                "Correct!"
            }else{
                "Wrong, Try again."
            }
        })
        {
            Text("Submit")
        }
            if(correctAnswer){
                Button(onClick = {
                    problem = MathProblemGenerator.generateAdditionProblem()
                    feedback = ""
                    correctAnswer = false
                    answer = ""
                }) { Text("New problem") }
            }
            }
        if (feedback.isNotEmpty()){Text(feedback)}
    }

}

@Preview(showBackground = true)
@Composable
fun MathProblemPreview() {
    MentalMathTheme {
        MathProblemView()
    }
}