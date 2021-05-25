package ru.netology.nmedia.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import ru.netology.nmedia.SERVER_URL
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.model.dto.Post
import java.util.concurrent.TimeUnit

private const val URL = "${SERVER_URL}/api/slow/"

class PostRepositorySpringImpl : PostRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAll(): List<Post> {
        val request: Request = Request.Builder()
            .url("${URL}posts")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typeToken.type)
            }
    }

    override fun save(post: Post) {
        val request: Request = Request.Builder()
                .post(gson.toJson(post).toRequestBody(jsonType))
                .url("${URL}posts")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
        .delete()
        .url("${URL}posts/$id")
        .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun likeById(id: Long) {
        val request: Request = Request.Builder()
            .post(gson.toJson(id).toRequestBody(jsonType))
            .url("${URL}posts/$id/likes")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun dislikeById(id: Long) {
        val request: Request = Request.Builder()
            .delete(gson.toJson(id).toRequestBody(jsonType))
            .url("${URL}posts/$id/likes")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun shareById(id: Long) {
        TODO("Not yet implemented")
    }



    override fun getAllAsync(callback: PostRepository.GetAllCallback) {

        PostsApi.retrofitService.getAll().enqueue(object : retrofit2.Callback<List<Post>> {
            override fun onResponse(
                call: retrofit2.Call<List<Post>>,
                response: retrofit2.Response<List<Post>>
            ) {
                when (response.code()) {
                    in 200..299 -> {
                        callback.onSuccess(response.body() ?: throw RuntimeException("body is null") )
                    }
                    in 400..499 -> {
                        Log.e("HTTPERROR", "${response.code()}  " + response.message() + response.raw().request.url)
                        callback.onError(response.code(), RuntimeException(response.message()))
                    }
                    in 500..599 -> {
                        Log.e("HTTPERROR", "${response.code()}  " + response.message() + response.raw().request.url)
                        callback.onError(response.code(), RuntimeException(response.message()))
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Post>>, t: Throwable) {
                callback.onError(0, RuntimeException(t.message))
            }

        }
        )
    }

    override fun likeByIdAsync(id: Long, callback: PostRepository.ByIdCallback) {
        PostsApi.retrofitService.likeById(id).enqueue(object: retrofit2.Callback<Post> {
            override fun onResponse(
                call: retrofit2.Call<Post>,
                response: retrofit2.Response<Post>
            ) {
                if (response.isSuccessful) {
                    callback.onSuccess()
                } else {
                    callback.onError(response.code(), RuntimeException(response.message()))
                }
            }

            override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                callback.onError(0, RuntimeException(t.message))
            }

        })
    }

    override fun dislikeByIdAsync(id: Long, callback: PostRepository.ByIdCallback) {

        PostsApi.retrofitService.dislikeById(id).enqueue(object: retrofit2.Callback<Post> {
            override fun onResponse(
                call: retrofit2.Call<Post>,
                response: retrofit2.Response<Post>
            ) {
                when (response.code()) {
                    in 200..299 -> {
                        callback.onSuccess()
                    }
                    in 400..499 -> {
                        Log.e("HTTPERROR", "${response.code()}  " + response.message() + response.raw().request.url)
                        callback.onError(response.code(), RuntimeException(response.message()))
                    }
                    in 500..599 -> {
                        Log.e("HTTPERROR", "${response.code()}  " + response.message() + response.raw().request.url)
                        callback.onError(response.code(), RuntimeException(response.message()))
                    }
                }

            }

            override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                callback.onError(0, RuntimeException(t.message))
            }

        })
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.ByIdCallback) {
        PostsApi.retrofitService.removeById(id).enqueue(object: retrofit2.Callback<Unit> {
            override fun onResponse(
                call: retrofit2.Call<Unit>,
                response: retrofit2.Response<Unit>
            ) {

                when (response.code()) {
                    in 200..299 -> {
                        callback.onSuccess()
                    }
                    in 400..499 -> {
                        Log.e("HTTPERROR", "${response.code()}  " + response.message() + response.raw().request.url)
                        callback.onError(response.code(), RuntimeException(response.message()))
                    }
                    in 500..599 -> {
                        Log.e("HTTPERROR", "${response.code()}  " + response.message() + response.raw().request.url)
                        callback.onError(response.code(), RuntimeException(response.message()))
                    }
                }


                if (response.isSuccessful) {
                    callback.onSuccess()
                } else {
                    callback.onError(response.code(), RuntimeException(response.message()))
                }
            }

            override fun onFailure(call: retrofit2.Call<Unit>, t: Throwable) {
                callback.onError(0, RuntimeException(t.message))
            }
        })
    }

    override fun shareByIdAsync(id: Long, callback: PostRepository.ByIdCallback) {
        PostsApi.retrofitService.shareById(id).enqueue(object : retrofit2.Callback<Post> {
            override fun onResponse(
                call: retrofit2.Call<Post>,
                response: retrofit2.Response<Post>
            ) {
                when (response.code()) {
                    in 200..299 -> {
                        callback.onSuccess()
                    }
                    in 400..499 -> {
                        Log.e("HTTPERROR", "${response.code()}  " + response.message() + response.raw().request.url)
                        callback.onError(response.code(), RuntimeException(response.message()))
                    }
                    in 500..599 -> {
                        Log.e("HTTPERROR", "${response.code()}  " + response.message() + response.raw().request.url)
                        callback.onError(response.code(), RuntimeException(response.message()))
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                callback.onError(0, RuntimeException(t.message))
            }

        })
    }


    override fun saveAsync(post: Post, callback: PostRepository.ByIdCallback) {

        PostsApi.retrofitService.save(post).enqueue(object: retrofit2.Callback<Post> {
            override fun onResponse(
                call: retrofit2.Call<Post>,
                response: retrofit2.Response<Post>
            ) {
                when (response.code()) {
                    in 200..299 -> {
                        callback.onSuccess()
                    }
                    in 400..499 -> {
                        Log.e("HTTPERROR", "${response.code()}  " + response.message() + response.raw().request.url)
                        callback.onError(response.code(), RuntimeException(response.message()))
                    }
                    in 500..599 -> {
                        Log.e("HTTPERROR", "${response.code()}  " + response.message() + response.raw().request.url)
                        callback.onError(response.code(), RuntimeException(response.message()))
                    }
                }

            }

            override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                callback.onError(0, RuntimeException(t.message))
            }

        })
    }

}