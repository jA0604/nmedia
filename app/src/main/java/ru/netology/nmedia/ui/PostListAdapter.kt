package ru.netology.nmedia.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ItemPostBinding
import ru.netology.nmedia.model.dto.Post
import ru.netology.nmedia.repository.BASE_URL

class PostListAdapter(
    private val onPostLiked: (Post) -> Unit,
    private val onPostShared: (Post) -> Unit,
    private val onPostRemoved: (Post) -> Unit,
    private val onPostEdited: (Post) -> Unit,
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
    private val onToFragment:(Post) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            ivAvatar.setImageResource(R.drawable.ic_netology_original)
            tvAuthor.setText(post.author)
            tvPostDate.setText(post.datePublished.toString())
            tvPostContent.setText(post.content)

            ivLike.isChecked = post.likedByMe
            ivLike.text = post.likes.toString()
            ivLike.setOnClickListener {
                onPostLiked(post)
            }

            ivShare.text = "${post.shares}"
            ivShare.setOnClickListener {
                onPostShared(post)
            }

            Glide.with(ivAvatar)
                .load("${BASE_URL}/avatars/${post.authorAvatar}")
                .placeholder(R.drawable.ic_loading_100dp)
                .error(R.drawable.ic_error_100dp)
                .timeout(10_000)
                .circleCrop()
                .into(ivAvatar)

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