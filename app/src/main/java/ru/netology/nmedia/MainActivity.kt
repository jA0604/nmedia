package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    lateinit var maBinding: ActivityMainBinding
    val maViewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        maBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(maBinding.root)

        init()
        event()
    }

    private fun init() {
        val post = requireNotNull(maViewModel.data.value)
        with(maBinding) {
            tvAuthor.setText(post.author)
            tvPostDate.setText(post.datePublished)
            tvPostContent.setText(post.content)
        }

        setLikeDislike(post)
        setShare(post)

    }

    private fun event() {

        with(maBinding) {
            ivLike.setOnClickListener {
                maViewModel.like()

            }
            ivShare.setOnClickListener {
                maViewModel.share()
            }
        }

        maViewModel.data.observe(this@MainActivity) {
            setLikeDislike(it)
            setShare(it)
        }

    }

    private fun setLikeDislike(post: Post) {
        with(maBinding) {
            ivLike.setImageResource(if (post.likedByMe) R.drawable.ic_baseline_thumb_up_24 else R.drawable.ic_baseline_thumb_down_24)
            tvLikeCount.setText(numberToK(post.likes))
        }

    }

    private fun setShare(post: Post) {
        maBinding.tvShareCount.setText(numberToK(post.shares))
    }

}