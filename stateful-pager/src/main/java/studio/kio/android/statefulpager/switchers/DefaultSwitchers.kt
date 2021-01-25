package studio.kio.android.statefulpager.switchers

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.contains
import studio.kio.android.statefulpager.ViewStateSwitcher

/**
 * created by KIO on 2021/1/25
 */
class DefaultSwitcher(private val container: ViewGroup) : ViewStateSwitcher {

    private var alternateView = AlternateWrapperLayout(container.context)

    override fun switchDefault() {
        container.removeView(alternateView)
    }

    override fun switchAlternate(view: View) {
        alternateView.removeAllViews()
        alternateView.addView(view, container.width, container.height)

        if (!container.contains(alternateView)) {
            container.addView(alternateView, container.width, container.height)
        }
    }
}


/**
 * [AlternateWrapperLayout] is used to wrap minor layout.
 * We use it to handle parent view padding,
 * and make sure that the minor layout can match the parent size
 */
class AlternateWrapperLayout(context: Context) : FrameLayout(context) {
    private var rect: Rect? = null
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        (parent as? ViewGroup)?.apply {
            rect = Rect(paddingLeft, paddingRight, paddingLeft, paddingRight)
            setPadding(0, 0, 0, 0)
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