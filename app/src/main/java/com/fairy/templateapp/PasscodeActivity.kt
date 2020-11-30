package com.fairy.templateapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.activity_passcode.*

class PasscodeActivity : AppCompatActivity(R.layout.activity_passcode) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pinInput.performClick()

        pinInput.doOnTextChanged { text, start, before, count ->
            pinView.setEnteredDigitsNumber(text?.length ?: 0)
            if (text?.length == pinView.pinLength) {
                pinView.postDelayed({
                    pinView.showWrongPin(pinInput)
                }, 100)
            }
        }

    }
}