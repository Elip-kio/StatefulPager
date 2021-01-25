package studio.kio.android.stateful.core

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * created by KIO on 2021/1/25
 */
class BindingViewHolder<T : ViewBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)