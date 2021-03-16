package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.model.dao.PostDao
import ru.netology.nmedia.model.dto.Post

class PostRepositorySqlImpl(
    private val dao: PostDao
) : RepoPost {
    private var posts = emptyList<Post>()
    private val mutableLiveData = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        mutableLiveData.value = posts
    }

    override val listLiveData: LiveData<List<Post>>
        get() = mutableLiveData

    override fun likeById(id: Long) {
        posts  = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes.dec() else it.likes.inc()
            )
        }
        mutableLiveData.value = posts
        dao.likeById(id) // синхронизация с БД SQLite
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shares = it.shares.inc())
        }
        mutableLiveData.value = posts
        dao.shareById(id) // синхронизация с БД SQLite
    }



    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        mutableLiveData.value = posts
        dao.removeById(id) // синхронизация с БД SQLite
    }

    override fun create(post: Post) {
        val nextId = dao.create(post)   // синхронизация с БД SQLite и возврат нового id
        posts = posts
            .orEmpty()
            .toMutableList()
            .apply {
                add(post.copy(id = nextId))
            }
        mutableLiveData.value = posts
    }

    override fun update(post: Post) {
        posts = posts
            .orEmpty()
            .map {
                if (it.id == post.id) it.copy(content = post.content) else it
            }
        mutableLiveData.value = posts
        dao.update(post) // синхронизация с БД SQLite
    }
}