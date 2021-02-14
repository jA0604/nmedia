package ru.netology.nmedia.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.screens.PostListAdapter
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val maBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(maBinding.root)

        val maViewModel: PostViewModel by viewModels()
        val adapter = PostListAdapter (
                {maViewModel.likeById(it.id)},
                {maViewModel.shareById(it.id)}
        )


        maBinding.rvPostConteiner.adapter = adapter
        maViewModel.data.observe(this@MainActivity) {
            adapter.submitList(it)
        }

    }

}