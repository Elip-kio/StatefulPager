@file:Suppress("FunctionName")

package studio.kio.android.statefulpager.effect

import android.view.animation.Animation
import studio.kio.android.statefulpager.SwitchAnimationProvider

fun ScaleAnimationProvider(): SwitchAnimationProvider = object : SwitchAnimationProvider {
    private val scaleIn =
        android.view.animation.ScaleAnimation(
            0f,
            1f,
            0f,
            1f,
            android.view.animation.ScaleAnimation.RELATIVE_TO_PARENT,
            0.5f,
            android.view.animation.ScaleAnimation.RELATIVE_TO_PARENT,
            0.5f
        ).apply {
            duration = 500
        }

    private val scaleOut = android.view.animation.ScaleAnimation(
        1f, 0f, 1f, 0f,
        android.view.animation.ScaleAnimation.RELATIVE_TO_PARENT, 0.5f,
        android.view.animation.ScaleAnimation.RELATIVE_TO_PARENT, 0.5f
    ).apply {
        duration = 500
    }

    override fun defaultLeave(): Animation = scaleOut

    override fun defaultEnter(): Animation = scaleIn

    override fun otherLeave(): Animation = scaleOut

    override fun otherEnter(): Animation = scaleIn

}