package studio.kio.android.stateful

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import studio.kio.android.stateful_pager.StatefulPageHelper

class MainActivity : AppCompatActivity() {

    private lateinit var statefulPageHelper: StatefulPageHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stateful_page)

        val root = findViewById<ViewGroup>(R.id.root)
        statefulPageHelper = StatefulPageHelper(root)

        root.setOnClickListener {
            reload()
        }

    }

    var loading = false

    @SuppressLint("SetTextI18n")
    private fun reload() {

        if (loading) return

        loading = true
        statefulPageHelper.showMinor {
            inflate(R.layout.state_loading, null)
        }

        //Performing loading
        postDelayed(2000) {

            val stateFlag = Math.random()

            if (stateFlag < 0.5) {
                statefulPageHelper.showMajor()
            } else {
                statefulPageHelper.showMinor {
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

}