package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.model.Post

class PostRepository : RepoPost {

    private val post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        datePublished = "21 мая в 18:36"
    )

    private val mutableLiveData = MutableLiveData<Post>(post)
    override val liveData: LiveData<Post>
        get() = mutableLiveData

    override fun like() {
        val currentPost = requireNotNull(mutableLiveData.value)
        mutableLiveData.value = currentPost.copy(
            likedByMe = !currentPost.likedByMe,
            likes = if (currentPost.likedByMe) currentPost.likes.inc() else if (currentPost.likes > 0) currentPost.likes.dec() else 0)
    }

    override fun share() {
        val currentPost = requireNotNull(mutableLiveData.value)
        mutableLiveData.value = currentPost.copy(shares = currentPost.shares.inc())
    }
}