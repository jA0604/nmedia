package ru.netology.nmedia.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.nmedia.model.dao.PostDao
import ru.netology.nmedia.model.dao.PostRoomDao
import ru.netology.nmedia.model.entity.PostEntity

@Database(entities = [PostEntity::class], version = 1)
abstract class AppRoom : RoomDatabase() {
    abstract fun postRoomDao(): PostRoomDao

    companion object {
        @Volatile
        private var instance: AppRoom? = null

        fun getInstance(context: Context): AppRoom {
            return instance ?: synchronized(this) {
                instance ?: buidDatabase(context).also { instance = it }
            }
        }

        private fun buidDatabase(context: Context) =
            Room.databaseBuilder(context, AppRoom::class.java, "roompost.db")
                .allowMainThreadQueries()
                .build()
    }


}