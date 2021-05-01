package com.example.flowwebsocket.features.roomflow

import androidx.lifecycle.ViewModel
import com.example.flowwebsocket.data.source.RoomDataSource
import com.example.flowwebsocket.model.RoomEventData
import com.example.flowwebsocket.socket.RoomDataSocket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.util.UUID

class MainViewModel(
    private var roomDataSource: RoomDataSource
): ViewModel() {

    private val userName: String = UUID.randomUUID().toString()
    private var socket: RoomDataSocket = RoomDataSocket()

    init {
        socket.openConnection(userName)
    }

    @ExperimentalCoroutinesApi
    fun onMovements() = socket.listenOtherPlayers()

    fun mockedRealTimePositions() = flow {
        for (roomData in mockedRoomData()) {
            emit(roomData)
            delay(2000L)
        }
    }

    fun move(posX: Int, posY: Int) {
        socket.move(RoomEventData.RoomData(userName, posX, posY))
    }

    fun startGame() = socket.startGame()

    fun saveRanking(total: Int) {
        roomDataSource.saveRanking(userName, total)
    }

    fun closeConnection() {
        socket.closeConnection()
    }

    private fun mockedRoomData() = listOf(
        RoomEventData.RoomData("Player1", 10, 10),
        RoomEventData.RoomData("Player2", 10, 9),
        RoomEventData.RoomData("Player2", 9, 9),
        RoomEventData.RoomData("Player1", 10, 5),
        RoomEventData.RoomData("Player1", 10, 6),
        RoomEventData.RoomData("Player1", 8, 5),
        RoomEventData.RoomData("Player1", 7, 4),
        RoomEventData.RoomData("Player2", 6, 9)
    )

}
