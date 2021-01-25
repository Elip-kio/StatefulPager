package studio.kio.android.statefulpager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.core.view.children
import studio.kio.android.statefulpager.switchers.DefaultSwitcher

/**
 * created by KIO on 2021/1/11
 */
class StatefulPagerHelper(private val container: ViewGroup) {
    private val viewStateChanger = DefaultSwitcher(container)

    var pageState = PageState.DEFAULT
        private set

    private val childrenOriginVisibility = mutableMapOf<View, Int>()

    @MainThread
    fun showDefaultView() {
        pageState = PageState.DEFAULT

        childrenOriginVisibility.iterator().forEach {
            it.key.visibility = it.value
        }

        viewStateChanger.switchDefault()
    }

    @MainThread
    fun show(viewGenerator: LayoutInflater.() -> View) {
        if (pageState == PageState.DEFAULT) {
            //record origin view state
            childrenOriginVisibility.clear()
            container.children.forEach {
                childrenOriginVisibility[it] = it.visibility
                it.visibility = View.GONE
            }
        }

        pageState = PageState.ALTERNATE

        val view = viewGenerator.invoke(LayoutInflater.from(container.context))
        viewStateChanger.switchAlternate(view)
    }

    enum class PageState { DEFAULT, ALTERNATE }
}
