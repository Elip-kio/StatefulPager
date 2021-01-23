package studio.kio.android.stateful

import android.app.Activity
import android.widget.FrameLayout

/**
 * created by KIO on 2021/1/19
 */
fun Activity.rootView(): FrameLayout = findViewById(android.R.id.content)

fun Activity.post(block: () -> Unit) = postDelayed(0L, block)

fun Activity.postDelayed(delay: Long, block: () -> Unit) {
    rootView().postDelayed({ block() }, delay)
}