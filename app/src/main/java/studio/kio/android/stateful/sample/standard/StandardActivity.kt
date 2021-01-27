package studio.kio.android.stateful.sample.standard

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.Explode
import android.transition.Transition
import android.transition.Visibility
import android.view.View
import studio.kio.android.stateful.core.BaseActivity
import studio.kio.android.stateful.databinding.ActivityStatefulPageBinding
import studio.kio.android.statefulpager.StatefulPagerHelper
import studio.kio.android.statefulpager.effect.ScaleAnimationProvider

class StandardActivity : BaseActivity() {

    private val binding by lazy {
        ActivityStatefulPageBinding.inflate(layoutInflater)
    }

    override fun onCreateStatefulPagerHelper(): StatefulPagerHelper {
        return StatefulPagerHelper(binding.container, ScaleAnimationProvider())
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = "Standard Demo"

        //StateAnimator will be reserved
        binding.container.setOnClickListener { }
    }
}