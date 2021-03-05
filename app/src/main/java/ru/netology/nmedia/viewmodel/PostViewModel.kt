package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.repository.PostRepository
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class PostViewModel : ViewModel() {

    private val blankPost = Post(
        id = 0L,
        author = "",
        datePublished = "",
        content = ""
    )

    private val repository = PostRepository()
    val contentEdit = MutableLiveData(blankPost)
    val data: LiveData<List<Post>>
        get() = repository.listLiveData

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