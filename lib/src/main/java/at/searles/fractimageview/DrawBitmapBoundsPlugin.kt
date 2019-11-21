package at.searles.fractimageview

import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent

class DrawBitmapBoundsPlugin: ScalableImageView.Plugin {
    override fun onDraw(source: ScalableImageView, canvas: Canvas) {
        val vw = source.width.toFloat()
        val vh = source.height.toFloat()

        val bw = source.bitmapProvider.width.toFloat()
        val bh = source.bitmapProvider.height.toFloat()

        val cx = vw / 2f
        val cy = vh / 2f
        val sbw = ScalableBitmapViewUtils.scaledBitmapWidth(bw, bh, vw, vh)
        val sbh = ScalableBitmapViewUtils.scaledBitmapHeight(bw, bh, vw, vh)

        // draw in total 4 transparent rectangles to indicate the drawing area
        canvas.drawRect(-1f, -1f, vw, cy - sbh / 2f, boundsPaint) // top
        canvas.drawRect(-1f, -1f, cx - sbw / 2f, vh, boundsPaint) // left
        canvas.drawRect(-1f, cy + sbh / 2f, vw, vh, boundsPaint) // bottom
        canvas.drawRect(cx + sbw / 2f, -1f, vw, vh, boundsPaint) // right
    }

    override fun onTouchEvent(source: ScalableImageView, event: MotionEvent): Boolean = false

    companion object {
        val boundsPaint = Paint().apply {
            color = -0x80000000 // semi-transparent black
            style = Paint.Style.FILL_AND_STROKE
        }
    }
}