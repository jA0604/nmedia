package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.model.Post

interface RepoPost {
    val liveData: LiveData<Post>
    fun like()
    fun share()
}