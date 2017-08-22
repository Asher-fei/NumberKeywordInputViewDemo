package com.fei.nkiv

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val MAX_LENG = 10
    private val mSbvalues = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        number_keyboard.setOnKeyboradListener(object : NumberKeyboardView.OnKeyboradListener{
            override fun onInputKeyEvent(str: String) {
                if (mSbvalues.length<MAX_LENG){
                    mSbvalues.append(str)
                }
                et_Input.setText(mSbvalues.toString())
            }

            override fun onDeleteKeyEent() {
                if (mSbvalues.length>0){
                    mSbvalues.deleteCharAt(mSbvalues.length -1)
                    et_Input.setText(mSbvalues.toString())
                }
            }

        })
    }


}
