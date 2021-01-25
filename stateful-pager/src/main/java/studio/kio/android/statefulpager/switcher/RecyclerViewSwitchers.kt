package studio.kio.android.statefulpager.switcher

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import studio.kio.android.statefulpager.ViewStateSwitcher

/**
 * created by KIO on 2021/1/25
 */
class RecyclerViewSwitcher(private val recyclerView: RecyclerView) : ViewStateSwitcher {
    private var defaultAdapter = recyclerView.adapter
    private var alternateAdapter = AlternateRecyclerAdapter(recyclerView.context)

    private var defaultItemDecorations = getItemDecorationsFrom(recyclerView)

    override fun switchDefault() {
        recyclerView.adapter = defaultAdapter
        clearItemDecorationsOf(recyclerView)
        wrapItemDecorationsFor(recyclerView)
    }

    override fun switchAlternate(view: View) {
        alternateAdapter.setView(view)

        if (recyclerView.adapter != alternateAdapter) {
            defaultAdapter = recyclerView.adapter
            recyclerView.adapter = alternateAdapter

            defaultItemDecorations = getItemDecorationsFrom(recyclerView)
            clearItemDecorationsOf(recyclerView)
        }
    }

    private fun getItemDecorationsFrom(recyclerView: RecyclerView): List<RecyclerView.ItemDecoration> =
        mutableListOf<RecyclerView.ItemDecoration>().apply {
            val count = recyclerView.itemDecorationCount
            repeat(count) {
                add(recyclerView.getItemDecorationAt(it))
            }
        }

    private fun clearItemDecorationsOf(recyclerView: RecyclerView) {
        getItemDecorationsFrom(recyclerView).forEach {
            recyclerView.removeItemDecoration(it)
        }
    }

    private fun wrapItemDecorationsFor(recyclerView: RecyclerView) {
        defaultItemDecorations.forEach {
            recyclerView.addItemDecoration(it)
        }
    }

}

private class AlternateViewHolder(view: View) : RecyclerView.ViewHolder(view)

private class AlternateRecyclerAdapter(context: Context) :
    RecyclerView.Adapter<AlternateViewHolder>() {

    private var alternateView = AlternateWrapperLayout(context)

    fun setView(view: View) {
        alternateView.removeAllViews()
        alternateView.addView(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlternateViewHolder =
        AlternateViewHolder(alternateView)

    override fun onBindViewHolder(holder: AlternateViewHolder, position: Int) {
        //ignored
    }

    override fun getItemCount(): Int = 1
}