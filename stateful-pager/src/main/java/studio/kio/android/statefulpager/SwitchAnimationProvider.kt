package studio.kio.android.statefulpager

import android.view.animation.Animation

/**
 * created by KIO on 2021/1/27
 */
interface SwitchAnimationProvider {
    fun defaultLeave(): Animation?
    fun defaultEnter(): Animation?

    fun otherLeave(): Animation?
    fun otherEnter(): Animation?
}