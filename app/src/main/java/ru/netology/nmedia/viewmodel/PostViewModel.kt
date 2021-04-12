package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.netology.nmedia.SingleLiveEvent
import ru.netology.nmedia.model.database.AppDatabase
import ru.netology.nmedia.model.database.AppRoom
import ru.netology.nmedia.model.dto.FeedModel
import ru.netology.nmedia.model.dto.Post
import ru.netology.nmedia.repository.*
import java.io.IOException
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

class PostViewModel (application: Application) : AndroidViewModel(application) {

    private val blankPost = Post(
        id = 0L,
        author = " ",
        datePublished = 0L, //" ",
        content = " ",
        likedByMe = false,
        likes = 0,
        shares = 0
    )

    private val repository: PostRepository = PostRepositorySpringImpl()
    private val _data = MutableLiveData(FeedModel())

    var contentEdit = MutableLiveData(blankPost)

    val data: MutableLiveData<FeedModel>
        get() = _data

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _postChanged = SingleLiveEvent<Unit>()
    val postChanged: LiveData<Unit>
        get() = _postChanged


    init {
        loadPosts()
    }

    fun loadPosts() {
        thread {
            _data.postValue(FeedModel(loading = true))
            try {
                val posts = repository.getAll()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: IOException) {
                FeedModel(error = true)
            }.also(_data::postValue)
        }
    }

    fun likeById(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            try {
                val b = _data.value?.posts.orEmpty()
                    .filter { it.id == id }
                    .last().likedByMe

                if (_data.value?.posts.orEmpty()
                        .filter { it.id == id }
                        .last().likedByMe
                ) repository.dislikeById(id)
                else repository.likeById(id)
                _postChanged.postValue(Unit)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }

    fun shareById(id: Long) {
        thread { repository.shareById(id) }
    }

    fun removeById(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .filter { it.id != id }
                )
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }

    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (text == contentEdit.value?.content) return
        else {
            val post = contentEdit.value?.copy(content = text)
            contentEdit.value = post
        }
    }

    fun save() {
        contentEdit.value?.let {
            thread {
                repository.save(it)
                _postCreated.postValue(Unit)
            }

        }
        contentEdit.value = blankPost
    }

    fun edit(post: Post) {
        contentEdit.value = post
    }
}