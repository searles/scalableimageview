package at.searles.fractimageview.demo

import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import at.searles.fractimageview.DrawBitmapBoundsPlugin
import at.searles.fractimageview.GridPlugin
import at.searles.fractimageview.IconIfFlippedPlugin
import at.searles.fractimageview.PluginScalableImageView

/**
 * This demo shows
 */
class DemoActivity : AppCompatActivity() {

    private val scalableImageView: PluginScalableImageView by lazy {
        findViewById<PluginScalableImageView>(R.id.scalableImageView)
    }

    private val textView: TextView by lazy {
        findViewById<TextView>(R.id.textView)
    }

    private val rotationLockCheckBox: CheckBox by lazy {
        findViewById<CheckBox>(R.id.rotationLockCheckBox)
    }

    private val confirmZoomCheckBox: CheckBox by lazy {
        findViewById<CheckBox>(R.id.confirmZoomCheckBox)
    }

    private val isTouchEnabledCheckBox: CheckBox by lazy {
        findViewById<CheckBox>(R.id.isTouchEnabledCheckBox)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scalableImageView.scalableBitmapModel = DemoBitmapProvider()

        scalableImageView.addPlugin(DrawBitmapBoundsPlugin())
        scalableImageView.addPlugin(GridPlugin(this))
        scalableImageView.addPlugin(DemoPlugin(textView))
        scalableImageView.addPlugin(IconIfFlippedPlugin(this))

        confirmZoomCheckBox.setOnClickListener { scalableImageView.mustConfirmZoom = confirmZoomCheckBox.isChecked }
        rotationLockCheckBox.setOnClickListener { scalableImageView.hasRotationLock = rotationLockCheckBox.isChecked }
        isTouchEnabledCheckBox.setOnClickListener { scalableImageView.isTouchEnabled = isTouchEnabledCheckBox.isChecked }

        scalableImageView.mustConfirmZoom = confirmZoomCheckBox.isChecked
        scalableImageView.hasRotationLock = rotationLockCheckBox.isChecked
        scalableImageView.isTouchEnabled = isTouchEnabledCheckBox.isChecked
    }

    override fun onBackPressed() {
        scalableImageView.cancelMultitouchGesture()
    }
}
