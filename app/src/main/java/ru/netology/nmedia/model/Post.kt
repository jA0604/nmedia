package ru.netology.nmedia.model

data class Post(
        val id: Long = 0L,
        val author: String,
        val content: String,
        val datePublished: String,
        val likedByMe: Boolean = true,
        val likes: Int = 0,
        val shares: Int = 0
)