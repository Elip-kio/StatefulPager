package studio.kio.android.statefulpager.switcher

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import studio.kio.android.statefulpager.SwitchAnimationProvider
import studio.kio.android.statefulpager.ViewStateSwitcher

/**
 * created by KIO on 2021/1/25
 */
class RecyclerViewSwitcher(
    private val recyclerView: RecyclerView,
    private val switchAnimationProvider: SwitchAnimationProvider? = null
) : ViewStateSwitcher {
    private val alternateAdapter by lazy {
        val origin = requireNotNull(recyclerView.adapter) { "Must set adapter $recyclerView" }
        AlternateAdapter(recyclerView.context, origin).also {
            recyclerView.adapter = it
        }
    }

    private var defaultItemDecorations = getItemDecorationsFrom(recyclerView)

    override fun switchDefault() {
        clearItemDecorationsOf(recyclerView)
        wrapItemDecorationsFor(recyclerView)
        alternateAdapter.isDefault = true
        recyclerView.children.iterator().forEach {
            switchAnimationProvider?.defaultEnter()?.run {
                it.startAnimation(this)
            }
        }
        alternateAdapter.alternateView = null
        alternateAdapter.notifyDataSetChanged()
    }

    override fun switchAlternate(view: View) {
        val needReplaceContent = alternateAdapter.alternateView != view
        val alternateAdded = alternateAdapter.alternateView != null

        if (needReplaceContent) {
            if (alternateAdded) {
                switchAnimationProvider?.otherLeave()?.run {
                    alternateAdapter.alternateView?.startAnimation(this)
                }
            }
            alternateAdapter.alternateView = view
        }

        if ((!alternateAdded) || needReplaceContent) {
            switchAnimationProvider?.otherEnter()?.run {
                view.startAnimation(this)
            }
        }

        if (alternateAdapter.isDefault) {
            recyclerView.children.iterator().forEach {
                switchAnimationProvider?.defaultLeave()?.run {
                    it.startAnimation(this)
                }
            }
            alternateAdapter.isDefault = false
            alternateAdapter.notifyDataSetChanged()
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

private class AlternateAdapter(
    context: Context,
    private val origin: RecyclerView.Adapter<in RecyclerView.ViewHolder>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private class AlternateViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val alternateWrapperLayout = AlternateWrapperLayout(context)

    var alternateView
        get() = alternateWrapperLayout.contentView
        set(value) {
            alternateWrapperLayout.removeAllViews()
            if (value != null)
                alternateWrapperLayout.addView(value)
        }

    var isDefault = true

    //Delegate adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isDefault) {
            origin.onCreateViewHolder(parent, viewType) as RecyclerView.ViewHolder
        } else {
            AlternateViewHolder(alternateWrapperLayout)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is AlternateViewHolder)
            origin.onBindViewHolder(holder, position)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (holder is AlternateViewHolder) {
            super.onBindViewHolder(
                holder,
                position,
                payloads
            )
        } else {
            origin.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isDefault) {
            val viewType = origin.getItemViewType(position)
            if (viewType == Int.MIN_VALUE)
                throw IllegalArgumentException("$viewType is used by StatefulPager, please consider replacing it with another")
            viewType
        } else Int.MIN_VALUE
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        if (isDefault) {
            origin.setHasStableIds(hasStableIds)
        } else {
            super.setHasStableIds(hasStableIds)
        }
    }

    override fun getItemId(position: Int): Long {
        return if (isDefault) origin.getItemId(position) else super.getItemId(position)
    }

    override fun getItemCount(): Int {
        return if (isDefault) origin.itemCount else 1
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is AlternateViewHolder) {
            super.onViewRecycled(holder)
        } else {
            origin.onViewRecycled(holder)
        }
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        if (holder is AlternateViewHolder) {
            return super.onFailedToRecycleView(holder)
        }
        return origin.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        if (holder is AlternateViewHolder) {
            super.onViewAttachedToWindow(holder)
        } else {
            origin.onViewAttachedToWindow(holder)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        if (holder is AlternateViewHolder) {
            super.onViewDetachedFromWindow(holder)
        } else {
            origin.onViewDetachedFromWindow(holder)
        }
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        origin.registerAdapterDataObserver(observer)
        super.registerAdapterDataObserver(observer)
    }

    override fun unregisterAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        origin.unregisterAdapterDataObserver(observer)
        super.unregisterAdapterDataObserver(observer)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        origin.onAttachedToRecyclerView(recyclerView)
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        origin.onDetachedFromRecyclerView(recyclerView)
        super.onDetachedFromRecyclerView(recyclerView)
    }

}