package com.example.quizgame

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private var mQuestionBank: Array<Question> = initQuestionBank()
    private val KEY_QBANK = "mQuestionBank"


    private var mCurrentIndex = 0
    private val KEY_INDEX = "mCurrentIndex"

    private var question = mQuestionBank[mCurrentIndex].mTextResId

    private var mScore = 0
    private val KEY_SCORE = "mScore"

    private var cheatForResult: ActivityResultLauncher<Intent>? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCurrentIndex = savedInstanceState?.getInt(KEY_INDEX) ?: 0
        mScore = savedInstanceState?.getInt(KEY_SCORE) ?: 0
        question = mQuestionBank[mCurrentIndex].mTextResId
        mScore = savedInstanceState?.getInt(KEY_SCORE) ?: 0
        mQuestionBank = (savedInstanceState?.getSerializable(KEY_QBANK)
            ?: initQuestionBank()) as Array<Question>

        showScoreAndQuestion()


        cheatForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val isCheatedI = it.data?.getBooleanExtra("cheatBool", false)
                    mQuestionBank[mCurrentIndex].isCheated = isCheatedI.toString().toBoolean()
                }
            }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, mCurrentIndex)
        outState.putInt(KEY_SCORE, mScore)
        outState.putSerializable(KEY_QBANK, mQuestionBank)
    }

    fun moveQuestionNext(v: View) {
        mCurrentIndex = if (mCurrentIndex == (mQuestionBank.size - 1))
            0
        else
            (mCurrentIndex + 1) % mQuestionBank.size

        question = mQuestionBank[mCurrentIndex].mTextResId
        findViewById<TextView>(R.id.question_text_view).setText(question)
    }

    fun moveQuestionBack(v: View) {
        mCurrentIndex = if (mCurrentIndex == 0)
            mQuestionBank.size - 1
        else
            (mCurrentIndex - 1) % mQuestionBank.size

        question = mQuestionBank[mCurrentIndex].mTextResId
        findViewById<TextView>(R.id.question_text_view).setText(question)
    }

    fun checkTrueAnswer(v: View) {
        checkAnswer(true)
    }

    fun checkFalseAnswer(v: View) {
        checkAnswer(false)
    }

    @SuppressLint("SetTextI18n")
    private fun checkAnswer(userPressedTrue: Boolean) {
        val mScoreTextView = findViewById<TextView>(R.id.score_text_view)
        val answerIsTrue = mQuestionBank[mCurrentIndex].mAnswerTrue
        var messageResId = 0


        if (mQuestionBank[mCurrentIndex].isAnswered) {

            messageResId = R.string.isAnswered_toast

        } else if (mQuestionBank[mCurrentIndex].isCheated) {
            messageResId = R.string.cheat_toast

        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast
                mScoreTextView.text = "Score = ${++mScore}"
                mQuestionBank[mCurrentIndex].isAnswered = true
            } else {
                messageResId = R.string.incorrect_toast
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

    }

    fun onClickCheatButton(v: View) {
        val i = Intent(this@MainActivity, CheatActivity::class.java)
        i.putExtra("trueAnswer", mQuestionBank[mCurrentIndex].mAnswerTrue)
        cheatForResult?.launch(i)
    }

    private fun initQuestionBank(): Array<Question> {

        return arrayOf(
            Question(R.string.question_africa, false),
            Question(R.string.question_americas, true),
            Question(R.string.question_asia, true),
            Question(R.string.question_mideast, false),
            Question(R.string.question_world, false),
        )
    }

    @SuppressLint("SetTextI18n")
    private fun showScoreAndQuestion() {
        val mScoreTextView = findViewById<TextView>(R.id.score_text_view)
        mScoreTextView.text = "Score = $mScore"

        val mQuestionTextView = findViewById<TextView>(R.id.question_text_view)
        question = mQuestionBank[mCurrentIndex].mTextResId
        mQuestionTextView.setText(question)

    }

}