package studio.kio.android.stateful.sample.viewpager

import android.os.Bundle
import studio.kio.android.stateful.core.BaseControlActivity
import studio.kio.android.stateful.databinding.ActivityViewPagerBinding
import studio.kio.android.statefulpager.StatefulPagerHelper

class ViewPagerActivity : BaseControlActivity() {
    init {
        throw NotImplementedError("This activity is still building...")
    }

    private val binding by lazy { ActivityViewPagerBinding.inflate(layoutInflater) }

    override fun onCreateStatefulPagerHelper(): StatefulPagerHelper =
        StatefulPagerHelper(binding.viewPager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}