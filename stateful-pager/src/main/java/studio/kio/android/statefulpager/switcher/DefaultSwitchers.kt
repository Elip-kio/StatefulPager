package studio.kio.android.statefulpager.switcher

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.core.view.children
import androidx.core.view.contains
import studio.kio.android.statefulpager.SwitchAnimationProvider
import studio.kio.android.statefulpager.ViewStateSwitcher

/**
 * created by KIO on 2021/1/25
 */
class DefaultSwitcher(
    private val container: ViewGroup,
    private val switchAnimationProvider: SwitchAnimationProvider? = null
) : ViewStateSwitcher {

    private val childrenOriginVisibility = mutableMapOf<View, Int>()

    private var alternateView = AlternateWrapperLayout(container.context)

    override fun switchDefault() {
        childrenOriginVisibility.iterator().forEach {
            it.key.visibility = it.value
        }

        switchAnimationProvider?.otherLeave()?.run {
            alternateView.startAnimation(this)
        }
        container.removeView(alternateView)
        switchAnimationProvider?.defaultEnter()?.run {
            container.children.filterVisibility(View.VISIBLE).startAnimation(this)
        }
    }

    override fun switchAlternate(view: View) {
        val alternateAdded = container.contains(alternateView)
        val needReplaceContent = view != alternateView.contentView

        if (needReplaceContent) {
            if (alternateAdded) {
                switchAnimationProvider?.otherLeave()?.run {
                    alternateView.contentView?.startAnimation(this)
                }
            }
            alternateView.removeAllViews()

            alternateView.addView(view)
        }

        if ((!alternateAdded) || needReplaceContent) {
            switchAnimationProvider?.otherEnter()?.run {
                view.startAnimation(this)
            }
        }

        if (!alternateAdded) {
            childrenOriginVisibility.clear()
            container.children.forEach {
                childrenOriginVisibility[it] = it.visibility
                it.visibility = View.GONE
            }

            switchAnimationProvider?.defaultLeave()?.run {
                container.children.startAnimation(this)
            }
            container.addView(alternateView, container.width, container.height)
        }
    }

    private fun Sequence<View>.filterVisibility(visibility: Int) = filter {
        it.visibility == visibility
    }

    private fun Sequence<View>.startAnimation(animation: Animation) {
        forEach {
            it.startAnimation(animation)
        }
    }
}