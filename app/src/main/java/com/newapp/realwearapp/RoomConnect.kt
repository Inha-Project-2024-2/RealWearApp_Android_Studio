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
import timber.log.Timber


class RoomConnect : Activity(), RoomListener {
    private var executor: LooperExecutor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // LooperExecutor 초기화
        executor = LooperExecutor()
        executor!!.requestStart()

        // WebSocket을 통한 KurentoRoomAPI 연결 설정
        val wsRoomUri = "wss://mykurentoserver:1234/room"
        kurentoRoomAPI = KurentoRoomAPI(executor, wsRoomUri, this)
        kurentoRoomAPI!!.connectWebSocket()
    }

    // 서버에 연결되었을 때 호출되는 메소드
    override fun onRoomConnected() {
        Timber.tag(TAG).d("WebSocket 연결 완료!")


        // 방에 참여 요청
        kurentoRoomAPI!!.sendJoinRoom("myUserName", "myRoomName", true, 123)
    }

    // 방 참여 응답 처리
    override fun onRoomResponse(response: RoomResponse) {
        Timber.tag(TAG).d("RoomResponse: " + response)

        if (response.id == 123) {
            Timber.tag(TAG).d("방에 성공적으로 연결되었습니다!")

            // 방에 있는 모든 사용자에게 메시지 전송
            kurentoRoomAPI!!.sendMessage("myRoomName", "myUserName", "안녕하세요, 방!", 125)
        } else if (response.id == 125) {
            Timber.tag(TAG).d("서버가 메시지를 수신했습니다!")
        }
    }

    // 방 알림 처리 (메시지 수신)
    override fun onRoomNotification(notification: RoomNotification) {
        if (notification.method == RoomListener.METHOD_SEND_MESSAGE) {
            val username = notification.getParam("user").toString()
            val message = notification.getParam("message").toString()
            Timber.tag(TAG).d(username + "로부터 메시지를 받았습니다: " + message)
        }
    }

    // 오류 처리
    override fun onRoomError(error: RoomError) {
        Timber.tag(TAG).d("RoomError: " + error)

        if (error.code == RoomError.Code.EXISTING_USER_IN_ROOM_ERROR_CODE.value) {
            Timber.tag(TAG).d("이미 같은 이름의 사용자가 방에 있습니다!")
        }
    }

    // 방에서 연결이 끊어졌을 때 호출되는 메소드
    override fun onRoomDisconnected() {
        Timber.tag(TAG).d("연결이 끊겼습니다.")
    }

    override fun onDestroy() {
        // 방에서 나가기 요청을 전송
        kurentoRoomAPI!!.sendLeaveRoom(131)
        super.onDestroy()
    }

    companion object {
        private const val TAG = "MainActivity"
        private var kurentoRoomAPI: KurentoRoomAPI? = null
    }
}
