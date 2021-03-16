package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.netology.nmedia.model.dao.PostRoomDao
import ru.netology.nmedia.model.dto.Post
import ru.netology.nmedia.model.entity.PostEntity

class PostRepositoryRoomImpl(
    private val dao: PostRoomDao
) : RepoPost {

    override val listLiveData: LiveData<List<Post>>
        get() = Transformations.map(dao.getAll()) {
            list -> list.map {
                Post(
                    id = it.id,
                    author = it.author,
                    content = it.content,
                    datePublished = it.datePublished,
                    likedByMe = it.likedByMe,
                    likes = it.likes,
                    shares = it.shares,
                    linkToVideo = it.linkToVideo
                )
        }
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

    override fun create(post: Post) {
        dao.create(PostEntity.fromDto(post))
    }

    override fun update(post: Post) {
        dao.updateContenById(post.id, post.content)
    }
}