package com.example.latihan_lks

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.latihan_lks.R

class BiodataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biodata)

        findViewById<TextView>(R.id.biodataText).text = "Dibuat oleh: Nama Anda\nEmail: email@domain.com"
    }
}
