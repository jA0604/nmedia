package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.model.dto.Post

interface RepoPost {
    val listLiveData: LiveData<List<Post>>
    fun likeById(id: Long)
    fun shareById(id: Long)

//    fun save(post: Post)
    fun removeById(id: Long)

    fun create(post: Post)
    fun update(post: Post)
}
