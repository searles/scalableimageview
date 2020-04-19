package at.searles.fractimageview

import android.graphics.Canvas
import android.view.MotionEvent

interface Plugin {
    val isEnabled: Boolean
    fun onDraw(source: ScalableImageView, canvas: Canvas)
    fun onTouchEvent(source: ScalableImageView, event: MotionEvent): Boolean
}