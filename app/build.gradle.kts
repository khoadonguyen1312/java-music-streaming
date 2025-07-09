plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services") version "4.4.0"
}

android {
    namespace = "com.khoadonguyen.java_music_streaming"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.khoadonguyen.java_music_streaming"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.googleid)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // NewPipe extractor
    implementation("com.github.TeamNewPipe:NewPipeExtractor:0.24.6")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Material
    implementation("com.google.android.material:material:1.12.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.7.1")
    implementation("androidx.media3:media3-session:1.7.1")
    implementation("androidx.media3:media3-common:1.7.1")
    implementation("androidx.media3:media3-exoplayer-hls:1.7.1")
    implementation("androidx.media3:media3-ui:1.7.1")


    // SpinKit
    implementation("com.github.ybq:Android-SpinKit:1.4.0")

    // Pretty Duration Formatter
    implementation("org.apache.commons:commons-lang3:3.12.0")

    // Firebase
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-analytics:21.6.1")
    implementation("com.google.android.gms:play-services-auth:21.0.0")
// Firebase Realtime Database
    implementation("com.google.firebase:firebase-database:20.3.0")

// Firebase Cloud Storage
    implementation("com.google.firebase:firebase-storage:20.3.0")

// skeleton animation
// https://mvnrepository.com/artifact/com.facebook.shimmer/shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")
// amdroid palette
    // https://mvnrepository.com/artifact/androidx.palette/palette
    implementation("androidx.palette:palette:1.0.0-alpha1")

}
apply(plugin = "com.google.gms.google-services")
