plugins {
    alias(libs.plugins.android.application)
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    /**
     * new piped extractor
     */
    implementation("com.github.TeamNewPipe:NewPipeExtractor:0.24.6")
    /**
     *
     */
// https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    /**
     * android material package
     */

    // https://mvnrepository.com/artifact/com.google.android.material/material
    implementation("com.google.android.material:material:1.12.0")

    /**
     *  glide for image handle
     */
    // https://mvnrepository.com/artifact/com.github.bumptech.glide/glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    /**
     * Exoplayer
     */
    implementation("androidx.media3:media3-exoplayer:1.7.1")
    implementation("androidx.media3:media3-session:1.7.1")
    implementation("androidx.media3:media3-common:1.7.1")
    implementation("androidx.media3:media3-exoplayer-hls:1.7.1")

    /**
     * loading package
     */

    implementation ("com.github.ybq:Android-SpinKit:1.4.0")


}