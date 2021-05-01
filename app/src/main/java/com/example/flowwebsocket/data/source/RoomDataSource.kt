package com.example.flowwebsocket.data.source

interface RoomDataSource {
    fun saveRanking(username: String, total: Int)
}
