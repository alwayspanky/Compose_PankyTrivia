package com.example.pankytrivia.network

import com.example.pankytrivia.model.Questions
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface QuestionApi {

    @GET("world.json")
    suspend fun getQuestions(): Questions
}