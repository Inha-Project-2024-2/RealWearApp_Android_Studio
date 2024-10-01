package com.newapp.realwearapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import org.webrtc.SurfaceViewRenderer

class VideoActivity : AppCompatActivity() {
    private lateinit var localView: SurfaceViewRenderer
    private lateinit var webRTCManager: WebRTCManager
    private lateinit var signalingClient: SignalingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        // UI 설정
        localView = findViewById(R.id.local_view)
        webRTCManager = WebRTCManager()
        webRTCManager.initialize(this)

        // Signaling 서버 연결
        signalingClient = SignalingClient("ws://your-sfu-server", ::onMessageReceived)
        signalingClient.connect()

        // 비디오 출력
        localView.init(webRTCManager.getEglBaseContext(), null)
        localView.setMirror(true)

        // 로컬 미디어 설정
        val localMediaStream = webRTCManager.createLocalMediaStream(this)
        val videoTrack = webRTCManager.getLocalVideoTrack()
        videoTrack?.addSink(localView)
    }

    private fun onMessageReceived(data: JSONObject) {
        // Offer/Answer 처리
    }

    override fun onDestroy() {
        super.onDestroy()
        localView.release()
        signalingClient.disconnect()
    }
}

