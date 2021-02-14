package ru.netology.nmedia.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ItemPostBinding
import ru.netology.nmedia.model.Post

class PostListAdapter (
        private val onPostLiked: (Post) -> Unit,
        private val onPostShared: (Post) -> Unit
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    var posts: List<Post> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemPostBinding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding = itemPostBinding,
                              onPostLiked = onPostLiked,
                              onPostShared = onPostShared)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

}

class PostViewHolder (
    private val binding: ItemPostBinding,
    private val onPostLiked: (Post) -> Unit,
    private val onPostShared: (Post) -> Unit
        ) : RecyclerView.ViewHolder(binding.root) {

    fun bind (post: Post) {
        with(binding) {
            ivAvatar.setImageResource(R.drawable.ic_netology_original)
            tvAuthor.setText(post.author)
            tvPostDate.setText(post.datePublished)
            tvPostContent.setText(post.content)

            ivLike.setImageResource(if (post.likedByMe) R.drawable.ic_baseline_thumb_up_24 else R.drawable.ic_baseline_thumb_down_24)
            tvLikeCount.setText(post.likes.toString())
            ivLike.setOnClickListener {
                onPostLiked(post)
            }

            tvShareCount.setText(post.shares.toString())
            ivShare.setOnClickListener {
                onPostShared(post)
            }
        }
    }
}

class PostDiffCallback: DiffUtil.ItemCallback<Post> () {

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}