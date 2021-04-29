package com.example.flowwebsocket.model

sealed class RoomEventData {
    data class RoomData(val name: String, val posX: Int, val posY: Int): RoomEventData()
}

