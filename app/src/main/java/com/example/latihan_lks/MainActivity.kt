package com.example.latihan_lks

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
        adapter = MovieAdapter(
            movieList
        ) {
            movie -> onListItemClick(movie)
        }

        recyclerView.adapter = adapter

        fetchMovies()
    }
    fun onListItemClick(item:Movie){
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

                    runOnUiThread { adapter.notifyDataSetChanged() }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}