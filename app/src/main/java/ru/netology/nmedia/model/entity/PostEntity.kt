package ru.netology.nmedia.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.model.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val author: String,
    val content: String,
    val datePublished: Long, //String,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val shares: Int = 0
) {
    fun toDto() = Post(
        id = id,
        author = author,
        content = content,
        datePublished = datePublished,
        likedByMe = likedByMe,
        likes = likes,
        shares = shares
    )

    companion object {
        fun  fromDto(dto: Post) = PostEntity(
            id = dto.id,
            author = dto.author,
            content = dto.content,
            datePublished = dto.datePublished,
            likedByMe = dto.likedByMe,
            likes = dto.likes,
            shares = dto.shares
        )
    }
}