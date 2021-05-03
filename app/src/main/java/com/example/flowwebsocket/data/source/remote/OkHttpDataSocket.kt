package com.example.flowwebsocket.data.source.remote

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.logging.HttpLoggingInterceptor
import okio.ByteString
import timber.log.Timber
import java.lang.Exception
import java.util.concurrent.TimeUnit

class OkHttpDataSocket {

   private var okHttpClient: OkHttpClient = initializeClient()
   private var request: Request = initializeRequest()

   private fun initializeClient(): OkHttpClient {
      return OkHttpClient.Builder()
         .readTimeout(3, TimeUnit.SECONDS)
         .retryOnConnectionFailure(true)
         .connectTimeout(10, TimeUnit.SECONDS)
         .readTimeout(10, TimeUnit.SECONDS)
         .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
         .build()
   }

   private fun initializeRequest(): Request {
      return Request.Builder()
         .get()
         .url(RoomDataSocket.LOCAL_WEBSOCKET_HOST)
         //.header("Transfer-Encoding", "chunked")
         .header("Connection", "close")
         .build()
   }

   private val webSocketListener = object: WebSocketListener() {
      override fun onOpen(webSocket: WebSocket, response: Response) {
         log("Socket connection: onOpen: $response")
      }

      override fun onMessage(webSocket: WebSocket, text: String) {
         log("Socket connection:onMessage (Text): $text")
      }

      override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
         log("Socket connection:onMessage (ByteString): $bytes")
      }

      override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
         log("Socket connection:onClosing: Code: $code Reason: $reason")
      }

      override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
         log("Socket connection:onClosed: Code: $code Reason: $reason")
      }

      override fun onFailure(webSocket: WebSocket, t:Throwable, response: Response?) {
         log("Socket connection:onFailure: Throwable: ${t.message} Response: $response, websocket: $webSocket")
      }
   }

   fun openConnection() {
      try {
         val webSocket = okHttpClient.newWebSocket(request, webSocketListener)
         webSocket.send("testing 1,2,3")
         okHttpClient.dispatcher.executorService.shutdown()
      } catch (e: Exception) {
         e.printStackTrace()
         print(e.toString())
         Timber.e("Error ocurred: $e")
      }
   }
}