package studio.kio.android.statefulpager

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import studio.kio.android.statefulpager.switcher.DefaultSwitcher
import studio.kio.android.statefulpager.switcher.RecyclerViewSwitcher

/**
 * created by KIO on 2021/1/11
 */
@Suppress("CanBeParameter", "MemberVisibilityCanBePrivate")
class StatefulPagerHelper(
    private val container: ViewGroup,
    private val switchAnimationProvider: SwitchAnimationProvider? = null
) {
    private val viewStateChanger = when (container) {
        is RecyclerView -> RecyclerViewSwitcher(container, switchAnimationProvider)
        else -> DefaultSwitcher(container, switchAnimationProvider)
    }

    var pageState = PageState.DEFAULT
        private set

    fun showDefaultView() = container.post {
        if (pageState == PageState.DEFAULT) return@post

        pageState = PageState.DEFAULT

        viewStateChanger.switchDefault()
    }

    fun show(view: View) = container.post {

        pageState = PageState.ALTERNATE

        viewStateChanger.switchAlternate(view)
    }

    enum class PageState { DEFAULT, ALTERNATE }
}
