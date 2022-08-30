package com.example.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class CheatActivity : AppCompatActivity() {


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isCheated", isCheated)
    }

    var isCheated = false
    var answerIsTrue = false


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        isCheated = savedInstanceState?.getBoolean("isCheated") ?: false
        answerIsTrue = intent.getBooleanExtra("trueAnswer", true)



        val cheatTextView = findViewById<TextView>(R.id.cheat_textview)

        if (isCheated) {
            if (answerIsTrue) cheatTextView.setText(R.string.true_button)
            else cheatTextView.setText(R.string.false_button)
        }

    }




    fun onClickBackButton(v: View) {
        val i = Intent()
        i.putExtra("cheatBool", isCheated)
        setResult(RESULT_OK, i)
        finish()
    }

    fun onClickShowButton(v: View) {

        val cheatTextView = findViewById<TextView>(R.id.cheat_textview)
        if (answerIsTrue) cheatTextView.setText(R.string.true_button)
        else cheatTextView.setText(R.string.false_button)

        isCheated = true
    }


}