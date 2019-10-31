package id.scode.kadeooredoo.submission.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.scode.kadeooredoo.R
import id.scode.kadeooredoo.submission.RvFootballAdapter
import id.scode.kadeooredoo.submission.data.db.pojo.ItemClubFootball
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * @Authors scode | Yogi Arif Widodo
 * Created on 31 10/31/19 1:31 PM 2019
 * id.scode.kadeooredoo.submission.ui
 * https://github.com/yogithesymbian
 * Android Studio 3.5.1
 * Build #AI-191.8026.42.35.5900203, built on September 26, 2019
 * JRE: 1.8.0_202-release-1483-b49-5587405 amd64
 * JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
 * Linux 5.2.0-kali3-amd64
 */

class MainActivity : AppCompatActivity(){
    /**
     * Declare recycler and mutableList
     */
    private var itemClubFootballs: MutableList<ItemClubFootball> = mutableListOf()
    private lateinit var recyclerViewFootball: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create layout
        verticalLayout {

            padding = dip(16)
            recyclerViewFootball = recyclerView()  // assign the layout for initialize recycler

        }
        initData() // set Data from string.xml

        /**
         * set layout for recycler
         * set the adapter with RecyclerViewFootballAdapter with context and mutableList
         * where the mutableList is alr set from initData
         * and give the listener for action
         */
        recyclerViewFootball.layoutManager = LinearLayoutManager(this)
        recyclerViewFootball.adapter = RvFootballAdapter(
            this,
            itemClubFootballs
        ){
            toast("${it.nameClubFootball}")
        }

    }

    private fun initData() {
        /**
         * Initialize the dataItemFootballLeague
         */
        val idFootball = resources.getIntArray(R.array.club_id)
        val nameFootball = resources.getStringArray(R.array.club_name)
        val descFootball = resources.getStringArray(R.array.club_desc)
        val imageFootball = resources.obtainTypedArray(R.array.club_image)

        itemClubFootballs.clear() //clear the recycler | duplicate when alr load

        // for assign data with loop | where i..length of item data nameFootball
        for (i in nameFootball.indices) {
            itemClubFootballs.add(
                ItemClubFootball(
                    idFootball[i],
                    nameFootball[i],
                    descFootball[i],
                    imageFootball.getResourceId(i, 0)
                )
            )
        }
        // recycle the imageFootball
        imageFootball.recycle()
    }

}
