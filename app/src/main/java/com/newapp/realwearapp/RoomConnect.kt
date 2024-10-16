package com.newapp.realwearapp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import fi.vtt.nubomedia.kurentoroomclientandroid.KurentoRoomAPI
import fi.vtt.nubomedia.kurentoroomclientandroid.RoomError
import fi.vtt.nubomedia.kurentoroomclientandroid.RoomListener
import fi.vtt.nubomedia.kurentoroomclientandroid.RoomNotification
import fi.vtt.nubomedia.kurentoroomclientandroid.RoomResponse
import fi.vtt.nubomedia.utilitiesandroid.LooperExecutor
import org.json.JSONObject
import timber.log.Timber


class RoomConnect : Activity(), RoomListener {
    public var executor: LooperExecutor? = null
    public var kurentoRoomAPI: KurentoRoomAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // LooperExecutor 초기화
        executor = LooperExecutor()
        executor!!.requestStart()

        // WebSocket을 통한 KurentoRoomAPI 연결 설정
        val wsRoomUri = "wss://localhost:8443/groupcall"
        kurentoRoomAPI = KurentoRoomAPI(executor, wsRoomUri, this)
        kurentoRoomAPI!!.connectWebSocket()
    }

    // 서버에 연결되었을 때 호출되는 메소드
    override fun onRoomConnected() {
        Timber.tag(TAG).d("WebSocket 연결 완료!")

        // 방에 참여 요청
        joinRoom("myUserName", "myRoomName")
    }

    private fun joinRoom(userName: String, roomName: String) {
        // 서버의 "joinRoom" 요청에 맞게 JSON 형식으로 메시지를 생성
        val joinRoomMessage = JSONObject().apply {
            put("id", "joinRoom")
            put("name", userName)
            put("room", roomName)
        }
        sendMessageToServer(joinRoomMessage)
    }

    private fun sendMessageToServer(message: JSONObject) {
        kurentoRoomAPI?.sendCustomRequest(arrayOf("id", "name", "room"), arrayOf(message.getString("id"), message.getString("name"), message.getString("room")), 123)
    }

    // 방 참여 응답 처리
    override fun onRoomResponse(response: RoomResponse) {
        Timber.tag(TAG).d("RoomResponse: $response")

        if (response.id == 123) {
            Timber.tag(TAG).d("방에 성공적으로 연결되었습니다!")
        }
    }

    // 방 알림 처리 (메시지 수신)
    override fun onRoomNotification(notification: RoomNotification) {
        if (notification.method == RoomListener.METHOD_SEND_MESSAGE) {package com.newapp.realwearapp

            import android.app.Activity
                    import android.os.Bundle
                    import android.util.Log
                    import fi.vtt.nubomedia.kurentoroomclientandroid.KurentoRoomAPI
                    import fi.vtt.nubomedia.kurentoroomclientandroid.RoomError
                    import fi.vtt.nubomedia.kurentoroomclientandroid.RoomListener
                    import fi.vtt.nubomedia.kurentoroomclientandroid.RoomNotification
                    import fi.vtt.nubomedia.kurentoroomclientandroid.RoomResponse
                    import fi.vtt.nubomedia.utilitiesandroid.LooperExecutor
                    import org.json.JSONObject
                    import timber.log.Timber


            class RoomConnect : Activity(), RoomListener {
                public var executor: LooperExecutor? = null
                public var kurentoRoomAPI: KurentoRoomAPI? = null

                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)

                    // LooperExecutor 초기화
                    executor = LooperExecutor()
                    executor!!.requestStart()

                    // WebSocket을 통한 KurentoRoomAPI 연결 설정
                    val wsRoomUri = "wss://localhost:8443/groupcall"
                    kurentoRoomAPI = KurentoRoomAPI(executor, wsRoomUri, this)
                    kurentoRoomAPI!!.connectWebSocket()
                }

                // 서버에 연결되었을 때 호출되는 메소드
                override fun onRoomConnected() {
                    Timber.tag(TAG).d("WebSocket 연결 완료!")

                    // 방에 참여 요청
                    joinRoom("myUserName", "myRoomName")
                }

                private fun joinRoom(userName: String, roomName: String) {
                    // 서버의 "joinRoom" 요청에 맞게 JSON 형식으로 메시지를 생성
                    val joinRoomMessage = JSONObject().apply {
                        put("id", "joinRoom")
                        put("name", userName)
                        put("room", roomName)
                    }
                    sendMessageToServer(joinRoomMessage)
                }

                private fun sendMessageToServer(message: JSONObject) {
                    kurentoRoomAPI?.sendCustomRequest(arrayOf("id", "name", "room"), arrayOf(message.getString("id"), message.getString("name"), message.getString("room")), 123)
                }

                // 방 참여 응답 처리
                override fun onRoomResponse(response: RoomResponse) {
                    Timber.tag(TAG).d("RoomResponse: $response")

                    if (response.id == 123) {
                        Timber.tag(TAG).d("방에 성공적으로 연결되었습니다!")
                    }
                }

                // 방 알림 처리 (메시지 수신)
                override fun onRoomNotification(notification: RoomNotification) {
                    if (notification.method == RoomListener.METHOD_SEND_MESSAGE) {
                        val username = notification.getParam("user").toString()
                        val message = notification.getParam("message").toString()
                        Timber.tag(TAG).d("$username 로부터 메시지를 받았습니다: $message")
                    }
                }

                // ICE 후보자 추가 메시지 처리
                public fun sendIceCandidate(candidate: JSONObject, userName: String) {
                    val iceCandidateMessage = JSONObject().apply {
                        put("id", "onIceCandidate")
                        put("name", userName)
                        put("candidate", candidate)
                    }
                    sendMessageToServer(iceCandidateMessage)
                }

                // 오류 처리
                override fun onRoomError(error: RoomError) {
                    Timber.tag(TAG).d("RoomError: $error")

                    if (error.code == RoomError.Code.EXISTING_USER_IN_ROOM_ERROR_CODE.value) {
                        Timber.tag(TAG).d("이미 같은 이름의 사용자가 방에 있습니다!")
                    }
                }

                // WebSocket 연결이 끊겼을 때 호출되는 메소드
                override fun onRoomDisconnected() {
                    Timber.tag(TAG).d("WebSocket 연결이 끊어졌습니다.")
                }

                companion object {
                    public const val TAG = "RoomConnect"
                }
            }
            val username = notification.getParam("user").toString()
            val message = notification.getParam("message").toString()
            Timber.tag(TAG).d("$username 로부터 메시지를 받았습니다: $message")
        }
    }

    // ICE 후보자 추가 메시지 처리
    public fun sendIceCandidate(candidate: JSONObject, userName: String) {
        val iceCandidateMessage = JSONObject().apply {
            put("id", "onIceCandidate")
            put("name", userName)
            put("candidate", candidate)
        }
        sendMessageToServer(iceCandidateMessage)
    }

    // 오류 처리
    override fun onRoomError(error: RoomError) {
        Timber.tag(TAG).d("RoomError: $error")

        if (error.code == RoomError.Code.EXISTING_USER_IN_ROOM_ERROR_CODE.value) {
            Timber.tag(TAG).d("이미 같은 이름의 사용자가 방에 있습니다!")
        }
    }

    // WebSocket 연결이 끊겼을 때 호출되는 메소드
    override fun onRoomDisconnected() {
        Timber.tag(TAG).d("WebSocket 연결이 끊어졌습니다.")
    }

    companion object {
        public const val TAG = "RoomConnect"
    }
}