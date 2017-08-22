package com.fei.nkiv

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.util.AttributeSet

/**
 * Created by cyc on 2017/7/24.
 */
class NumberKeyboardView : KeyboardView,KeyboardView.OnKeyboardActionListener{

    private val KEYCODE_EMPTY = -10
    private var mDeleteBackgroundColor = 0
    private var mDeleteDrawRect: Rect?=null
    private var mDeleteDrawable:Drawable?=null

    private var mOnKeyborardListener:OnKeyboradListener?=null

    constructor(context: Context,attrs: AttributeSet):this(context, attrs,0)

    constructor(context: Context,attrs: AttributeSet,defStyleAttr: Int): super(context, attrs, defStyleAttr){
        init(context,attrs,defStyleAttr)
    }

    private fun init(context: Context,attrs: AttributeSet,defStyleAttr: Int){
        val a = context.obtainStyledAttributes(attrs,
                R.styleable.PasswordKeyboardView, defStyleAttr, 0)
        mDeleteDrawable = a.getDrawable(
                R.styleable.PasswordKeyboardView_pkvDeleteDrawable)
        mDeleteBackgroundColor = a.getColor(
                R.styleable.PasswordKeyboardView_pkvDeleteBackgroundColor,
                Color.TRANSPARENT)
        a.recycle()

        val mKeyborad = Keyboard(context,R.xml.keyboard_number_password)
        keyboard = mKeyborad
        isEnabled = true
        isPreviewEnabled = false
        onKeyboardActionListener = this
    }

    private fun drawKeyBackground(key: Keyboard.Key,canvas: Canvas,color:Int){
        val drawable = ColorDrawable(color)
        drawable.setBounds(key.x,key.y,key.x+key.width,key.y + key.height)
        drawable.draw(canvas)
    }

    private fun drawDeteleButton(key: Keyboard.Key,canvas: Canvas){
        if (mDeleteDrawable == null) return

        if (mDeleteDrawRect == null || mDeleteDrawRect!!.isEmpty){
            val intrinsicWidth = mDeleteDrawable!!.intrinsicWidth
            val intrinsicHeight = mDeleteDrawable!!.intrinsicHeight
            var drawWidth = intrinsicWidth
            var drawHeight = intrinsicHeight

            if (drawWidth > key.width){
                drawWidth = key.width
                drawHeight = drawWidth * intrinsicHeight /intrinsicWidth
            }

            if (drawHeight > key.height){
                drawHeight = key.height
                drawWidth = drawHeight * intrinsicWidth /intrinsicHeight
            }

            val left = key.x + (key.width - drawWidth)/2
            val top = key.y + (key.height - drawHeight) /2
            mDeleteDrawRect = Rect(left,top,left+ drawWidth,top+drawHeight)
        }

        if (mDeleteDrawRect !=null && !mDeleteDrawRect!!.isEmpty){
            mDeleteDrawable!!.setBounds(mDeleteDrawRect!!.left,mDeleteDrawRect!!.top,
                    mDeleteDrawRect!!.right,mDeleteDrawRect!!.bottom)
            mDeleteDrawable!!.draw(canvas)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val keys = keyboard.keys
        for (key in keys){
            if (key.codes[0] == KEYCODE_EMPTY){
                drawKeyBackground(key,canvas!!,mDeleteBackgroundColor)
            }else if (key.codes[0] == Keyboard.KEYCODE_DELETE){
                drawKeyBackground(key,canvas!!,mDeleteBackgroundColor)
                drawDeteleButton(key,canvas!!)
            }
        }
    }

    override fun onPress(primaryCode: Int) {}

    override fun onRelease(primaryCode: Int) {}

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        if(primaryCode == Keyboard.KEYCODE_DELETE){
           mOnKeyborardListener?.onDeleteKeyEent()
        }else if (primaryCode != KEYCODE_EMPTY){
            mOnKeyborardListener?.onInputKeyEvent(Character.toString(primaryCode.toChar()))
        }
    }

    override fun onText(text: CharSequence?) {}

    fun setOnKeyboradListener(listener: OnKeyboradListener?){
        this.mOnKeyborardListener = listener
    }

    override fun swipeLeft() {}

    override fun swipeUp() {}

    override fun swipeRight() {}

    override fun swipeDown() {}

    interface  OnKeyboradListener{
        fun onInputKeyEvent(str:String)
        fun onDeleteKeyEent()
    }
}