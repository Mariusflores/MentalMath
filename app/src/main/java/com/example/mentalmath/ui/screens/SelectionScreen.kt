package com.example.mentalmath.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mentalmath.R
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.ui.components.SelectionScreen.DropdownTemplate
import com.example.mentalmath.ui.components.SelectionScreen.GameModeCard
import com.example.mentalmath.ui.components.SelectionScreen.OperatorCheckBox
import com.example.mentalmath.ui.components.SelectionScreen.QuizLengthSlider
import com.example.mentalmath.ui.viewmodel.QuizViewModel
import com.example.mentalmath.ui.viewmodel.SettingsViewModel

private const val OPERATOR_LABEL = "Choose operators"

@Composable
fun QuizSelectorScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel,
    quizViewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    var selectedDifficulty by remember { mutableStateOf(settingsViewModel.difficulty.value) }
    var quizLength by remember { mutableStateOf(settingsViewModel.quizLength.value) }
    val operatorList = settingsViewModel.operators.value
    var selectedGameMode: GameMode by remember { mutableStateOf(GameMode.Casual) }
    var sliderPosition by remember { mutableFloatStateOf(10f) }


    val difficultyLabel = stringResource(R.string.difficulty_label)

    val gameModeList = listOf<GameMode>(
        GameMode.Casual,
        GameMode.TimeAttack,
        GameMode.Survival,
        GameMode.Practice
    )
    val difficultyList = listOf(
        stringResource(R.string.difficulty_easy),
        stringResource(R.string.difficulty_medium),
        stringResource(R.string.difficulty_hard),

        )
    val operatorResourceList = listOf(
        stringResource(R.string.operator_add),
        stringResource(R.string.operator_sub),
        stringResource(R.string.operator_mul),
        stringResource(R.string.operator_div)
    )


    Column(
        modifier = modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(gameModeList) { gameMode ->
                GameModeCard(
                    selectedGameMode = selectedGameMode,
                    gameMode = gameMode,
                    onSelect = {selectedGameMode = it}
                    )
            }
        }
        Spacer(modifier = modifier.height(16.dp))

        Spacer(modifier = modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            DropdownTemplate(
                selectedDifficulty, difficultyLabel, difficultyList,
                onSelected = { selectedDifficulty = it },
                modifier = Modifier.weight(0.6f)
            )
        }
        Spacer(modifier = modifier.height(16.dp))

        if(selectedGameMode == GameMode.Casual){
            QuizLengthSlider(
                sliderPosition,
                onSlide = {sliderPosition = it}
            )
        }

        Text(
            text = OPERATOR_LABEL,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(operatorResourceList) { operator ->
                OperatorCheckBox(
                    symbol = operator,
                    isChecked = operatorList.contains(operator),
                    settingsViewModel = settingsViewModel,

                    )
            }
        }



        Spacer(modifier = modifier.height(16.dp))

        Button(
            onClick = {
                settingsViewModel.setDifficulty(selectedDifficulty)
                settingsViewModel.setQuizLength(quizLength)
                quizViewModel.startQuiz(
                    settingsViewModel.getModeConfiguration(selectedGameMode)
                )

                navController.navigate("quiz")
            }
        ) {
            Text("Start Quiz")
        }


    }
}

