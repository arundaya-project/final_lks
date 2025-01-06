package com.example.latihan_lks

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: MovieAdapter
    private val movieList = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = MovieAdapter(movieList) {
            startActivity(Intent(this, DetailActivity::class.java).putExtra("extra_item", it))
        }

        findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

        findViewById<SearchView>(R.id.searchView).setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?) = filterMovies(newText).run { true }
        })

        fetchMovies()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchMovies() {
        thread {
            val response = try {
                URL("https://api.ptraazxtt.my.id/api/v1/film").readText()
            } catch (e: Exception) {
                e.printStackTrace()
                return@thread
            }

            val results = JSONObject(response).getJSONArray("films")
            for (i in 0 until results.length()) {
                results.getJSONObject(i).run {
                    movieList.add(Movie(getInt("id"), getString("title"), "http://api.ptraazxtt.my.id/storage/image/${getString("image")}", getString("genre"), getString("description"), getString("release_date")))
                }
            }

            runOnUiThread { adapter.notifyDataSetChanged() }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filterMovies(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) movieList else movieList.filter { it.title.contains(query, true) }
        movieList.apply {
            clear()
            addAll(filteredList)
        }
        adapter.notifyDataSetChanged()
    }
}
