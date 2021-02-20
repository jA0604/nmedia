package ru.netology.nmedia.screens

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.AndroidUtils.hideKeyboard
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.viewmodel.PostViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val maBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(maBinding.root)

        val maViewModel: PostViewModel by viewModels()
        val adapter = PostListAdapter(
                onPostLiked = { maViewModel.likeById(it.id) },
                onPostShared = { maViewModel.shareById(it.id) },
                onPostRemoved = { maViewModel.removeById(it.id) },
                onPostEdited = { maViewModel.edit(it) }
        )


        maBinding.rvPostConteiner.adapter = adapter
        maViewModel.data.observe(this@MainActivity) {
            adapter.submitList(it)
        }

        maViewModel.contentEdit.observe(this@MainActivity) {
            if (it.id == 0L) return@observe
            maBinding.edContentPost.setText(it.content)

        }

        maBinding.ivSave.setOnClickListener {
            with(maBinding.edContentPost) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(this@MainActivity, context.getString(R.string.error_emty_content), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                maViewModel.changeContent(text.toString())
                maViewModel.save()
                setText("")
                clearFocus()
                it.hideKeyboard()
            }
        }


        maBinding.edContentPost.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                maBinding.ivCancel.visibility = View.VISIBLE
            else
                maBinding.ivCancel.visibility = View.GONE
        }

        maBinding.ivCancel.setOnClickListener {
            with(maBinding.edContentPost) {
                if (!text.isNullOrBlank()) setText("")
                clearFocus()
                it.hideKeyboard()
            }
        }
    }

}