import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.newapp.realwearapp"
    compileSdk = 34

    val properties = Properties()
    properties.load(FileInputStream(rootProject.file("local.properties")))


    defaultConfig {
        applicationId = "com.newapp.realwearapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //buildConfigField("String", "SIGNALING_SERVER_IP_ADDRESS", properties.getProperty("signalingServerIp"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.kurento.room.client.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Web RTC
    implementation ("io.getstream:stream-webrtc-android:1.1.3")
    implementation ("io.getstream:stream-webrtc-android-ui:1.1.3")

    // Fragment
    implementation ("androidx.fragment:fragment-ktx:1.8.1")

    // Lottie
    implementation ("com.airbnb.android:lottie:6.4.1")

    // Network
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")

    // Timber
    implementation ("com.jakewharton.timber:timber:5.0.1")

    // camera
    implementation ("androidx.camera:camera-camera2:1.3.4")
    implementation ("androidx.camera:camera-core:1.3.4")
    implementation ("androidx.camera:camera-lifecycle:1.3.4")
    implementation ("androidx.camera:camera-view:1.3.4")

    // socket
    // Socket.IO 클라이언트
    implementation ("io.socket:socket.io-client:2.0.0")
    // JSON 라이브러리 (필요시 추가)
    implementation ("org.json:json:20210307")

}