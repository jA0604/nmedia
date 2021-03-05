package ru.netology.nmedia.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.RequestCode
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.viewmodel.PostViewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mViewModel: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val maBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(maBinding.root)

        val appToolbar = maBinding.tbMain
        appToolbar.setTitle(getString(R.string.toolbar_title))
        setSupportActionBar(appToolbar)

        val maViewModel: PostViewModel by viewModels()
        mViewModel = maViewModel

        val adapter = PostListAdapter(
            onPostLiked = { maViewModel.likeById(it.id) },
            onPostShared = { maViewModel.shareById(it.id) },
            onPostRemoved = { maViewModel.removeById(it.id) },
            onPostEdited = { maViewModel.edit(it) },
            onVideoPlay = { videoPlay(it) }

        )

        maBinding.rvPostConteiner.adapter = adapter
        maViewModel.data.observe(this@MainActivity) {
            adapter.submitList(it)
        }

        maBinding.fabAddPost.setOnClickListener {
            val intent = Intent(this, EditPostActivity::class.java)
            startActivityForResult(intent, RequestCode.ADD_POST)
        }


        maViewModel.contentEdit.observe(this@MainActivity) {
            if (it.id == 0L) return@observe
            val intent = Intent(this, EditPostActivity::class.java)

            intent.putExtra(Intent.EXTRA_TEXT, it.content)
            startActivityForResult(intent, RequestCode.EDIT_POST)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.ADD_POST -> {
                if (resultCode != Activity.RESULT_OK) return

                data?.getStringExtra(Intent.EXTRA_TEXT)?.let {
                    mViewModel.changeContent(it)
                    mViewModel.save()
                }
            }
            RequestCode.EDIT_POST -> {
                if (resultCode != Activity.RESULT_OK) return

                data?.getStringExtra(Intent.EXTRA_TEXT)?.let {
                    mViewModel.changeContent(it)
                    mViewModel.save()
                }
            }

        }
    }

    fun videoPlay(post: Post) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(post.linkToVideo)
        }
        startActivity(intent)
    }
}
