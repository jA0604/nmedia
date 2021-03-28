package ru.netology.nmedia.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessaging
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
