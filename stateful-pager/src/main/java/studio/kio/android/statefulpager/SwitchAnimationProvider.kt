package studio.kio.android.statefulpager

import android.view.animation.Animation

/**
 * Provides animations for the [ViewStateSwitcher].
 * Besides, you can handle animation instances by yourself.
 * This means that the mechanism of animation caching or other can be customized.
 */
interface SwitchAnimationProvider {
    fun defaultLeave(): Animation?
    fun defaultEnter(): Animation?

    fun otherLeave(): Animation?
    fun otherEnter(): Animation?
}