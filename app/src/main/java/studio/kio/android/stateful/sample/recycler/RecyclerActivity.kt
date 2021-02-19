package studio.kio.android.stateful.sample.recycler

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import studio.kio.android.stateful.core.BaseControlActivity
import studio.kio.android.stateful.core.BindingViewHolder
import studio.kio.android.stateful.databinding.ActivityListBinding
import studio.kio.android.stateful.databinding.ItemRecyclerBinding
import studio.kio.android.statefulpager.StatefulPagerHelper

class RecyclerActivity : BaseControlActivity() {
    private val binding by lazy { ActivityListBinding.inflate(layoutInflater) }

    override fun onCreateStatefulPagerHelper() = StatefulPagerHelper(binding.recycler)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installContent(binding.root)

        title = "Recycler View Demo"

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = RecyclerAdapter()

        binding.recycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        //You can change state to loading at beginning
        //statefulEnableConfig?.apply {
        //    statefulPagerHelper.show(loadingView.root)
        //}
    }

}

class RecyclerAdapter : RecyclerView.Adapter<BindingViewHolder<ItemRecyclerBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingViewHolder<ItemRecyclerBinding> = BindingViewHolder(
        ItemRecyclerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BindingViewHolder<ItemRecyclerBinding>, position: Int) {
        holder.binding.title.text = "$position . This is a sample line"
    }

    override fun getItemCount(): Int = 50

}