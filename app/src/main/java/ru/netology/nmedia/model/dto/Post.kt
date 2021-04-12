package ru.netology.nmedia.model.dto


data class Post(
        val id: Long = 0L,
        val author: String,
        val content: String,
        val datePublished: Long, //String,
        val likedByMe: Boolean = false,
        val likes: Int = 0,
        val shares: Int = 0
)