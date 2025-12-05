package com.example.sookplace.di

import com.example.sookplace.data.local.TokenManager
import com.example.sookplace.data.remote.api.AuthApi
import com.example.sookplace.data.remote.auth.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://your.api.base.url" // <-- 실제 주소로 교체

    //LoggingInterceptor 제공
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val log = HttpLoggingInterceptor()
        log.level = HttpLoggingInterceptor.Level.BODY
        return log
    }

    //AuthInterceptor 제공 (토큰 자동 포함)
    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor {
        return AuthInterceptor(tokenManager)
    }

    //OkHttpClient 제공 (Interceptor 포함)
    @Provides
    @Singleton
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor) // <-- 여기서 토큰 자동 추가
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    //Retrofit 제공
    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //AuthApi 제공
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}
