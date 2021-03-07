package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.model.Post

class PostRepositoryFileImpl (
    private val context: Context
) : RepoPost {
    private val gson = Gson()
    private val tokenType = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val filename = "posts.json"
    private var ids = 1L
    private var posts = emptyList<Post>()
    private val mutableLiveData = MutableLiveData(posts)

    override val listLiveData: LiveData<List<Post>>
        get() = mutableLiveData

    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()) {
            context.openFileInput(filename).bufferedReader().use {
                posts = gson.fromJson(it, tokenType)
                mutableLiveData.value = posts
                ids = posts.last().id + 1
            }
        } else {
            sync()
        }
    }

    override fun likeById(id: Long) {
        posts  = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes.dec() else it.likes.inc()
            )
        }
        mutableLiveData.value = posts
        sync()
    }

    override fun shareById(id: Long) {
         posts = posts.map {
             if (it.id != id) it else it.copy(shares = it.shares.inc())
         }
        mutableLiveData.value = posts
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        mutableLiveData.value = posts
        sync()
    }

    override fun create(post: Post) {
        posts = posts
            .orEmpty()
            .toMutableList()
            .apply {
                add(post.copy(id = ids++))
            }
        mutableLiveData.value = posts
        sync()
    }

    override fun update(post: Post) {
        posts = posts
            .orEmpty()
            .map {
                if (it.id == post.id) it.copy(content = post.content) else it
            }
        mutableLiveData.value = posts
        sync()
    }

    override fun save(post: Post) {
        posts = posts.filter { it.id != post.id }
        sync()
    }

    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE/*APPEND*/).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

}