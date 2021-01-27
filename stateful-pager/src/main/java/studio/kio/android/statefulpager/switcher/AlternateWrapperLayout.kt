package studio.kio.android.statefulpager.switcher

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import java.lang.IllegalStateException

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
            width = if (parent.width != 0) parent.width else ViewGroup.LayoutParams.WRAP_CONTENT
            height = if (parent.height != 0) parent.height else ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        val p = parent as? ViewGroup ?: return
        rect?.let {
            p.setPadding(it.left, it.top, it.right, it.bottom)
        }
    }

    val contentView: View?
        get() = when (childCount) {
            0 -> null
            1 -> getChildAt(0)
            else -> throw IllegalStateException("AlternateWrapperLayout can host only one child.")
        }
}