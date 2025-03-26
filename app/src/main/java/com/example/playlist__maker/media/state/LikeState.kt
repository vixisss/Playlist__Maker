package com.example.playlist__maker.media.state

sealed interface LikeState {
    object Liked : LikeState
    object Unliked : LikeState
}