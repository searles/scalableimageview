package at.searles.fractimageview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent

class PluginScalableImageView(context: Context, attrs: AttributeSet) : ScalableImageView(context, attrs) {
    private val plugins = ArrayList<Plugin>()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        plugins.forEach {
            it.onDraw(this, canvas)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(!isTouchEnabled) {
            return false
        }

        plugins.forEach {
            if (it.onTouchEvent(this, event)) return true
        }

        return super.onTouchEvent(event)
    }

    fun addPlugin(plugin: Plugin) {
        this.plugins.add(0, plugin)
    }

    fun removePlugin(plugin: Plugin) {
        this.plugins.remove(plugin)
    }
}