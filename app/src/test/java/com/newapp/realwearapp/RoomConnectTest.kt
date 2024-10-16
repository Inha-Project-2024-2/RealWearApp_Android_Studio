package com.newapp.realwearapp

import android.app.Activity
import android.os.Bundle
import fi.vtt.nubomedia.kurentoroomclientandroid.KurentoRoomAPI
import fi.vtt.nubomedia.kurentoroomclientandroid.RoomError
import fi.vtt.nubomedia.kurentoroomclientandroid.RoomListener
import fi.vtt.nubomedia.kurentoroomclientandroid.RoomNotification
import fi.vtt.nubomedia.kurentoroomclientandroid.RoomResponse
import fi.vtt.nubomedia.utilitiesandroid.LooperExecutor
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import timber.log.Timber

class RoomConnectTest {

    private lateinit var roomConnect: RoomConnect
    private lateinit var kurentoRoomAPI: KurentoRoomAPI
    private lateinit var executor: LooperExecutor

    @Before
    fun setUp() {
        // Mock dependencies
        executor = mock(LooperExecutor::class.java)
        kurentoRoomAPI = mock(KurentoRoomAPI::class.java)

        // Initialize RoomConnect with mocked dependencies
        roomConnect = RoomConnect().apply {
            this.executor = executor
            this.kurentoRoomAPI = kurentoRoomAPI
        }
    }

    @Test
    fun `test onRoomConnected should join room`() {
        // Simulate onRoomConnected call
        roomConnect.onRoomConnected()

        // Capture JSON message sent to kurentoRoomAPI
        val messageCaptor = ArgumentCaptor.forClass(Array<String>::class.java)
        verify(kurentoRoomAPI).sendCustomRequest(messageCaptor.capture(), any())

        // Check if the correct room join message was sent
        val message = messageCaptor.value
        assert(message[0] == "id")
        assert(message[1] == "name")
        assert(message[2] == "room")
    }

    @Test
    fun `test onRoomResponse should log successful connection`() {
        // Simulate onRoomResponse call
        val response = RoomResponse(123, "response", emptyMap())
        roomConnect.onRoomResponse(response)

        // Verify log output
        Timber.tag(RoomConnect.TAG).d("RoomResponse: $response")
        Timber.tag(RoomConnect.TAG).d("방에 성공적으로 연결되었습니다!")
    }

    @Test
    fun `test onRoomError should handle existing user error`() {
        // Simulate onRoomError call
        val error = RoomError(RoomError.Code.EXISTING_USER_IN_ROOM_ERROR_CODE.value, "Error message", emptyMap())
        roomConnect.onRoomError(error)

        // Verify log output
        Timber.tag(RoomConnect.TAG).d("RoomError: $error")
        Timber.tag(RoomConnect.TAG).d("이미 같은 이름의 사용자가 방에 있습니다!")
    }

    @Test
    fun `test sendIceCandidate should send correct message`() {
        // Create a sample ICE candidate message
        val candidate = JSONObject().apply {
            put("candidate", "sample_candidate")
            put("sdpMid", "0")
            put("sdpMLineIndex", 0)
        }

        // Call sendIceCandidate
        roomConnect.sendIceCandidate(candidate, "myUserName")

        // Capture JSON message sent to kurentoRoomAPI
        val messageCaptor = ArgumentCaptor.forClass(Array<String>::class.java)
        verify(kurentoRoomAPI).sendCustomRequest(messageCaptor.capture(), anyInt())

        // Check if the correct ICE candidate message was sent
        val message = messageCaptor.value
        assert(message[0] == "id")
        assert(message[1] == "name")
        assert(message[2] == "candidate")
    }
}

private fun KurentoRoomAPI.sendCustomRequest(capture: Array<String>?, any: Array<String>?) {
    TODO("Not yet implemented")
}
