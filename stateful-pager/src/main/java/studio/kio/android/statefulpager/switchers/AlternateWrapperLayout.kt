package studio.kio.android.statefulpager.switchers

import android.content.Context
import android.graphics.Rect
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams

/**
 * [AlternateWrapperLayout] is used to wrap minor layout.
 * We use it to handle parent view padding,
 * and make sure that the minor layout can match the parent size
 */
class AlternateWrapperLayout(context: Context) : FrameLayout(context) {
    private var rect: Rect? = null
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val parent = parent as? ViewGroup ?: return

        //remove padding
        parent.apply {
            rect = Rect(paddingLeft, paddingRight, paddingLeft, paddingRight)
            setPadding(0, 0, 0, 0)
        }

        //fit size to parent
        updateLayoutParams<ViewGroup.LayoutParams> {
            width = parent.width
            height = parent.height
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        val p = parent as? ViewGroup ?: return
        rect?.let {
            p.setPadding(it.left, it.top, it.right, it.bottom)
        }
    }
}