package studio.kio.android.stateful

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import studio.kio.android.statefulpager.StatefulPagerHelper

class MainActivity : AppCompatActivity() {

    private lateinit var statefulPagerHelper: StatefulPagerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stateful_page)

        val root = findViewById<ViewGroup>(R.id.root)
        statefulPagerHelper = StatefulPagerHelper(root)

        root.setOnClickListener {
            reload()
        }

    }

    var loading = false

    @SuppressLint("SetTextI18n")
    private fun reload() {

        if (loading) return

        loading = true
        statefulPagerHelper.show {
            inflate(R.layout.state_loading, null)
        }

        //Performing loading
        postDelayed(2000) {

            val stateFlag = Math.random()

            if (stateFlag < 0.5) {
                statefulPagerHelper.showDefaultView()
            } else {
                statefulPagerHelper.show {
                    if (stateFlag < 0.7) {
                        //error
                        inflate(R.layout.state_error_or_empty, null).apply {
                            findViewById<TextView>(R.id.text).text =
                                "Oops, an error occurred during data loading!"
                        }
                    } else {
                        inflate(R.layout.state_error_or_empty, null).apply {
                            findViewById<TextView>(R.id.text).text =
                                "There is nothing to be shown."
                        }
                    }
                }
            }
            loading = false
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