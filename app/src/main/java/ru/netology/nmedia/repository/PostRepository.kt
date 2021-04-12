package ru.netology.nmedia.repository

import ru.netology.nmedia.model.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Long)
    fun save(post: Post)
    fun removeById(id: Long)
    fun shareById(id: Long)
    fun dislikeById(id: Long)
}