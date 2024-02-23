package com.example.pankytrivia.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pankytrivia.data.DataOrException
import com.example.pankytrivia.model.QuestionsItem
import com.example.pankytrivia.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val repository: QuestionRepository)
    : ViewModel() {

        val data: MutableState<DataOrException<ArrayList<QuestionsItem>,Boolean,Exception>>
                = mutableStateOf( DataOrException(null,true,Exception()))

    init {
        getAllQuestions()
    }

    private fun getAllQuestions(){
        viewModelScope.launch {
            data.value.boolean = true
            data.value = repository.getAllQuestions()
            if (data.value.data.toString().isNotEmpty()){
                data.value.boolean = false
            }
        }
    }

    fun getQuestionCount(): Int = data.value.data?.toMutableList()?.size!!
}