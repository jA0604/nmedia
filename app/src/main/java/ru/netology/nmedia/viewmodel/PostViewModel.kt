package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

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
        author = "Me",
        datePublished = 0L,
        authorAvatar = "netology.jpg",
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
        _data.postValue(FeedModel(loading = true))
        repository.getAllAsync(object : PostRepository.GetAllCallback {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }


    fun likeById(id: Long) {
        val old = _data.value?.posts.orEmpty()

        if (_data.value?.posts.orEmpty().filter { it.id == id }.last().likedByMe )
            repository.dislikeByIdAsync(id, object : PostRepository.ByIdCallback {
                override fun onSuccess() {
                    _postChanged.postValue(Unit)
                }

                override fun onError(e: Exception) {
                    _data.postValue(_data.value?.copy(posts = old))
                }
            })
        else repository.likeByIdAsync(id, object : PostRepository.ByIdCallback {
            override fun onSuccess() {
                _postChanged.postValue(Unit)
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        })

    }

    fun shareById(id: Long) {
        val old = _data.value?.posts.orEmpty()

            repository.shareByIdAsync(id, object : PostRepository.ByIdCallback {
                override fun onSuccess() {
                    _postChanged.postValue(Unit)
                }

                override fun onError(e: Exception) {
                    _data.postValue(_data.value?.copy(posts = old))
                }
            })
    }

    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        _data.postValue(
            _data.value?.copy(posts = _data.value?.posts.orEmpty()
                .filter { it.id != id }
            )
        )
        repository.removeByIdAsync(id, object : PostRepository.ByIdCallback {
            override fun onSuccess() {

            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        })


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

            repository.saveAsync(it, object : PostRepository.ByIdCallback {
                override fun onSuccess() {
//                    repository.saveAsync(it, )
                    _postCreated.postValue(Unit)
                }

                override fun onError(e: Exception) {

                }
            })
        }
        contentEdit.value = blankPost
    }

    fun edit(post: Post) {
        contentEdit.value = post
    }

}