package com.fairy.templateapp

import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ViewSwitcher
import androidx.vectordrawable.graphics.drawable.AnimationUtilsCompat

class PinView : LinearLayout {

    var pinLength: Int
        private set

    var enteredDigitsNumber: Int = 0
        private set

    constructor(context: Context?) : super(context) {
        pinLength = 5
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        pinLength = 5
        context?.obtainStyledAttributes(attrs, R.styleable.PinView)?.apply {
            pinLength = getInt(R.styleable.PinView_pinLength, pinLength)
            recycle()
        }
        initView()
    }

    private fun initView() {
        orientation = HORIZONTAL
        clipChildren = false
        clipToPadding = false
        val size = resources.getDimensionPixelSize(R.dimen.pin_digit_size)
        val margin = resources.getDimensionPixelSize(R.dimen.pin_digit_margin)
        val layoutParams = LayoutParams(size, size)
        layoutParams.marginStart = margin
        layoutParams.marginEnd = margin
        for (i in 0 until pinLength) {
            addView(View.inflate(context, R.layout.view_pin_digit, null), layoutParams)
        }
    }

    fun setEnteredDigitsNumber(digits: Int) {
        enteredDigitsNumber = digits
        for (i in 0 until digits) {
            (getChildAt(i) as ViewSwitcher).apply {
                if (displayedChild != 1) displayedChild = 1
            }
        }
        for (i in digits until pinLength) {
            (getChildAt(i) as ViewSwitcher).apply {
                if (displayedChild != 0) displayedChild = 0
            }
        }
    }

    fun showWrongPin(pinInput: EditText) {
        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        var animation: Animation? = null
        for (i in 0 until pinLength) {
            (getChildAt(i) as ViewSwitcher).getChildAt(1).apply {
                background.setTint(Color.RED)
                animation = AnimationUtils.loadAnimation(context, R.anim.shake)
                postDelayed({ startAnimation(animation) }, i * 15L)

            }
        }
//        val animation = AnimationUtils.loadAnimation(context, R.anim.shake)
//        startAnimation(animation)

        animation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                for (i in 0 until pinLength) {
                    (getChildAt(i) as ViewSwitcher).getChildAt(1).apply {
                        background.setTintList(null)
                        animate().translationX(0f).setDuration(55L).start()
//                        translationX = 0f
                    }
                }
                postDelayed({
                    pinInput.text = null
                }, 100)
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
    }

}