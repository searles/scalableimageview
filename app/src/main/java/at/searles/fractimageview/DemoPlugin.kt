package at.searles.fractimageview

import android.graphics.*
import android.view.MotionEvent
import android.widget.TextView

class DemoPlugin(private val tv: TextView): ScalableImageView.Plugin {

    private val paint = Paint().apply {
        strokeWidth = 4f
        color = Color.BLACK
    }

    override fun onDraw(source: ScalableImageView, canvas: Canvas) {
        val corners = floatArrayOf(-1f, -1f, -1f, 1f, 1f, -1f)

        normToViewMatrix(source).mapPoints(corners)

        canvas.drawLine(corners[0], corners[1], corners[2], corners[3], paint)
        canvas.drawLine(corners[2], corners[3], corners[4], corners[5], paint)
        canvas.drawLine(corners[4], corners[5], corners[0], corners[1], paint)
    }

    override fun onTouchEvent(source: ScalableImageView, event: MotionEvent): Boolean {
        // show coordinates in norm-coordinates
        val matrix = viewToNormMatrix(source)

        val pt = floatArrayOf(event.x, event.y)
        matrix.mapPoints(pt)

        tv.text = "${pt[0]} x ${pt[1]}"

        return false
    }

    private fun normToViewMatrix(scalableImageView: ScalableImageView): Matrix {
        val vw = scalableImageView.width.toFloat()
        val vh = scalableImageView.height.toFloat()
        val bw = scalableImageView.scalableBitmapModel.width.toFloat()
        val bh = scalableImageView.scalableBitmapModel.height.toFloat()

        val matrix = Matrix(scalableImageView.scaleNormMatrix)
        matrix.postConcat(ScalableBitmapViewUtils.normToBitmapMatrix(bw, bh))
        matrix.postConcat(ScalableBitmapViewUtils.bitmapToViewMatrix(bw, bh, vw, vh))

        return matrix
    }

    private fun viewToNormMatrix(scalableImageView: ScalableImageView): Matrix {
        return normToViewMatrix(scalableImageView).also {
            it.invert(it)
        }
    }
}