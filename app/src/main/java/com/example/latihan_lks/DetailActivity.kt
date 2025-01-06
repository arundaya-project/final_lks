package com.example.latihan_lks

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.net.URL

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        intent.getParcelableExtra<Movie>("extra_item")?.let { movie ->
            findViewById<TextView>(R.id.detailTitle).text = movie.title
            findViewById<TextView>(R.id.detailGenre).text = "Genre: ${movie.genre}"
            findViewById<TextView>(R.id.detailDescription).text = movie.description
            findViewById<TextView>(R.id.detailReleaseDate).text = "Release Date: ${movie.releaseDate}"
            LoadImageTask(findViewById(R.id.detailPoster)).execute(movie.image)
        }
    }

    companion object {
        fun newIntent(context: Context, movie: Movie) = Intent(context, DetailActivity::class.java).apply {
            putExtra("MOVIE_KEY", movie)
        }
    }

    private class LoadImageTask(val imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        override fun doInBackground(vararg params: String?) = try {
            BitmapFactory.decodeStream(URL(params[0]).openConnection().getInputStream())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        override fun onPostExecute(result: Bitmap?) {
            result?.let { imageView.setImageBitmap(it) }
        }
    }
}
