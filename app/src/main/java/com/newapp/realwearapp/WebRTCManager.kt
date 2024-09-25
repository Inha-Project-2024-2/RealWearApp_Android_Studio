package com.newapp.realwearapp

import android.content.Context
import org.webrtc.*
import org.webrtc.PeerConnection
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription

class WebRTCManager {
    private lateinit var peerConnectionFactory: PeerConnectionFactory
    private lateinit var rootEglBase: EglBase
    private lateinit var videoTrack: VideoTrack
    private lateinit var audioTrack: AudioTrack
    private lateinit var localMediaStream: MediaStream
    //private var peerConnection: PeerConnection? = null

    fun initialize(context: Context) {
        // EGL 초기화 (비디오 처리용)
        rootEglBase = EglBase.create()

        // PeerConnectionFactory 초기화
        val initializationOptions = PeerConnectionFactory.InitializationOptions.builder(context)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(initializationOptions)

        // PeerConnectionFactory 빌드
        val options = PeerConnectionFactory.Options()
        peerConnectionFactory = PeerConnectionFactory.builder()
            .setOptions(options)
            .createPeerConnectionFactory()
    }

    fun createLocalMediaStream(context: Context): MediaStream {
        // 카메라와 마이크를 사용해 로컬 미디어 스트림 생성
        val videoCapturer = createCameraCapturer(Camera2Enumerator(context))
        val videoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast)
        val surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", rootEglBase.eglBaseContext)
        videoCapturer.initialize(surfaceTextureHelper, context, videoSource.capturerObserver)
        videoCapturer.startCapture(1280, 720, 30)

        videoTrack = peerConnectionFactory.createVideoTrack("VIDEO_TRACK_ID", videoSource)

        val audioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
        audioTrack = peerConnectionFactory.createAudioTrack("AUDIO_TRACK_ID", audioSource)

        localMediaStream = peerConnectionFactory.createLocalMediaStream("LOCAL_STREAM")
        localMediaStream.addTrack(videoTrack)
        localMediaStream.addTrack(audioTrack)

        return localMediaStream
    }

    private fun createCameraCapturer(enumerator: Camera2Enumerator): VideoCapturer {
        val deviceNames = enumerator.deviceNames
        for (deviceName in deviceNames) {
            if (enumerator.isBackFacing(deviceName)) {
                val videoCapturer = enumerator.createCapturer(deviceName, null)
                if (videoCapturer != null) {
                    return videoCapturer
                }
            }
        }
        throw RuntimeException("No front-facing camera found")
    }

    fun getEglBaseContext(): EglBase.Context = rootEglBase.eglBaseContext

    /* fun createPeerConnection(iceServers: List<PeerConnection.IceServer>, onIceCandidate: (IceCandidate) -> Unit) {
        val rtcConfig = PeerConnection.RTCConfiguration(iceServers)
        peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, object : PeerConnection.Observer {
            override fun onIceCandidate(candidate: IceCandidate) {
                onIceCandidate(candidate)
            }

            override fun onAddStream(stream: MediaStream) {
                // 원격 스트림이 추가될 때
            }

            override fun onRemoveStream(stream: MediaStream) {
                // 원격 스트림이 제거될 때
            }

            override fun onSignalingChange(state: PeerConnection.SignalingState) {}
            override fun onIceConnectionChange(state: PeerConnection.IceConnectionState) {}
            override fun onIceGatheringChange(state: PeerConnection.IceGatheringState) {}
            override fun onDataChannel(channel: DataChannel) {}
            override fun onRenegotiationNeeded() {}
        })
    }

    fun setRemoteDescription(description: SessionDescription) {
        peerConnection.setRemoteDescription(object : SdpObserver {
            override fun onSetSuccess() {}
            override fun onSetFailure(error: String) {}
        }, description)
    }

    fun addIceCandidate(candidate: IceCandidate) {
        peerConnection.addIceCandidate(candidate)
    } */


}


