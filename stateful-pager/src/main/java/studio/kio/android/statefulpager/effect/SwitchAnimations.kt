@file:Suppress("FunctionName")

package studio.kio.android.statefulpager.effect

import android.view.animation.*
import studio.kio.android.statefulpager.SwitchAnimationProvider

fun ScaleAnimationProvider(): SwitchAnimationProvider = object : SwitchAnimationProvider {
    private val scaleIn = ScaleAnimation(
        0.2f, 1f, 0.2f, 1f,
        ScaleAnimation.RELATIVE_TO_PARENT, 0.5f,
        ScaleAnimation.RELATIVE_TO_PARENT, 0.5f
    ).apply {
        duration = 300
        interpolator = OvershootInterpolator()
    }

    private val scaleOut = ScaleAnimation(
        1f, 0.2f, 1f, 0.2f,
        ScaleAnimation.RELATIVE_TO_PARENT, 0.5f,
        ScaleAnimation.RELATIVE_TO_PARENT, 0.5f
    ).apply {
        duration = 300
    }

    override fun defaultLeave(): Animation = scaleOut

    override fun defaultEnter(): Animation = scaleIn

    override fun otherLeave(): Animation = scaleOut

    override fun otherEnter(): Animation = scaleIn

}

fun FadeAnimationProvider(): SwitchAnimationProvider = object : SwitchAnimationProvider {
    private val fadeIn = AlphaAnimation(0f, 1f).apply {
        duration = 300
    }

    private val fadeOut = AlphaAnimation(1f, 0f).apply {
        duration = 300
    }

    override fun defaultLeave(): Animation = fadeOut

    override fun defaultEnter(): Animation = fadeIn

    override fun otherLeave(): Animation = fadeOut

    override fun otherEnter(): Animation = fadeIn

}

fun ScaleFadeAnimationProvider(): SwitchAnimationProvider = object : SwitchAnimationProvider {
    private val scale = ScaleAnimationProvider()
    private val fade = FadeAnimationProvider()

    private operator fun Animation?.plus(other: Animation?): Animation =
        AnimationSet(false).apply {
            if (this@plus != null) addAnimation(this@plus)
            if (other != null) addAnimation(other)
        }

    private val defaultLeave: Animation by lazy {
        scale.defaultLeave() + fade.defaultLeave()
    }

    private val defaultEnter: Animation by lazy {
        scale.defaultEnter() + fade.defaultEnter()
    }

    private val otherLeave: Animation by lazy {
        scale.otherLeave() + fade.otherLeave()
    }

    private val otherEnter: Animation by lazy {
        scale.otherEnter() + fade.otherEnter()
    }

    override fun defaultLeave(): Animation = defaultLeave

    override fun defaultEnter(): Animation = defaultEnter

    override fun otherLeave(): Animation = otherLeave

    override fun otherEnter(): Animation = otherEnter

}