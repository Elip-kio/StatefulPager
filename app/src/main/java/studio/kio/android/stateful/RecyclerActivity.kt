package studio.kio.android.stateful

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import studio.kio.android.statefulpager.StatefulPagerHelper

class RecyclerActivity : AppCompatActivity() {
    private lateinit var statefulPagerHelper: StatefulPagerHelper
    private lateinit var recycler: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        recycler = findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = object : RecyclerView.Adapter<SampleViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): SampleViewHolder = SampleViewHolder(TextView(parent.context))

            @SuppressLint("SetTextI18n")
            override fun onBindViewHolder(holder: SampleViewHolder, position: Int) {
                holder.text.text = "$position . This is sample line"
            }

            override fun getItemCount(): Int = 50

        }
        recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        this.statefulPagerHelper = StatefulPagerHelper(recycler)
    }

    private class SampleViewHolder(val text: TextView) : RecyclerView.ViewHolder(text)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recycler_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("SetTextI18n")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.start_loading -> {
                statefulPagerHelper.show {
                    inflate(R.layout.state_loading, null)
                }
            }
            R.id.end_loading_success -> {
                statefulPagerHelper.showDefaultView()
            }
            R.id.end_loading_empty -> {
                statefulPagerHelper.show {
                    inflate(R.layout.state_error_or_empty, null).apply {
                        findViewById<TextView>(R.id.text).text = "There is nothing to be shown."
                    }
                }
            }
            R.id.end_loading_error -> {
                statefulPagerHelper.show {
                    inflate(R.layout.state_error_or_empty, null).apply {
                        findViewById<TextView>(R.id.text).text =
                            "Oops, an error occurred during data loading!"
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}