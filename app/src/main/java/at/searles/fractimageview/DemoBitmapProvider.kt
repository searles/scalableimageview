package at.searles.fractimageview

import android.graphics.Bitmap
import android.graphics.Matrix

class DemoBitmapProvider: BitmapProvider() {

    private val scaleMatrix = Matrix()

    override var bitmap: Bitmap =
        Bitmap.createBitmap(50, 10, Bitmap.Config.ARGB_8888).apply {
            eraseColor(android.graphics.Color.GREEN);
        }

    override val normMatrix: Matrix
        get() = scaleMatrix

    override fun scale(normMatrix: Matrix) {
        scaleMatrix.postConcat(normMatrix)
    }
}