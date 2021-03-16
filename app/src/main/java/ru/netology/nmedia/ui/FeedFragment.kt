package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.model.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {
    private val ffViewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private lateinit var ffBinding: FragmentFeedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ffBinding = FragmentFeedBinding.inflate(inflater, container, false)
        return ffBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
    }

    private fun initialization() {
        val adapter = PostListAdapter(
            onPostLiked = { ffViewModel.likeById(it.id) },
            onPostShared = { ffViewModel.shareById(it.id) },
            onPostRemoved = { ffViewModel.removeById(it.id) },
            onPostEdited = { onToFragment(it) },
            onVideoPlay = { videoPlay(it) },
            onToFragment = { onToFragment(it) }

            )

        ffBinding.rvPostConteiner.adapter = adapter
        ffViewModel.data.observe(this@FeedFragment) {
            adapter.submitList(it)
        }

        ffBinding.fabAddPost.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }


        ffViewModel.contentEdit.observe(this@FeedFragment) {
            if (it.id == 0L) return@observe

        }
    }

    fun videoPlay(post: Post) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(post.linkToVideo)
        }
        startActivity(intent)
    }

    fun onToFragment(post: Post) {
        ffViewModel.edit(post)
        findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
    }

}