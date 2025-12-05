plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.sookplace"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.sookplace"
        minSdk = 24
        targetSdk = 36
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
    kotlinOptions {
        jvmTarget = "11"
    }
    dataBinding {
        enable = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.room:room-runtime:2.8.4")
    annotationProcessor("androidx.room:room-compiler:2.8.4")

    // To use Kotlin annotation processing tool (kapt)
    ksp("androidx.room:room-compiler:2.8.4")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // ViewModelScope
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.1")

    implementation("androidx.paging:paging-runtime-ktx:3.3.6")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.google.dagger:hilt-android:2.52")
    ksp("com.google.dagger:hilt-compiler:2.52")

    implementation("io.coil-kt:coil:2.6.0")

    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")
}