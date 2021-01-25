package studio.kio.android.stateful.core

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import studio.kio.android.stateful.R
import studio.kio.android.stateful.databinding.StateErrorOrEmptyBinding
import studio.kio.android.stateful.databinding.StateLoadingBinding
import studio.kio.android.statefulpager.StatefulPagerHelper

/**
 * created by KIO on 2021/1/25
 */
@Suppress("MemberVisibilityCanBePrivate")
@SuppressLint("SetTextI18n")
abstract class BaseActivity : AppCompatActivity() {

    protected val statefulEnableConfig by lazy {
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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()

        statefulEnableConfig?.apply {
            when (item.itemId) {
                R.id.start_loading -> statefulPagerHelper.show(loadingView.root)
                R.id.end_loading_success -> statefulPagerHelper.showDefaultView()
                R.id.end_loading_empty -> statefulPagerHelper.show(emptyView.root)
                R.id.end_loading_error -> statefulPagerHelper.show(errorView.root)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (statefulEnableConfig != null)
            menuInflater.inflate(R.menu.menu_controls, menu)

        return super.onCreateOptionsMenu(menu)
    }

    /**
     * If subclass returns a [StatefulPagerHelper],
     * [BaseActivity] will create some stateful view, and generate menu items.
     * Through this [StatefulPagerHelper], [BaseActivity] provide default behavior for the user.
     */
    protected open fun onCreateStatefulPagerHelper(): StatefulPagerHelper? {
        return null
    }

    data class StatefulEnableConfig(
        val statefulPagerHelper: StatefulPagerHelper,
        val loadingView: StateLoadingBinding,
        val emptyView: StateErrorOrEmptyBinding,
        val errorView: StateErrorOrEmptyBinding,
    )
}