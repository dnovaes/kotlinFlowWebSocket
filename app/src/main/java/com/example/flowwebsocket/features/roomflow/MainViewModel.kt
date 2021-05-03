package com.example.flowwebsocket.features.roomflow

import androidx.lifecycle.ViewModel
import com.example.flowwebsocket.data.source.LocalDataSource
import com.example.flowwebsocket.features.roomflow.model.MobDangerState
import com.example.flowwebsocket.features.roomflow.model.MobDangerState.Companion.THRESHOLD_ATTENTION
import com.example.flowwebsocket.features.roomflow.model.MobDangerState.Companion.THRESHOLD_CAUTION
import com.example.flowwebsocket.features.roomflow.model.MobDangerState.Companion.THRESHOLD_DANGER
import com.example.flowwebsocket.features.roomflow.model.MobDangerState.Companion.THRESHOLD_PEACEFULL
import com.example.flowwebsocket.model.RoomEventData
import com.example.flowwebsocket.data.source.remote.RoomDataSocket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class MainViewModel(
    private var localDataSource: LocalDataSource,
    private var socket: RoomDataSocket
): ViewModel() {

    private val userName: String = UUID.randomUUID().toString()
    private val _dangerStateFlow: MutableStateFlow<MobDangerState> = MutableStateFlow(MobDangerState.Peacefull)
    val dangerStateFlow: StateFlow<MobDangerState> get() = _dangerStateFlow

    private val _onGameOverFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val onGameOverFlow: StateFlow<Boolean> = _onGameOverFlow

    init {
        socket.openConnection(userName)
        socket.registerGameOverEvent {
            _onGameOverFlow.value = true
        }
    }

    fun startGame() {
        socket.startGame()
    }

    suspend fun countdownEmitter() = flow<Int> {
        var result = processingExample()
        while (true) {
            result++
            emit(result)
            kotlinx.coroutines.delay(1000L)
        }
    }

    fun processingExample() = 1

    @ExperimentalCoroutinesApi
    suspend fun onStartGame(): Flow<Boolean> = socket.startedGame()

    @ExperimentalCoroutinesApi
    suspend fun onMobChange(): Flow<RoomEventData.MobData> = socket.onMobEvent()

    fun updateMobCache(posX: Int, posY: Int, valueTobeMarked: Boolean = true) {
        localDataSource.markMob(posX, posY, valueTobeMarked)
        updateDangerState()
    }

    fun removeMob(posX: Int, posY: Int) {
        socket.removeMob(posX, posY)
    }

    fun hasMob(posX: Int, posY: Int) = localDataSource.hasMobIn(posX, posY)

    fun closeConnection() {
        localDataSource.mobClear()
        socket.closeConnection()
    }

    private fun updateDangerState() {
        val currentPercent = localDataSource.mobPercentage()
        println("logd currentPercent: $currentPercent")
        when {
            (currentPercent > THRESHOLD_DANGER) -> _dangerStateFlow.value = MobDangerState.Emergency
            (currentPercent > THRESHOLD_CAUTION) -> _dangerStateFlow.value = MobDangerState.Danger
            (currentPercent > THRESHOLD_ATTENTION) -> _dangerStateFlow.value = MobDangerState.Caution
            (currentPercent > THRESHOLD_PEACEFULL) -> _dangerStateFlow.value = MobDangerState.Attention
            else -> _dangerStateFlow.value = MobDangerState.Peacefull
        }
    }

    fun saveRanking(total: Int) {
        localDataSource.saveRanking(userName, total)
    }

}
