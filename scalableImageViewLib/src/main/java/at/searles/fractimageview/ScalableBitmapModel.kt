package at.searles.fractimageview

import android.graphics.Bitmap
import android.graphics.Matrix

abstract class ScalableBitmapModel {

    val width
        get() = bitmap.width
    val height
        get() = bitmap.height

    /**
     * The current bitmap to be drawn
     */
    abstract val bitmap: Bitmap

    /**
     * A matrix that should be used to transform the bitmap before drawing it.
     * Needed eg if the bitmap should be scale but no new image data is available yet.
     */
    abstract val bitmapTransformMatrix: Matrix

    /**
     * Registers a scale event. The matrix itself is normalized, ie, (0,0) is the center
     * and the image is supposed to fully fix the square (-1,-1)-(1,1) (left-up to right-down)
     */
    abstract fun scale(relativeMatrix: Matrix)

}