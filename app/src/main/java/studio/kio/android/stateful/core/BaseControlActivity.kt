package studio.kio.android.stateful.core

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import studio.kio.android.stateful.R
import studio.kio.android.stateful.databinding.BaseControllScaffoldBinding
import studio.kio.android.stateful.databinding.StateErrorOrEmptyBinding
import studio.kio.android.stateful.databinding.StateLoadingBinding
import studio.kio.android.statefulpager.StatefulPagerHelper
import studio.kio.android.statefulpager.effect.FadeAnimationProvider
import studio.kio.android.statefulpager.effect.ScaleAnimationProvider
import studio.kio.android.statefulpager.effect.ScaleFadeAnimationProvider

/**
 * created by KIO on 2021/1/25
 */
@Suppress("MemberVisibilityCanBePrivate")
@SuppressLint("SetTextI18n")
abstract class BaseControlActivity : AppCompatActivity() {

    private lateinit var binding: BaseControllScaffoldBinding

    private val statefulEnableConfig by lazy {
        onCreateStatefulPagerHelper()?.let {
            StatefulEnableConfig(
                statefulPagerHelper = it,
                loadingView = StateLoadingBinding.inflate(layoutInflater),
                errorView = StateErrorOrEmptyBinding.inflate(layoutInflater).apply {
                    text.text = "Oops, an error occurred during data loading!"
                    icon.setImageResource(R.drawable.error)
                },
                emptyView = StateErrorOrEmptyBinding.inflate(layoutInflater).apply {
                    text.text = "There is nothing to be shown."
                    icon.setImageResource(R.drawable.empty)
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = BaseControllScaffoldBinding.inflate(layoutInflater)

        statefulEnableConfig?.apply {
            binding.tabState.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.text) {
                        getString(R.string.start_loading) -> statefulPagerHelper.show(loadingView.root)
                        getString(R.string.end_loading_success) -> statefulPagerHelper.showDefaultView()
                        getString(R.string.end_loading_empty) -> statefulPagerHelper.show(emptyView.root)
                        getString(R.string.end_loading_error) -> statefulPagerHelper.show(errorView.root)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })

            binding.tabAnimation.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    statefulEnableConfig?.statefulPagerHelper?.apply {
                        when (tab?.text) {
                            getString(R.string.animation_empty) -> {
                                this.switchAnimationProvider = null
                            }
                            getString(R.string.animation_fade) -> {
                                this.switchAnimationProvider = FadeAnimationProvider()
                            }
                            getString(R.string.animation_scale) -> {
                                this.switchAnimationProvider = ScaleAnimationProvider()
                            }
                            getString(R.string.animation_fade_scale) -> {
                                this.switchAnimationProvider = ScaleFadeAnimationProvider()
                            }
                        }
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.tabState.clearOnTabSelectedListeners()
        binding.tabAnimation.clearOnTabSelectedListeners()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }

    /**
     * If subclass returns a [StatefulPagerHelper],
     * [BaseControlActivity] will create some stateful view, and provides the basic controlling features.
     *
     * @see [installContent]
     */
    protected open fun onCreateStatefulPagerHelper(): StatefulPagerHelper? {
        return null
    }

    /**
     * If subclass provides a [StatefulPagerHelper], this function will put the provided view into
     * a scaffold layout which consist with control board.
     *
     * @see [onCreateStatefulPagerHelper]
     */
    protected fun installContent(content: View) {
        if (statefulEnableConfig != null) {
            binding.content.removeAllViews()
            binding.content.addView(content)
            setContentView(binding.root)
        } else {
            setContentView(content)
        }
    }

    private data class StatefulEnableConfig(
        val statefulPagerHelper: StatefulPagerHelper,
        val loadingView: StateLoadingBinding,
        val emptyView: StateErrorOrEmptyBinding,
        val errorView: StateErrorOrEmptyBinding,
    )
}