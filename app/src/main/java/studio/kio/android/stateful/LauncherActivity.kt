package studio.kio.android.stateful

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import studio.kio.android.stateful.core.BindingViewHolder
import studio.kio.android.stateful.databinding.ActivityLauncherBinding
import studio.kio.android.stateful.databinding.ItemLauncherBinding
import studio.kio.android.stateful.sample.recycler.RecyclerActivity
import studio.kio.android.stateful.sample.standard.StandardActivity
import studio.kio.android.stateful.sample.viewpager.ViewPagerActivity

class LauncherActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLauncherBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val data = listOf(
            LauncherItem("Standard Use Case", R.drawable.launcher_standard),
            LauncherItem("RecyclerView Use Case", R.drawable.launcher_standard),
            LauncherItem("ViewPager Use Case", R.drawable.launcher_standard),
            LauncherItem("Transitions", R.drawable.launcher_standard)
        )

        binding.launchItems.adapter = LauncherAdapter(data).apply {
            onItemClickListener = {
                val intent = when (it) {
                    0 -> Intent(this@LauncherActivity, StandardActivity::class.java)
                    1 -> Intent(this@LauncherActivity, RecyclerActivity::class.java)
                    2 -> Intent(this@LauncherActivity, ViewPagerActivity::class.java)
                    else -> throw NotImplementedError("This activity is still building!")
                }
                startActivity(intent)
            }
        }

        binding.launchItems.layoutManager = LinearLayoutManager(this)
    }
}

private class LauncherAdapter(val data: List<LauncherItem>) :
    RecyclerView.Adapter<BindingViewHolder<ItemLauncherBinding>>() {

    var onItemClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingViewHolder<ItemLauncherBinding> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLauncherBinding.inflate(inflater, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ItemLauncherBinding>, position: Int) {
        holder.binding.title.text = data[position].title
        holder.binding.image.setImageResource(data[position].image)
        holder.binding.root.setOnClickListener { onItemClickListener?.invoke(holder.adapterPosition) }
    }

    override fun getItemCount(): Int = data.size
}

data class LauncherItem(val title: String, @DrawableRes val image: Int)