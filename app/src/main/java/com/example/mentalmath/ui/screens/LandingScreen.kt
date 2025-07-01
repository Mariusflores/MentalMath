package com.example.mentalmath.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mentalmath.R

@Composable
fun LandingScreen(
    navController: NavController,
    modifier: Modifier = Modifier
){

    Column (
        modifier = modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Column(
            modifier = modifier
                .padding(24.dp)
        )
        {

            Image(
                painter = painterResource(id = R.drawable.icons8_math_40),
                contentDescription = "App Logo",
                modifier = Modifier
                    .padding(16.dp)
                    .size(50.dp)
            )
            Text(
                text = "MentalMath",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

        }

        Spacer(
            modifier = modifier
                .height(24.dp)
        )

        Button(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(60.dp),
            onClick = {
                navController.popBackStack("landing", inclusive = false)
                navController.navigate("quiz")

            }){
            Text("Play")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row (
            modifier = Modifier.padding(12.dp)
        ){

            Button(onClick = {
               /*
                   Some functionality, maybe set difficulty? not a priority
               */
            }){
                Text("TBD")
            }

            Button(onClick = {
                /*
                Some functionality? maybe choose types of problems(addition,subtraction, mixed not a priority
                 */
            }){
                Text("TBD")
            }

        }
    }

}