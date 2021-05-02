package com.example.flowwebsocket.features.roomflow

import androidx.lifecycle.ViewModel
import com.example.flowwebsocket.data.source.LocalDataSource
import com.example.flowwebsocket.model.RoomEventData
import com.example.flowwebsocket.socket.RoomDataSocket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class MainViewModel(
    private var localDataSource: LocalDataSource
): ViewModel() {

    private val userName: String = UUID.randomUUID().toString()
    private var socket: RoomDataSocket = RoomDataSocket()

    init {
        socket.openConnection(userName)
    }

    @ExperimentalCoroutinesApi
    fun onMovements() = socket.listenOtherPlayers()

    fun startGame() = socket.startGame()

    @ExperimentalCoroutinesApi
    suspend fun onStartGame(): Flow<Boolean> = socket.startedGame()

    @ExperimentalCoroutinesApi
    suspend fun onMobChange(): Flow<RoomEventData.MobData> = socket.getMobs()

    fun updateMobCache(posX: Int, posY: Int, valueTobeMarked: Boolean = true) {
        localDataSource.markMob(posX, posY, valueTobeMarked)
    }

    fun removeMob(posX: Int, posY: Int) {
        socket.removeMob(posX, posY)
    }

    fun hasMob(posX: Int, posY: Int) = localDataSource.hasMobIn(posX, posY)

    fun closeConnection() {
        socket.closeConnection()
    }

    fun saveRanking(total: Int) {
        localDataSource.saveRanking(userName, total)
    }

}
