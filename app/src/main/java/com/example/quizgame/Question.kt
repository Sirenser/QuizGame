package com.example.quizgame

import java.io.Serializable

class Question(val mTextResId: Int,val mAnswerTrue: Boolean): Serializable {

    var isAnswered = false

    var isCheated = false

}