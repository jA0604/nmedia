package ru.netology.nmedia.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val maBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(maBinding.root)

        val appToolbar = maBinding.tbMain
        appToolbar.setTitle(getString(R.string.toolbar_title))
        setSupportActionBar(appToolbar)

    }
}
