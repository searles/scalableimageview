package at.searles.fractimageview

import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import kotlin.math.max
import kotlin.math.min

object ScaleableBitmapViewUtils {
    fun bitmapToViewMatrix(bw: Float, bh: Float, vw: Float, vh: Float, ret: Matrix = Matrix()): Matrix {
        require(bw > 0 && bh > 0 && vw > 0 && vh > 0)

        val vr = RectF(0f, 0f, vw, vh)

        // next must be coordinated with isBitmapFlipped.
        val br = if (vw > vh) {
            RectF(0f, 0f, max(bw, bh), min(bw, bh))
        } else {
            RectF(0f, 0f, min(bw, bh), max(bw, bh))
        }

        val m = ret.apply {
            setRectToRect(br, vr, Matrix.ScaleToFit.CENTER)
        }

        // Check orientation
        if (isBitmapFlipped(bw, bh, vw, vh)) { // Turn centerImageMatrix by 90 degrees
            m.preTranslate(bh, 0f)
            m.preRotate(90f)
        }

        return m
    }

    fun isBitmapFlipped(bw: Float, bh: Float, vw: Float, vh: Float): Boolean {
        // maximize filled area
        return bw > bh && vw < vh || bw < bh && vw > vh
    }

    /**
     * Inverse of bitmap2norm
     */
    fun normToBitmapMatrix(width: Float, height: Float, ret: Matrix = Matrix()): Matrix {
        val m = Math.min(width, height)
        return ret.apply {
            setValues(floatArrayOf(
                m / 2f, 0f, width / 2f,
                0f, m / 2f, height / 2f,
                0f, 0f, 1f
            ))
        }
    }

    /**
     * Matrices to convert coordinates into value that is
     * independent from the bitmap-size. Normized always
     * contains the square -1,-1 - 1-1 with 0,0 in the middle
     * but also keeps the ratio of the image.
     */
    fun bitmapToNormMatrix(width: Float, height: Float, ret: Matrix = Matrix()): Matrix {
        val m = min(width, height)
        return ret.apply {
            setValues(floatArrayOf(
                2f / m, 0f, -width / m,
                0f, 2f / m, -height / m,
                0f, 0f, 1f
            ))
        }
    }

    /**
     * This returns the up-scaled length of the bitmap's edge towards the view's width.
     */
    fun scaledBitmapWidth(bw: Float, bh: Float, vw: Float, vh: Float): Float {
        /* Just some thoughts:
		The scaled rectangle of the bitmap should fit into the view.
		So, the ratio is the min-ratio.
		 */
        return if (isBitmapFlipped(bw, bh, vw, vh)) {
            val ratio = min(vw / bh, vh / bw)
            bh * ratio
        } else {
            val ratio = Math.min(vw / bw, vh / bh)
            bw * ratio
        }
    }

    fun scaledBitmapHeight(bw: Float, bh: Float, vw: Float, vh: Float): Float {
        /* Just some thoughts:
		The scaled rectangle of the bitmap should fit into the view.
		So, the ratio is the min-ratio.
		 */
        return if (isBitmapFlipped(bw, bh, vw, vh)) {
            val ratio = min(vw / bh, vh / bw)
            bw * ratio
        } else {
            val ratio = Math.min(vw / bw, vh / bh)
            bh * ratio
        }
    }


    /**
     * Normalize view coordinates using the provided bitmap coordinates. This one
     * takes care of isBitmapFlipped etc...
     */
    fun norm(p: PointF, bw: Float, bh: Float, vw: Float, vh: Float, ret: PointF = PointF()): PointF {
        val m = min(scaledBitmapWidth(bw, bh, vw, vh), scaledBitmapHeight(bw, bh, vw, vh))

        val x: Float
        val y: Float

        if(isBitmapFlipped(bw, bh, vw, vh)) {
            x = (p.y * 2f - vh) / m
            y = (vw - p.x * 2f) / m
        } else {
            x = (p.x * 2f - vw) / m
            y = (p.y * 2f - vh) / m
        }

        return ret.apply {
            this.x = x
            this.y = y
        }
    }

    fun bitmapInViewMatrix(bw: Float, bh: Float, vw: Float, vh: Float, bitmapNormMatrix: Matrix, scaleNormMatrix: Matrix, ret: Matrix = Matrix()): Matrix {
        return ret.apply {
            set(scaleNormMatrix)
            preConcat(bitmapNormMatrix)

            preConcat(bitmapToNormMatrix(bw, bh))
            postConcat(normToBitmapMatrix(bw, bh))
            postConcat(bitmapToViewMatrix(bw, bh, vw, vh))
        }
    }

    fun scaleMatrix(center: PointF, factor: Float, ret: Matrix = Matrix()): Matrix {
        return ret.apply {
            setValues(
                floatArrayOf(
                    factor, 0f, center.x * (1 - factor),
                    0f,
                    factor, center.y * (1 - factor),
                    0f, 0f, 1f
                )
            )
        }
    }
}