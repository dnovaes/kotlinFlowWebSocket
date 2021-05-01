package com.example.flowwebsocket.data.source

interface LocalDataSource {
    fun hasMobIn(posX: Int, posY: Int): Boolean
    fun markMob(posX: Int, posY: Int, valueToBeMarked: Boolean = true)
    fun saveRanking(username: String, total: Int)
}
