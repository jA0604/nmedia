package ru.netology.nmedia.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ItemPostBinding
import ru.netology.nmedia.model.Post

class PostListAdapter(
    private val onPostLiked: (Post) -> Unit,
    private val onPostShared: (Post) -> Unit,
    private val onPostRemoved: (Post) -> Unit,
    private val onPostEdited: (Post) -> Unit,
    private val onVideoPlay: (Post) -> Unit,
    private val onToFragment:(Post) -> Unit
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    var posts: List<Post> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemPostBinding =
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(
            binding = itemPostBinding,
            onPostLiked = onPostLiked,
            onPostShared = onPostShared,
            onPostRemoved = onPostRemoved,
            onPostEdited = onPostEdited,
            onVideoPlay = onVideoPlay,
            onToFragment = onToFragment
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: ItemPostBinding,
    private val onPostLiked: (Post) -> Unit,
    private val onPostShared: (Post) -> Unit,
    private val onPostRemoved: (Post) -> Unit,
    private val onPostEdited: (Post) -> Unit,
    private val onVideoPlay: (Post) -> Unit,
    private val onToFragment:(Post) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            ivAvatar.setImageResource(R.drawable.ic_netology_original)
            tvAuthor.setText(post.author)
            tvPostDate.setText(post.datePublished)
            tvPostContent.setText(post.content)

            ivYoutube.visibility = if (post.linkToVideo.isBlank()) View.GONE else View.VISIBLE

            ivLike.isChecked = post.likedByMe
            ivLike.text = post.likes.toString()
            ivLike.setOnClickListener {
                onPostLiked(post)
            }

            ivShare.text = "${post.shares}"
            ivShare.setOnClickListener {
                onPostShared(post)
            }

            ivMore.setOnClickListener {
                val popupMenu = androidx.appcompat.widget.PopupMenu(it.context, it)
                popupMenu.inflate(R.menu.menu_post_options)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
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

            ivYoutube.setOnClickListener {
                onVideoPlay(post)
            }

            root.setOnClickListener {
                onToFragment(post)
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}