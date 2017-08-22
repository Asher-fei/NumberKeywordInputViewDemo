package com.fei.nkiv

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.widget.EditText

/**
 * Created by cyc on 2017/7/4.
 * 可支持明文和密文显示（isPasswordType = true:密文显示，否则明文显示）
 * 可支持有无边框显示（isNeedRim= true: 有边框，否则无边框）
 */
class PassWordInputView : EditText {

    private var passwordLength =0
    private var passwordColor =0xcccccc

    private var passwordWidth =0.0f
    private var passwordRadius =0.0f

    private var borderColor = Color.TRANSPARENT  //边框颜色
    private var borderWidth = 0.0f   //边框宽度大小
    private var borderRadius = 0.0f  //圆角
    private var contextBackgroundColor =0  //内容区的背景
    private var splitLineWidth = 0.5f
    private var splitLineColor = 0
    private var splitLine_Margin =0.0f

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)  //边框和内容区画笔
    private val mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    private var isNeedRim = false //是否需要边框
    private var isPassword= true  //是否为密码类型

    private var textLengt =0
    private var context:String?=null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.PasswordInputView,0,0)
        borderColor = a.getColor(R.styleable.PasswordInputView_pivBorderColor, borderColor)
        borderWidth = a.getDimension(R.styleable.PasswordInputView_pivBorderWidth, borderWidth)
        borderRadius = a.getDimension(R.styleable.PasswordInputView_pivBorderRadius, borderRadius)

        passwordLength = a.getInt(R.styleable.PasswordInputView_pivPasswordLength, passwordLength)
        passwordColor = a.getColor(R.styleable.PasswordInputView_pivPasswordColor, passwordColor)
        passwordWidth = a.getDimension(R.styleable.PasswordInputView_pivPasswordWidth, passwordWidth)
        passwordRadius = a.getDimension(R.styleable.PasswordInputView_pivPasswordRadius, passwordRadius)
        contextBackgroundColor = a.getColor(R.styleable.PasswordInputView_content_BackgroundColor, contextBackgroundColor)
        splitLineWidth = a.getDimension(R.styleable.PasswordInputView_lineWidth,splitLineWidth)
        splitLineColor = a.getColor(R.styleable.PasswordInputView_lineColor,splitLineColor)
        splitLine_Margin = a.getDimension(R.styleable.PasswordInputView_line_Margin,splitLine_Margin)
        isNeedRim = a.getBoolean(R.styleable.PasswordInputView_isNeedRim,isNeedRim)
        isPassword = a.getBoolean(R.styleable.PasswordInputView_isPasswordType,isPassword)

        a.recycle()

        borderPaint.strokeWidth = borderWidth
        borderPaint.color = borderColor
        mTextPaint.color = currentTextColor
        mTextPaint.textSize = textSize
    }


    override fun onDraw(canvas: Canvas?) {

        if (isNeedRim){
            /**绘制外边框**/
            val  borderRect = RectF(0.0f,0.0f,width.toFloat(),height.toFloat())
            borderPaint.color = borderColor
            canvas!!.drawRoundRect(borderRect,borderRadius,borderRadius,borderPaint)

            /**内容区**/
            val rectIn = RectF(borderRect.left + borderWidth,borderRect.top +borderWidth,borderRect.right - borderWidth,borderRect.bottom - borderWidth)
            borderPaint.color = contextBackgroundColor
            canvas.drawRoundRect(rectIn,borderRadius,borderRadius,borderPaint)
        }

        /**分割线**/
        borderPaint.color= splitLineColor
        borderPaint.strokeWidth = splitLineWidth
        val splitLineHeight = height-splitLine_Margin
        (1..passwordLength-1)
                .map { (width * it /passwordLength).toFloat() }
                .forEach { canvas!!.drawLine(it,splitLine_Margin, it,splitLineHeight,borderPaint) }


        /**密码**/

        val cy = height /2
        val half = width /passwordLength /2
        borderPaint.color = passwordColor
        borderPaint.strokeWidth = passwordWidth
        borderPaint.style = Paint.Style.FILL
        val baseY = cy -(mTextPaint.descent()+mTextPaint.ascent())/2
        for (i in 0..textLengt -1){
            val cx = width*i/passwordLength+half
            if (isPassword){
                canvas!!.drawCircle(cx.toFloat(), cy.toFloat(), passwordWidth, borderPaint)
            }else{
                val textWidth = mTextPaint.measureText(context!![i].toString())
                canvas!!.drawText(context!![i].toString(),cx - textWidth/2,baseY,mTextPaint)
            }

        }
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        this.textLengt = text.toString().length
        this.context = text.toString()
        invalidate()
    }
}