package at.searles.fractimageview

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import kotlin.math.min

abstract class BitmapProvider {

    val width
        get() = bitmap.width
    val height
        get() = bitmap.height

    val bitmapToNormMatrix: Matrix
        get() {
            val m = min(width, height).toFloat()
            return Matrix().apply {
                setValues(floatArrayOf(
                    2f / m, 0f, -width / m,
                    0f, 2f / m, -height / m,
                    0f, 0f, 1f
                ))
            }
        }

    val normToBitmapMatrix: Matrix
        get() {
            val m = min(width, height).toFloat()
            return Matrix().apply {
                setValues(floatArrayOf(
                    m / 2f, 0f, width / 2f,
                    0f, m / 2f, height / 2f,
                    0f, 0f, 1f
                ))
            }
        }

    /**
     * The current bitmap to be drawn
     */
    abstract val bitmap: Bitmap

    /**
     * A matrix that should be used to transform the bitmap before drawing it.
     * Needed eg if the bitmap should be scale but no new image data is available yet.
     */
    abstract val normMatrix: Matrix

    /**
     * Registers a scale event. The matrix itself is normalized, ie, (0,0) is the center
     * and the image is supposed to fully fix the square (-1,-1)-(1,1) (left-up to right-down)
     */
    abstract fun scale(normMatrix: Matrix)

    interface Listener {
        fun bitmapDimensionsChanged()
        fun bitmapUpdated()
    }
}