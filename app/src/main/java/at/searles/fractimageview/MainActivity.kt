package at.searles.fractimageview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView

/**
 * This demo shows
 */
class MainActivity : AppCompatActivity() {

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

        scalableImageView.bitmapProvider = DemoBitmapProvider()

        scalableImageView.addPlugin(DrawBitmapBoundsPlugin())
        scalableImageView.addPlugin(DemoPlugin(textView))

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
