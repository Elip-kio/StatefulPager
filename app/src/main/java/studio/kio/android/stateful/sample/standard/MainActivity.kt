package studio.kio.android.stateful.sample.standard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import studio.kio.android.stateful.R
import studio.kio.android.stateful.databinding.ActivityStatefulPageBinding
import studio.kio.android.stateful.databinding.StateErrorOrEmptyBinding
import studio.kio.android.stateful.databinding.StateLoadingBinding
import studio.kio.android.stateful.sample.recycler.RecyclerActivity
import studio.kio.android.statefulpager.StatefulPagerHelper

class MainActivity : AppCompatActivity() {

    enum class LoadResult {
        SUCCESS, ERROR, EMPTY
    }

    private val binding by lazy {
        ActivityStatefulPageBinding.inflate(layoutInflater)
    }

    private val statefulPagerHelper by lazy {
        StatefulPagerHelper(binding.container)
    }

    //prepare views
    private val loadingBinding by lazy { StateLoadingBinding.inflate(layoutInflater) }
    private val errorAndEmptyBinding by lazy { StateErrorOrEmptyBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //perform loading
        statefulPagerHelper.show(loadingBinding.root)
        lifecycleScope.launch {
            delay(2000)
            when (arrayOf(LoadResult.SUCCESS, LoadResult.EMPTY, LoadResult.ERROR).random()) {
                LoadResult.SUCCESS -> statefulPagerHelper.showDefaultView()
                LoadResult.ERROR -> statefulPagerHelper.show(errorAndEmptyBinding.root)
                LoadResult.EMPTY -> statefulPagerHelper.show(errorAndEmptyBinding.root)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.recycler) {
            startActivity(Intent(this, RecyclerActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}