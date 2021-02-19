package studio.kio.android.statefulpager

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.recyclerview.widget.RecyclerView
import studio.kio.android.statefulpager.switcher.DefaultSwitcher
import studio.kio.android.statefulpager.switcher.RecyclerViewSwitcher

/**
 * created by KIO on 2021/1/11
 */
@Suppress("CanBeParameter", "MemberVisibilityCanBePrivate")
class StatefulPagerHelper(private val container: ViewGroup) : SwitchAnimationProvider {

    var switchAnimationProvider: SwitchAnimationProvider? = null

    private val viewStateChanger = when (container) {
        is RecyclerView -> RecyclerViewSwitcher(container, this)
        else -> DefaultSwitcher(container, this)
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

    override fun defaultLeave(): Animation? = switchAnimationProvider?.defaultLeave()

    override fun defaultEnter(): Animation? = switchAnimationProvider?.defaultEnter()

    override fun otherLeave(): Animation? = switchAnimationProvider?.otherLeave()

    override fun otherEnter(): Animation? = switchAnimationProvider?.otherEnter()
}
