package at.searles.fractimageview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import at.searles.fractimageview.ScalableBitmapViewUtils.norm

open class ScalableImageView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var hasRotationLock = false
        set(value) {
            cancelMultitouchGesture()
            field = value
        }

    var mustConfirmZoom = false
        set(value) {
            cancelMultitouchGesture()
            field = value
        }

    /* Gesture control:
     * - scroll events are forwarded to a multitouch controller
     * - double tabs are zooms at the current position.
     */

    /**
     * To detect gestures (3 finger drag etc...)
     */
    private val detector = GestureDetector(context, ScaleGestureListener())

    /**
     * Adapter to convert gestures to input for the multitouch controller
     */
    private var multitouchAdapter: GestureToMultiTouchAdapter? = null

    lateinit var scalableBitmapModel: ScalableBitmapModel

    private val identityMatrix = Matrix()

    val scaleNormMatrix: Matrix
        get() = multitouchAdapter?.normMatrix ?: identityMatrix

    override fun onDraw(canvas: Canvas) {
        val scaleMatrix = multitouchAdapter?.normMatrix ?: identityMatrix

        val matrix = ScalableBitmapViewUtils.bitmapInViewMatrix(
            scalableBitmapModel.width.toFloat(), scalableBitmapModel.height.toFloat(),
            width.toFloat(), height.toFloat(),
            scalableBitmapModel.bitmapTransformMatrix, scaleMatrix
        )

        // draw image
        canvas.drawBitmap(scalableBitmapModel.bitmap, matrix, null)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_CANCEL -> {
                if(cancelMultitouchGesture()) {
                    return true
                }
            }
            MotionEvent.ACTION_DOWN -> {
                if(multitouchAdapter == null) {
                    multitouchAdapter = GestureToMultiTouchAdapter()
                }

                multitouchAdapter?.apply {
                    down(event)
                }
            }
            MotionEvent.ACTION_UP -> {
                multitouchAdapter?.run{
                    up(event)
                    if(isActive) {
                        if(!mustConfirmZoom) commitMultitouchGesture()
                    }
                }
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                multitouchAdapter?.down(event)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                multitouchAdapter?.up(event)
            }
        }

        return detector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    fun cancelMultitouchGesture(): Boolean {
        if(multitouchAdapter != null) {
            multitouchAdapter = null
            invalidate()
            return true
        }

        return false
    }

    private fun commitMultitouchGesture() {
        scalableBitmapModel.scale(multitouchAdapter!!.normMatrix)
        multitouchAdapter = null
        invalidate()
    }

    internal inner class ScaleGestureListener: SimpleOnGestureListener() {
        override fun onDown(motionEvent: MotionEvent): Boolean { // must be true, otherwise no touch events.
            return true
        }

        override fun onDoubleTap(event: MotionEvent): Boolean {
            if (mustConfirmZoom && multitouchAdapter != null && multitouchAdapter!!.isActive) { // not active when 'confirm zoom' is set.
                return false
            }

            if (multitouchAdapter != null) {
                cancelMultitouchGesture()
            }

            val index = event.actionIndex
            val p = PointF(event.getX(index), event.getY(index))
            val np = norm(p, scalableBitmapModel.width.toFloat(), scalableBitmapModel.height.toFloat(), width.toFloat(), height.toFloat())

            scalableBitmapModel.scale(ScalableBitmapViewUtils.scaleMatrix(np, dtScaleFactor))

            return true
        }

        override fun onSingleTapUp(motionEvent: MotionEvent): Boolean {
            return if (mustConfirmZoom && multitouchAdapter?.isActive == true) {
                commitMultitouchGesture()
                true
            } else {
                cancelMultitouchGesture()
                false
            }
        }

        override fun onScroll(startEvt: MotionEvent?, currentEvt: MotionEvent, vx: Float, vy: Float): Boolean {
            if(multitouchAdapter != null) {
                multitouchAdapter!!.scroll(currentEvt)
                return true
            }

            return false
        }
    }

    internal inner class GestureToMultiTouchAdapter {
        private val controller = MultiTouchController(hasRotationLock)

        var isActive = false

        val normMatrix: Matrix
            get() = controller.matrix

        fun down(event: MotionEvent) {
            val index = event.actionIndex
            val id = event.getPointerId(index)
            val pt = norm(PointF(event.getX(index), event.getY(index)), scalableBitmapModel.width.toFloat(), scalableBitmapModel.height.toFloat(), width.toFloat(), height.toFloat())

            controller.pointDown(id, pt)
        }

        fun up(event: MotionEvent) {
            val index = event.actionIndex
            val id = event.getPointerId(index)
            controller.pointUp(id)
        }

        fun scroll(event: MotionEvent) {
            isActive = true
            for (index in 0 until event.pointerCount) {
                val pt = norm(PointF(event.getX(index), event.getY(index)), scalableBitmapModel.width.toFloat(), scalableBitmapModel.height.toFloat(), width.toFloat(), height.toFloat())
                val id = event.getPointerId(index)
                controller.pointDrag(id, pt)
            }

            invalidate()
        }
    }

    companion object {
        /**
         * Scale factor on double tapping
         */
        const val dtScaleFactor = 3f
    }
}