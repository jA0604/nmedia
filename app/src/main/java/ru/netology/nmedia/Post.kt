package ru.netology.nmedia

data class Post(
        val id: Long,
        val author: String,
        val content: String,
        val datePublished: String,
        var likedByMe: Boolean = true
//        var likeCount: Int = 0,
//        var dislikeCount: Int = 0,
//        var shareCount: Int = 0,
)