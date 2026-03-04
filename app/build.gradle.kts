plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
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

    // [수정] 데이터바인딩과 뷰바인딩을 안정적으로 활성화합니다.
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    // [추가] 데이터바인딩 사용 시 발생할 수 있는 컴파일 에러를 상세히 리포팅하도록 설정합니다.
    dataBinding {
        enable = true
    }
}

// [추가] Hilt와 데이터바인딩이 함께 쓰일 때 에러 타입을 교정해주는 설정입니다.
kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    testImplementation(libs.junit)

    kapt("com.github.bumptech.glide:compiler:4.16.0")

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Room
    implementation("androidx.room:room-runtime:2.8.4")
    ksp("androidx.room:room-compiler:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")

    // Paging
    implementation("androidx.paging:paging-runtime-ktx:3.3.6")

    // Retrofit & Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-compiler:2.52")

    // Coil (이미지 로딩 라이브러리 추가)
    implementation("io.coil-kt:coil:2.6.0")

    // OkHttp Logging
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")

    // 네이버 지도 SDK
    implementation("com.naver.maps:map-sdk:3.23.1")
}