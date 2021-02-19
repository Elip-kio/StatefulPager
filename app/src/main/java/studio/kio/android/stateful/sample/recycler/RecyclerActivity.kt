package studio.kio.android.stateful.sample.recycler

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import studio.kio.android.stateful.R
import studio.kio.android.stateful.core.BaseControlActivity
import studio.kio.android.stateful.core.BindingViewHolder
import studio.kio.android.stateful.databinding.ActivityListBinding
import studio.kio.android.stateful.databinding.ItemRecyclerBinding
import studio.kio.android.stateful.databinding.SlotRecyclerBinding
import studio.kio.android.statefulpager.StatefulPagerHelper
import kotlin.random.Random

class RecyclerActivity : BaseControlActivity() {
    private val binding by lazy { ActivityListBinding.inflate(layoutInflater) }

    override fun onCreateStatefulPagerHelper() = StatefulPagerHelper(binding.recycler)

    override fun customizeControls(
        container: FrameLayout,
        statefulPagerHelper: StatefulPagerHelper
    ) {
        container.removeAllViews()
        SlotRecyclerBinding.inflate(layoutInflater, container, true).apply {
            val random = Random(System.currentTimeMillis())
            layoutManagerChoice.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.check_linear -> {
                        binding.recycler.layoutManager = LinearLayoutManager(this@RecyclerActivity)
                    }
                    R.id.check_grid -> {
                        binding.recycler.layoutManager =
                            GridLayoutManager(this@RecyclerActivity, random.nextInt(2, 6))
                    }
                }
            }
        }
    }

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

        binding.recycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.HORIZONTAL
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