package ru.netology.nmedia.screens

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityEditPostBinding

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val epBinding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(epBinding.root)

        val appToolbar = epBinding.tbEditPost
        appToolbar.setTitle(getString(R.string.toolbar_title))
        appToolbar.setSubtitle(getString(R.string.tb_edit_post_subtitle))
        setSupportActionBar(appToolbar)

        epBinding.edContentPost.setText(intent.getStringExtra(Intent.EXTRA_TEXT))

        epBinding.fabSave.setOnClickListener {
            val intent = Intent()
            if (epBinding.edContentPost.text.isBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = epBinding.edContentPost.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }

    }
}