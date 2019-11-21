package at.searles.fractimageview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView

/**
 * This demo shows
 */
class MainActivity : AppCompatActivity() {

    val scalableImageView: PluginScalableImageView by lazy {
        findViewById<PluginScalableImageView>(R.id.scalableImageView)
    }

    val textView: TextView by lazy {
        findViewById<TextView>(R.id.textView)
    }

    val rotationLockCheckBox: CheckBox by lazy {
        findViewById<CheckBox>(R.id.rotationLockCheckBox)
    }

    val centerLockCheckBox: CheckBox by lazy {
        findViewById<CheckBox>(R.id.centerLockCheckBox)
    }

    val confirmZoomCheckBox: CheckBox by lazy {
        findViewById<CheckBox>(R.id.confirmZoomCheckBox)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scalableImageView.bitmapProvider = DemoBitmapProvider()

        scalableImageView.addPlugin(DrawBitmapBoundsPlugin())
        scalableImageView.addPlugin(DemoPlugin(textView))

        confirmZoomCheckBox.setOnClickListener { scalableImageView.mustConfirmZoom = confirmZoomCheckBox.isChecked }
        centerLockCheckBox.setOnClickListener { scalableImageView.hasCenterLock = centerLockCheckBox.isChecked }
        rotationLockCheckBox.setOnClickListener { scalableImageView.hasRotationLock = rotationLockCheckBox.isChecked }

        scalableImageView.mustConfirmZoom = confirmZoomCheckBox.isChecked
        scalableImageView.hasRotationLock = rotationLockCheckBox.isChecked
        scalableImageView.hasCenterLock = centerLockCheckBox.isChecked
    }

    override fun onBackPressed() {
        scalableImageView.cancelMultitouchGesture()
    }
}
