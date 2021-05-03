package com.example.flowwebsocket.data.source.remote

import com.example.flowwebsocket.model.RoomEventData
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONObject
import timber.log.Timber

class RoomDataSocket {

    private val socket: Socket = IO.socket(LOCAL_WEBSOCKET_HOST)
    private lateinit var userName: String

    fun openConnection(userName: String) {
        try {
            socket.connect()
            this.userName = userName
            socket.emit(EVENT_SUBSCRIBE, userName)
        } catch (e: Exception) {
            e.printStackTrace()
            log("Error during OPEN_CONNECTION ocurred: $e")
        }
    }

    fun startGame() {
        socket.emit(EVENT_GAME_START)
    }

    @ExperimentalCoroutinesApi
    suspend fun startedGame() = callbackFlow<Boolean> {
        socket.on(EVENT_GAME_STARTED) {
            offer(true)
        }
        awaitClose()
    }

    @ExperimentalCoroutinesApi
    suspend fun onMobEvent() = callbackFlow<RoomEventData.MobData>{
        val listener = Emitter.Listener { args ->
            (args[0] as? JSONObject)?.apply {
                val roomData = Gson().fromJson(this.toString(), RoomEventData.MobData::class.java)
                log("Mob event at: ${roomData.posX}, ${roomData.posY}, isNew: ${roomData.isNew}")
                offer(roomData)
            }
        }
        socket.on(EVENT_MOB_EVENT, listener)
        awaitClose { socket.off(EVENT_MOB_EVENT, listener) }
    }

    fun removeMob(posX: Int, posY: Int) {
        val mobData = RoomEventData.MobData("mob", posX, posY, false)
        val dataJson = Gson().toJson(mobData)
        socket.emit(EVENT_MOB_DESTROY, dataJson)
    }

    fun move(roomData: RoomEventData.MobData) {
        val dataJson = Gson().toJson(roomData)
        log("Emiting ${EVENT_NEWPOSITION}) roomdata: $roomData")
        socket.emit(EVENT_NEWPOSITION, dataJson)
    }

    @ExperimentalCoroutinesApi
    fun listenOtherPlayers() = callbackFlow<RoomEventData.MobData> {
        socket.on(EVENT_NEWPOSITION) { args ->
            (args[0] as? JSONObject)?.apply {
                val roomData = Gson().fromJson(this.toString(), RoomEventData.MobData::class.java)
                log("onNewPosition event listener) ${roomData.name}, ${roomData.posX}, ${roomData.posY}")
                offer(roomData)
            }
        }
        awaitClose()
    }

    fun registerGameOverEvent(onEvent: () -> Unit) {
        socket.on(EVENT_GAME_OVER) {
            onEvent.invoke()
        }
    }

    fun closeConnection() {
        socket.emit(EVENT_UNSUBSCRIBE, userName)
        socket.disconnect()
        socket.close()
    }

    companion object {
        const val LOCAL_WEBSOCKET_HOST = "http://192.168.0.13:3000/"
        const val EVENT_GAME_START = "game_start"
        const val EVENT_GAME_STARTED = "game_started"
        const val EVENT_GAME_OVER = "game_over"
        const val EVENT_MOB_EVENT = "mob_event"
        const val EVENT_MOB_DESTROY = "mob_destroy"
        const val EVENT_NEWPOSITION = "new_position"
        const val EVENT_SUBSCRIBE = "subscribe"
        const val EVENT_UNSUBSCRIBE = "unsubscribe"
    }

}

fun log(msg: String) {
    Timber.tag("logd").v(msg)
}
