package com.example.latihan_lks

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieAdapter
    private val movieList = mutableListOf<Movie>()
    private val filteredMovieList = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MovieAdapter(filteredMovieList) { movie -> onListItemClick(movie) }
        recyclerView.adapter = adapter

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterMovies(newText)
                return true
            }
        })

        fetchMovies()
    }

    private fun onListItemClick(item: Movie) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("extra_item", item)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchMovies() {
        thread {
            try {
                val url = URL("https://api.ptraazxtt.my.id/api/v1/film")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().readText()
                    val jsonObject = JSONObject(response)
                    val results = jsonObject.getJSONArray("films")

                    for (i in 0 until results.length()) {
                        val item = results.getJSONObject(i)
                        val movie = Movie(
                            id = item.getInt("id"),
                            title = item.getString("title"),
                            image = "http://api.ptraazxtt.my.id/storage/image/" + item.getString("image"),
                            genre = item.getString("genre"),
                            description = item.getString("description"),
                            releaseDate = item.getString("release_date")
                        )
                        movieList.add(movie)
                    }
                    filteredMovieList.addAll(movieList)

                    runOnUiThread { adapter.notifyDataSetChanged() }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filterMovies(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            movieList
        } else {
            movieList.filter { it.title.contains(query, true) }
        }
        filteredMovieList.clear()
        filteredMovieList.addAll(filteredList)
        adapter.notifyDataSetChanged()
    }
}
