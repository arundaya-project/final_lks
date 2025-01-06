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

        val movie = intent.getParcelableExtra<Movie>("extra_item")

        findViewById<TextView>(R.id.detailTitle).text = movie?.title
        findViewById<TextView>(R.id.detailGenre).text = "Genre: ${movie?.genre}"
        findViewById<TextView>(R.id.detailDescription).text = movie?.description
        findViewById<TextView>(R.id.detailReleaseDate).text = "Release Date: ${movie?.releaseDate}"
        LoadImageTask(findViewById(R.id.detailPoster)).execute(movie?.image)
    }

    companion object {
        private const val MOVIE_KEY = "MOVIE_KEY"

        fun newIntent(context: Context, movie: Movie): Intent {
            return Intent(context, DetailActivity::class.java).apply {
                putExtra(MOVIE_KEY, movie)
            }
        }
    }

    private class LoadImageTask(val imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        override fun doInBackground(vararg params: String?): Bitmap? {
            return try {
                val url = URL(params[0])
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            result?.let { imageView.setImageBitmap(it) }
        }
    }
}