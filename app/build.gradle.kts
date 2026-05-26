plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services") version "4.4.4"
}

android {
    namespace = "com.example.lab6_23520536_21521202"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lab6_23520536_21521202"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Fix Kotlin JVM target
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.3")
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    // Google Sign In
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    // Thư viện CameraX (Dùng để mở và điều khiển Camera)
    val camerax_version = "1.3.1"
    implementation("androidx.camera:camera-core:$camerax_version")
    implementation("androidx.camera:camera-camera2:$camerax_version")
    implementation("androidx.camera:camera-lifecycle:$camerax_version")
    implementation("androidx.camera:camera-view:$camerax_version")

    // Thư viện Google ML Kit (Dùng để nhận diện và đọc mã QR/Barcode)
    implementation("com.google.mlkit:barcode-scanning:17.2.0")
    implementation("com.google.guava:guava:32.1.3-android")
    implementation("com.github.bumptech.glide:glide:4.16.0")
}