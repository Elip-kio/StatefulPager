package studio.kio.android.stateful_pager

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.MainThread
import androidx.core.view.children
import androidx.core.view.contains

/**
 * created by KIO on 2021/1/11
 */
class StatefulPageHelper(private val page: ViewGroup) {
    var pageState = PageState.MAJOR
        private set

    private val childrenOriginVisibility = mutableMapOf<View, Int>()

    private var minorView = MinorWrapperLayout(page.context)

    @MainThread
    fun showMajor() {
        pageState = PageState.MAJOR

        page.removeView(minorView)

        childrenOriginVisibility.iterator().forEach {
            it.key.visibility = it.value
        }

    }

    @MainThread
    fun showMinor(viewGenerator: LayoutInflater.() -> View) {
        if (pageState == PageState.MAJOR) {
            //record origin view state
            childrenOriginVisibility.clear()
            page.children.forEach {
                childrenOriginVisibility[it] = it.visibility
                it.visibility = View.GONE
            }
        }

        pageState = PageState.MINOR

        val view = viewGenerator.invoke(LayoutInflater.from(page.context))
        minorView.removeAllViews()
        minorView.addView(view, page.width, page.height)

        if (!page.contains(minorView)) {
            page.addView(minorView, page.width, page.height)
        }
    }

    enum class PageState { MAJOR, MINOR }
}

class MinorWrapperLayout(context: Context) : FrameLayout(context) {
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