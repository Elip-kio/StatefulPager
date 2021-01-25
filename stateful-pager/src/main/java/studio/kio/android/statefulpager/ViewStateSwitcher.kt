package studio.kio.android.statefulpager

import android.view.View

/**
 * created by KIO on 2021/1/24
 */
interface ViewStateSwitcher {
    fun switchDefault()
    fun switchAlternate(view: View)
}