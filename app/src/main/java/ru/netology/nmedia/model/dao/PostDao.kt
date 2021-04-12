package ru.netology.nmedia.model.dao

import ru.netology.nmedia.model.dto.Post

interface PostDao {
    fun getAll(): List<Post>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun create(post: Post): Long
    fun update(post: Post)
}