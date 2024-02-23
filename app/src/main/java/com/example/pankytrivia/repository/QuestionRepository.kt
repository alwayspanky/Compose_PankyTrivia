package com.example.pankytrivia.repository

import com.example.pankytrivia.data.DataOrException
import com.example.pankytrivia.model.Questions
import com.example.pankytrivia.model.QuestionsItem
import com.example.pankytrivia.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi) {

    private val allQuestionList = DataOrException<ArrayList<QuestionsItem>, Boolean, Exception>()

    suspend fun getAllQuestions():DataOrException<ArrayList<QuestionsItem>, Boolean, Exception>{

        try {
            allQuestionList.boolean = true
            allQuestionList.data = api.getQuestions()
            if (allQuestionList.data.toString().isNotEmpty()) allQuestionList.boolean = false
        }catch (exception:Exception){
             allQuestionList.e = exception
        }
        return allQuestionList
    }
}