package ru.netology.nmedia.model.dto

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val loading: Boolean = false,
    val error: Boolean = false,
    val empty: Boolean = false,
    val refreshing: Boolean = false,
    val httpError400: Boolean = false,
    val httpError500: Boolean = false
)