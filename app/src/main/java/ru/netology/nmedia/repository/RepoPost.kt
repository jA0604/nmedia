package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.model.Post

interface RepoPost {
    val listLiveData: LiveData<List<Post>>
    fun likeById(id: Long)
    fun shareById(id: Long)
}
