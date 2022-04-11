package main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import main.Adapter.FeedAdapter
import main.`interface`.ItemClickListener
import main.api.OkHttpRequest
import main.model.Item
import main.model.RSSObject
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import science.example.myrssreader.R
import java.io.IOException

class MainActivity: AppCompatActivity(), ItemClickListener {
    companion object {
        private const val RSS_LINK = "https://ria.ru/export/rss2/archive/index.xml"
        private const val RSS_TO_JSON_API = "http://api.rss2json.com/v1/api.json?rss_url="
    }

    private val feedAdapter = FeedAdapter(mutableListOf(), this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        toolbar.title="NEWS"
//        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavigationHostFragment) as? NavHostFragment
            ?: return
        val navController = findNavController(R.id.mainNavigationHostFragment)

        val mainBottomNavigationView = findViewById<BottomNavigationView>(R.id.mainBottomNavigation)
        mainBottomNavigationView.setupWithNavController(navController)


//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = feedAdapter

        loadRSS()
    }
    private fun loadRSS() {
        val endPointUrl = RSS_TO_JSON_API + RSS_LINK

        val okHttpRequest = OkHttpRequest()
        okHttpRequest.getRSS(endPointUrl, object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                runOnUiThread {
                    if (responseData != null) {
                        val rssObject = convertToObject(responseData)
                        updateRecyclerView(rssObject.items ?: mutableListOf())
                    } else {
                        Toast.makeText(this@MainActivity,
                            "Возникла ошибка при обработке данных полученых с сервера", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity,
                        "Возникла ошибка при подключении к удаленному серверу", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun convertToObject(response: String): RSSObject {
        return Gson().fromJson(response, RSSObject::class.java)
    }

    private fun updateRecyclerView(items: List<Item>) {
        feedAdapter.updateList(items)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.menu_refresh) {
//            loadRSS()
//            return true
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

    override fun onItemClick(item: Item) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(item.link ?: "")
        startActivity(intent)
    }
}