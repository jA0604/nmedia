package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.repository.PostRepository

class PostViewModel : ViewModel() {

    private val repository = PostRepository()
    val data: LiveData<List<Post>>
        get() = repository.listLiveData
    fun likeById(id: Long) {
        repository.likeById(id)
    }
    fun shareById(id: Long) {
        repository.shareById(id)
    }

}