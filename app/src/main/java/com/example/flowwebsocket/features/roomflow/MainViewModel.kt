package com.example.flowwebsocket.features.roomflow

import androidx.lifecycle.ViewModel
import com.example.flowwebsocket.data.source.LocalDataSource
import com.example.flowwebsocket.model.RoomEventData
import com.example.flowwebsocket.socket.RoomDataSocket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class MainViewModel(
    private var localDataSource: LocalDataSource,
    private var socket: RoomDataSocket
): ViewModel() {

    private val userName: String = UUID.randomUUID().toString()

    init {
        socket.openConnection(userName)
    }

    @ExperimentalCoroutinesApi
    fun onMovements() = socket.listenOtherPlayers()

    fun startGame() = socket.startGame()

    @ExperimentalCoroutinesApi
    suspend fun onStartGame(): Flow<Boolean> = socket.startedGame()

    @ExperimentalCoroutinesApi
    suspend fun onGameOver(): Flow<Boolean> = socket.onGameOver()

/*
    suspend fun onGameOver2(): Flow<Boolean> = flow {
        socket.onGameOver2 {
            viewModelScope.launch {
                gameOverData.postValue(true)
            }
        }
    }
*/

    @ExperimentalCoroutinesApi
    suspend fun onMobChange(): Flow<RoomEventData.MobData> = socket.onMobEvent()

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
