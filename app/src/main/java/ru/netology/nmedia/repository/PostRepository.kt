package ru.netology.nmedia.repository

import ru.netology.nmedia.model.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Long)
    fun save(post: Post)
    fun removeById(id: Long)
    fun shareById(id: Long)
    fun dislikeById(id: Long)

    fun getAllAsync(callback: GetAllCallback)
    interface GetAllCallback {
        fun onSuccess(posts: List<Post>) {}
        fun onError(code: Int, e: Exception) {}
    }

    fun likeByIdAsync(id: Long, callback: ByIdCallback)
    fun dislikeByIdAsync(id: Long, callback: ByIdCallback)

    fun removeByIdAsync(id: Long, callback: ByIdCallback)
    fun shareByIdAsync(id: Long, callback: ByIdCallback)

    fun saveAsync(post: Post, callback: ByIdCallback)

    interface ByIdCallback {
        fun onSuccess() {}
        fun onError(code: Int, e: Exception) {}
    }
}