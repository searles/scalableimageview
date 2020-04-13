package at.searles.fractimageview

import android.R.attr.*
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.util.TypedValue
import android.view.MotionEvent


class IconIfFlippedPlugin(context: Context): ScalableImageView.Plugin {

    private val icon = context.resources.getDrawable(R.drawable.flipped_icon, null)
    private val iconSize = dpToPx(iconSizeDp, context.resources)

    override fun onDraw(source: ScalableImageView, canvas: Canvas) {
        val vw = source.width.toFloat()
        val vh = source.height.toFloat()
        val bw = source.scalableBitmapModel.width.toFloat()
        val bh = source.scalableBitmapModel.height.toFloat()

        if(ScalableBitmapViewUtils.isBitmapFlipped(bw, bh, vw, vh)) {
            icon.setBounds(
                (vw - iconSize).toInt(), (vh - iconSize).toInt(),
                vw.toInt(), vh.toInt())

            icon.draw(canvas)
        }
    }

    override fun onTouchEvent(source: ScalableImageView, event: MotionEvent): Boolean = false

    private fun dpToPx(dip: Float, resources: Resources): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            resources.displayMetrics
        )
    }

    companion object {
        private const val iconSizeDp = 24f
        private const val paddingDp = 2f
    }
}