package at.searles.fractimageview.plugins

import android.graphics.Canvas
import android.view.MotionEvent
import at.searles.fractimageview.Plugin
import at.searles.fractimageview.ScalableImageView

class GestureBlockPlugin: Plugin {

    override var isEnabled: Boolean = true

    override fun onDraw(source: ScalableImageView, canvas: Canvas) {
        // XXX maybe some icon?
    }

    override fun onTouchEvent(source: ScalableImageView, event: MotionEvent): Boolean {
        return isEnabled
    }
}