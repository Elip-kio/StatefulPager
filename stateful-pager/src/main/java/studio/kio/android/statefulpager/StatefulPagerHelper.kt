package studio.kio.android.statefulpager

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import studio.kio.android.statefulpager.switcher.DefaultSwitcher
import studio.kio.android.statefulpager.switcher.RecyclerViewSwitcher

/**
 * created by KIO on 2021/1/11
 */
@Suppress("CanBeParameter")
class StatefulPagerHelper(
    private val container: ViewGroup,
    private val switchAnimationProvider: SwitchAnimationProvider? = null
) {
    private val viewStateChanger = when (container) {
        is RecyclerView -> RecyclerViewSwitcher(container)
        else -> DefaultSwitcher(container, switchAnimationProvider)
    }

    var pageState = PageState.DEFAULT
        private set

    private val childrenOriginVisibility = mutableMapOf<View, Int>()

    fun showDefaultView() = container.post {
        if (pageState == PageState.DEFAULT) return@post

        pageState = PageState.DEFAULT

        childrenOriginVisibility.iterator().forEach {
            it.key.visibility = it.value
        }

        viewStateChanger.switchDefault()
    }

    fun show(view: View) = container.post {
        if (pageState == PageState.DEFAULT) {
            //record origin view state
            childrenOriginVisibility.clear()
            container.children.forEach {
                childrenOriginVisibility[it] = it.visibility
                it.visibility = View.GONE
            }
        }

        pageState = PageState.ALTERNATE

        viewStateChanger.switchAlternate(view)
    }

    enum class PageState { DEFAULT, ALTERNATE }
}
