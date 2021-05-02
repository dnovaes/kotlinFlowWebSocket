package com.example.flowwebsocket.model

sealed class RoomEventData {
    data class MobData(val name: String, val posX: Int, val posY: Int, val isNew: Boolean): RoomEventData()
}

