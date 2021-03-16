package ru.netology.nmedia.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.model.entity.PostEntity

@Dao
interface PostRoomDao {
    @Query("SELECT * FROM PostEntity ORDER BY id ASC")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun create(post: PostEntity)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Long)

    @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
    fun updateContenById(id: Long, content: String)

    @Query("""
        UPDATE PostEntity SET 
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id    
    """)
    fun likeById(id: Long)

    @Query("""
        UPDATE PostEntity SET 
        shares = shares + 1
        WHERE id = :id    
    """)
    fun shareById(id: Long)


}