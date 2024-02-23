package com.example.pankytrivia.component

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pankytrivia.model.QuestionsItem
import com.example.pankytrivia.screens.QuestionViewModel
import com.example.pankytrivia.util.AppColors

@Composable
fun QuestionView(questionViewModel: QuestionViewModel) {
    val questions = questionViewModel.data.value.data?.toMutableList()

    val questionIndex = remember {
        mutableStateOf(0)
    }

    if (questionViewModel.data.value.boolean == true){
        CircularProgressIndicator()
    }else{
        val question = try {
            questions?.get(questionIndex.value)
        }catch (ex:Exception){
            null
        }
        if (questions != null){
            QuestionDisplay(questionsItem =  question!!,
                    questionIndex = questionIndex,
                    viewModel = questionViewModel){
                    questionIndex.value = questionIndex.value + 1
                }
         }
    }

}

//@Preview
@Composable
fun QuestionDisplay(
    questionsItem: QuestionsItem,
    questionIndex:MutableState<Int>,
    viewModel: QuestionViewModel,
    onNextClick: (Int) -> Unit = {}
){

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f,10f),0f)
    val choicesState = remember(questionsItem) {
        questionsItem.choices.toMutableList()
    }

    val answerState = remember(questionsItem) {
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(questionsItem) {
        mutableStateOf<Boolean?>(null)
    }

    val updateState:(Int)->Unit = remember(questionsItem) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == questionsItem.answer
        }

    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = AppColors.mDarkPurple
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            if (questionIndex.value >= 3) ShowProgress(score = questionIndex.value)
            QuestionTracker(counter = questionIndex.value, viewModel.getQuestionCount())
            DrawDottedLine(pathEffect)
            Column(
                modifier = Modifier.padding(4.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = questionsItem.question,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxHeight(0.3f),
                    color = AppColors.mOffWhite,
                )

                //choices
                choicesState.forEachIndexed { index, answerText ->
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp, brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColors.mOffDarkPurple,
                                        AppColors.mOffDarkPurple
                                    )
                                ), shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomStartPercent = 50,
                                    bottomEndPercent = 50
                                )
                            )
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically) {

                        RadioButton(selected = (answerState.value == index),
                            onClick = {
                                updateState(index)
                            },
                            modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults
                                .colors(selectedColor = if (correctAnswerState.value == true && index == answerState.value) {
                                    Color.Green.copy(alpha = 0.2f)
                                } else {
                                    Color.Red.copy(alpha = 0.2f)
                                }))
                        val annotedString = buildAnnotatedString {
                            withStyle(style = SpanStyle(
                                fontWeight = FontWeight.Light,
                                color = if (correctAnswerState.value == true &&  index == answerState.value){
                                    Color.Green
                                }else if (correctAnswerState.value == false &&  index == answerState.value){
                                    Color.Red
                                }else{
                                    AppColors.mOffWhite
                                },
                                fontSize = 17.sp
                            )){
                                append(answerText)
                            }
                        }
                        Text(text = annotedString)

                    }
                }
                Button(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    onClick = { onNextClick(questionIndex.value) },
                    colors = ButtonDefaults.buttonColors(
                        AppColors.mLightBlue
                    )
                ) {
                    Text(text = "Next",
                        modifier = Modifier.padding(4.dp),
                        color = AppColors.mOffWhite,
                        fontSize = 17.sp)
                }

            }
        }
    }
}

@Composable
fun QuestionTracker(
    counter:Int = 10,
    outOf:Int = 100
) {
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)){
            withStyle(style = SpanStyle(color = AppColors.mLightGray,
                fontWeight = FontWeight.Bold,
                fontSize = 27.sp)){
                append("Question $counter/")

                withStyle(style = SpanStyle(color = AppColors.mLightGray,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp)){
                    append("$outOf")
                }
            }
        }
    }, modifier = Modifier.padding(20.dp))
}

@Composable
fun DrawDottedLine(pathEffect: PathEffect){
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)){
        drawLine(
            color =  AppColors.mLightGray,
            start = Offset(0f,0f),
            end = Offset(size.width,0f),
            pathEffect = pathEffect
        )
    }
}

@Preview
@Composable
fun ShowProgress(score:Int = 10){


    val progressFactor by remember(score) {
        mutableStateOf(score*0.005f)
    }
    val gradient = Brush.linearGradient(listOf(Color(0xFFF95075),
        Color(0xFFBE6BE5)))

    Row(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp,
                shape = RoundedCornerShape(34.dp),
                brush = Brush.linearGradient(listOf(AppColors.mLightPurple,AppColors.mLightPurple))
            )
            .clip(
                RoundedCornerShape(
                    topEndPercent = 50,
                    topStartPercent = 50,
                    bottomEndPercent = 50,
                    bottomStartPercent = 50
                )
            )
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
        ) {
        Button(
            modifier = Modifier
                .fillMaxWidth(progressFactor)
                .background(gradient),
            onClick = {},
            contentPadding = PaddingValues(1.dp),
            colors = buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            elevation = null,
            enabled = false
            ) {
            Text(text = (score*1).toString(),
                modifier = Modifier.clip(shape = RoundedCornerShape(23.dp))
                    .fillMaxHeight(0.87f)
                    .fillMaxWidth()
                    .padding(6.dp),
                color = AppColors.mOffWhite,
                textAlign = TextAlign.Center)
        }
    }
}
