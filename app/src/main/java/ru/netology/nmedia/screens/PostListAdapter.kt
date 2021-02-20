package ru.netology.nmedia.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ItemPostBinding
import ru.netology.nmedia.model.Post

class PostListAdapter (
        private val onPostLiked: (Post) -> Unit,
        private val onPostShared: (Post) -> Unit,
        private val onPostRemoved: (Post) -> Unit,
        private val onPostEdited: (Post) -> Unit
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    var posts: List<Post> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemPostBinding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding = itemPostBinding,
                              onPostLiked = onPostLiked,
                              onPostShared = onPostShared,
                              onPostRemoved = onPostRemoved,
                              onPostEdited = onPostEdited)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

}

class PostViewHolder (
    private val binding: ItemPostBinding,
    private val onPostLiked: (Post) -> Unit,
    private val onPostShared: (Post) -> Unit,
    private val onPostRemoved: (Post) -> Unit,
    private val onPostEdited: (Post) -> Unit
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

            ivMore.setOnClickListener {
                val popupMenu = androidx.appcompat.widget.PopupMenu(it.context, it)
                popupMenu.inflate(R.menu.menu_post_options)
                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.edit -> {
                            onPostEdited(post)
                            true
                        }
                        R.id.remove -> {
                            onPostRemoved(post)
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()

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