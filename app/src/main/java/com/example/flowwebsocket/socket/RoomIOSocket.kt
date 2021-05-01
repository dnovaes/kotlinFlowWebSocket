package com.example.flowwebsocket.socket

import com.example.flowwebsocket.model.RoomEventData
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
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

    @ExperimentalCoroutinesApi
    fun startGame() = callbackFlow<RoomEventData.RoomData>{
        socket.emit(EVENT_STARTGAME)
        socket.on(EVENT_MOB_APPEARS) { args  ->
            (args[0] as?JSONObject)?.apply {
                val roomData = Gson().fromJson(this.toString(), RoomEventData.RoomData::class.java)
                log("Zubat Appeared at: ${roomData.posX}, ${roomData.posY}")
                offer(roomData)
            }
        }
        awaitClose()
    }

    fun move(roomData: RoomEventData.RoomData) {
        val dataJson = Gson().toJson(roomData)
        log("Emiting ${EVENT_NEWPOSITION}) roomdata: $roomData")
        socket.emit(EVENT_NEWPOSITION, dataJson)
    }

    @ExperimentalCoroutinesApi
    fun listenOtherPlayers() = callbackFlow<RoomEventData.RoomData> {
        socket.on(EVENT_NEWPOSITION) { args ->
            (args[0] as? JSONObject)?.apply {
                val roomData = Gson().fromJson(this.toString(), RoomEventData.RoomData::class.java)
                log("onNewPosition event listener) ${roomData.name}, ${roomData.posX}, ${roomData.posY}")
                offer(roomData)
            }
        }
        awaitClose()
    }

    fun closeConnection() {
        log("$userName closing connection")
        socket.emit(EVENT_UNSUBSCRIBE, userName)
        socket.disconnect()
        socket.off(EVENT_NEWPOSITION)
        socket.close()
    }

    companion object {
        const val LOCAL_WEBSOCKET_HOST = "http://192.168.0.13:3000/"
        const val EVENT_STARTGAME = "start_game"
        const val EVENT_MOB_APPEARS = "mob_appears"
        const val EVENT_NEWPOSITION = "new_position"
        const val EVENT_SUBSCRIBE = "subscribe"
        const val EVENT_UNSUBSCRIBE = "unsubscribe"
    }

}

fun log(msg: String) {
    Timber.tag("logd").v(msg)
}
