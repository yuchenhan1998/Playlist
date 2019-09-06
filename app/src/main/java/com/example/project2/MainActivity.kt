package com.example.project2

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import android.widget.Button
import com.example.project2.Fragments.Friends
import com.example.project2.Fragments.NoConnectionFragment
import com.example.project2.Fragments.PlayList
import com.example.project2.Fragments.Search
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val fm = this.supportFragmentManager!!
    var net: Boolean = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo == null) {
            Log.e("NETWORK", "Not Connected. Please check your network settings")
            net = false
        } else {
            net = true;
        }
        // Load Fragment into View
        val fragmentAdapter = MyPagerAdapter(supportFragmentManager, this, net)

        viewpager_main.adapter = fragmentAdapter

        tabs.setupWithViewPager(viewpager_main)
    }

   // set up the tap view
    class MyPagerAdapter(fm: FragmentManager, context: Context, private var Network: Boolean) : FragmentPagerAdapter(fm) {
        private val parentContext = context;

        override fun getItem(position: Int): Fragment {
            if (!this.Network) {
                return NoConnectionFragment()
            }
            return when (position) {
                0 -> {
                    Search(this.parentContext)
                }
                1 -> {
                    PlayList(this.parentContext)
                }
                else -> {
                    Friends(this.parentContext)
                }
            }
        }

        override fun getCount(): Int {
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                0 -> "Home"
                1 -> "PlayList"
                else -> {
                    return "Friends"
                }
            }
        }
    }
}



