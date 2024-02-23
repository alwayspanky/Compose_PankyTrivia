package com.example.pankytrivia.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pankytrivia.component.QuestionView

@Composable
fun TriviaHome(questionViewModel: QuestionViewModel = hiltViewModel()){

    QuestionView(questionViewModel)
}