package ru.netology.nmedia.model.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.model.dto.Post

class PostDaoImpl(
    private val db: SQLiteDatabase
) : PostDao {

    companion object {
        val sqlRequestOnCreateDb = """
        CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_DATE_PUBLISHED} TEXT NOT NULL,
            ${PostColumns.COLUMN_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LIKES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LINK_TO_VIDEO} TEXT DEFAULT NULL
        );
        """.trimIndent()
    }


    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_DATE_PUBLISHED = "datePublished"
        const val COLUMN_LIKED_BY_ME = "likedByMe"
        const val COLUMN_LIKES = "likes"
        const val COLUMN_SHARES = "shares"
        const val COLUMN_LINK_TO_VIDEO = "linkToVideo"

        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_DATE_PUBLISHED,
            COLUMN_LIKED_BY_ME,
            COLUMN_LIKES,
            COLUMN_SHARES,
            COLUMN_LINK_TO_VIDEO
        )
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} ASC" //"${PostColumns.COLUMN_ID} DESC" - если нужно по убыванию id,
        ).use {
            while (it.moveToNext()) {
                posts.add(mapToPost(it))
            }
        }
        return posts
    }


    override fun likeById(id: Long) {
        db.execSQL(
            """
                UPDATE posts SET
                    likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
                    likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
                WHERE id = ?;    
            """.trimIndent(), arrayOf(id)
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
                UPDATE posts SET
                    shares = shares + 1
                WHERE id = ?;    
            """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun create(post: Post): Long {
        val values = ContentValues().apply {
            put(PostColumns.COLUMN_AUTHOR, post.author)
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_DATE_PUBLISHED, post.datePublished)
            put(PostColumns.COLUMN_LIKED_BY_ME, if (post.likedByMe) 1 else 0)
            put(PostColumns.COLUMN_LIKES, post.likes)
            put(PostColumns.COLUMN_SHARES, post.shares)
            put(PostColumns.COLUMN_LINK_TO_VIDEO, post.linkToVideo)
        }
        db.insert(PostColumns.TABLE, null, values)

        var newPost: Post
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC",
            "1"
        ).use {
                it.moveToNext()
                newPost = mapToPost(it)
        }
       return newPost.id
    }

    override fun update(post: Post) {
        val values = ContentValues().apply {
            put(PostColumns.COLUMN_ID, post.id)
            put(PostColumns.COLUMN_AUTHOR, post.author)
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_DATE_PUBLISHED, post.datePublished)
            put(PostColumns.COLUMN_LIKED_BY_ME, if (post.likedByMe) 1 else 0)
            put(PostColumns.COLUMN_LIKES, post.likes)
            put(PostColumns.COLUMN_SHARES, post.shares)
            put(PostColumns.COLUMN_LINK_TO_VIDEO, post.linkToVideo)
        }
        db.update(PostColumns.TABLE, values, "${PostColumns.COLUMN_ID} = ?", arrayOf(post.id.toString()))
    }

    private fun mapToPost(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                datePublished = getString(getColumnIndexOrThrow(PostColumns.COLUMN_DATE_PUBLISHED)),
                likedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                likes = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
                shares = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARES)),
                linkToVideo = getString(getColumnIndexOrThrow(PostColumns.COLUMN_LINK_TO_VIDEO))
            )
        }
    }

}