package com.example.macrostracker.ui.diary.foodList.barcodeScanner

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Point
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable

class ScannerReticle(): Drawable() {


    private val boxPaint: Paint = Paint().apply {
        color = Color.rgb(55,178,220)
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val scrimPaint: Paint = Paint().apply {
        color = Color.rgb(0,0,0)
        alpha = 99
    }

    private val eraserPaint: Paint = Paint().apply {
        strokeWidth = boxPaint.strokeWidth
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }


    override fun draw(canvas: Canvas) {

        val canvasCenter = PointF(canvas.width/2f, canvas.height/2f)
        val rectW = 400f
        val rectH = 400f
        val rect = RectF(canvasCenter.x - rectW,canvasCenter.y -rectH,
            canvasCenter.x + rectW,canvasCenter.y + rectH)

        canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), scrimPaint)
        eraserPaint.style = Paint.Style.FILL
        canvas.drawRect(rect, eraserPaint)
        eraserPaint.style = Paint.Style.STROKE
        canvas.drawRect(rect, eraserPaint)

        canvas.drawRect(rect, boxPaint)


    }

    override fun setAlpha(p0: Int) {
    }

    override fun setColorFilter(p0: ColorFilter?) {
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

}