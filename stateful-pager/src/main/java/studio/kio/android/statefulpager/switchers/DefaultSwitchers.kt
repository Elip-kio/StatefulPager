package studio.kio.android.statefulpager.switchers

import android.view.View
import android.view.ViewGroup
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