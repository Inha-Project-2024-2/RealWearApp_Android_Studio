package com.newapp.realwearapp


import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SignalingClient(private val signalingUrl: String, private val onMessage: (JSONObject) -> Unit) {
    private lateinit var socket: Socket

    fun connect() {
        socket = IO.socket(signalingUrl)
        socket.connect()

        socket.on("offer") { args ->
            val data = args[0] as JSONObject
            onMessage(data)
        }

        socket.on("answer") { args ->
            val data = args[0] as JSONObject
            onMessage(data)
        }

        socket.on("ice-candidate") { args ->
            val data = args[0] as JSONObject
            onMessage(data)
        }
    }

    fun sendMessage(event: String, message: JSONObject) {
        socket.emit(event, message)
    }

    fun disconnect() {
        socket.disconnect()
    }
}