package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var maBinding: ActivityMainBinding
    lateinit var post: Post
    var likeCount: Int = 0
    var shareCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        maBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(maBinding.root)

        init()
        event()
    }

    private fun init() {
        post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            datePublished = "21 мая в 18:36",
        )

        with(maBinding) {
            tvAuthor.setText(post.author)
            tvPostDate.setText(post.datePublished)
            tvPostContent.setText(post.content)

        }

        setLikeDislike()
        setShare()

    }

    private fun event() {
        maBinding.ivLike.setOnClickListener {
            if (post.likedByMe) likeCount++ else if (likeCount > 0) likeCount-- else 0
            setLikeDislike()
            post.likedByMe = !post.likedByMe
        }

        maBinding.root.setOnClickListener {
            if (post.likedByMe) likeCount++ else if (likeCount > 0) likeCount-- else 0
            setLikeDislike()
        }

        maBinding.ivAvatar.setOnClickListener {
            if (post.likedByMe) likeCount++ else if (likeCount > 0) likeCount-- else 0
            setLikeDislike()
        }

        maBinding.ivShare.setOnClickListener {
            shareCount++
            setShare()
        }

    }

    private fun setLikeDislike() {
        maBinding.ivLike.setImageResource(if (post.likedByMe) R.drawable.ic_baseline_thumb_up_24 else R.drawable.ic_baseline_thumb_down_24)
        maBinding.tvLikeCount.setText(numberToK(likeCount))

    }

    private fun setShare() {
        maBinding.tvShareCount.setText(numberToK(shareCount))
    }

}