package com.example.mentalmath.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

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

        Button(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(60.dp),
            onClick = {
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