package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.model.database.AppDatabase
import ru.netology.nmedia.model.database.AppRoom
import ru.netology.nmedia.model.dto.Post
import ru.netology.nmedia.repository.PostRepositoryFileImpl
import ru.netology.nmedia.repository.PostRepositoryRoomImpl
import ru.netology.nmedia.repository.PostRepositorySqlImpl
import ru.netology.nmedia.repository.RepoPost
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class PostViewModel (application: Application) : AndroidViewModel(application) {

    private val blankPost = Post(
        id = 0L,
        author = "",
        datePublished = "",
        content = ""
    )

//    private val repository = PostRepositoryMemoryImpl()
//    private val repository = PostRepositoryFileImpl(application)

//    private val repository: RepoPost = PostRepositorySqlImpl(
//        AppDatabase.getInstance(application).postDao
//    )

    private val repository: RepoPost = PostRepositoryRoomImpl(
        AppRoom.getInstance(application).postRoomDao()
    )

    var contentEdit = MutableLiveData<Post>()
    val data: LiveData<List<Post>>
        get() = repository.listLiveData

    init {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm")
        val currentDate = Instant.now().atZone(ZoneId.systemDefault())

        contentEdit = MutableLiveData(blankPost.copy(author = "New Author", datePublished = formatter.format(currentDate)))
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)

    fun changeContent(content: String) {
        val text = content.trim()
        if (text == contentEdit.value?.content) return

        if (contentEdit.value?.id == 0L) {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm")
            val currentDate = Instant.now().atZone(ZoneId.systemDefault())
            contentEdit.value = contentEdit.value?.copy(
                author = "New Author", //потом нужно убрать хардкод
                datePublished = formatter.format(currentDate),
                content = text
            )
        } else contentEdit.value = contentEdit.value?.copy(content = text)
    }

    fun changeLinkToVideo(link: String) {
        val text = link.trim()
        if (text == contentEdit.value?.linkToVideo) return
        if (contentEdit.value?.id == 0L) {
            contentEdit.value = contentEdit.value?.copy(linkToVideo = text)
        } else contentEdit.value = contentEdit.value?.copy(linkToVideo = text)
    }

    fun save() {
        contentEdit.value?.let {
            if (it.id == 0L) repository.create(it)
            else repository.update(it)
        }
        contentEdit.value = blankPost
    }

    fun edit(post: Post) {
        contentEdit.value = post
    }
}