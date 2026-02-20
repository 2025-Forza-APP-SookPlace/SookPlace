package com.example.sookplace.di

import android.content.Context
import androidx.room.Room
import com.example.sookplace.data.local.TokenManager
import com.example.sookplace.data.local.dao.*
import com.example.sookplace.data.local.db.AppDatabase
import com.example.sookplace.data.remote.api.*
import com.example.sookplace.data.remote.auth.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    // ★★★ [매우 중요] 여기 주소를 지금 실행 중인 ngrok 주소로 꼭 바꿔주세요! ★★★
    // 끝에 '/' 슬래시를 빠뜨리지 마세요.
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // [수정됨] 데이터베이스 충돌 방지 옵션 추가
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "sookplace_db"
        )
            .fallbackToDestructiveMigration() // 데이터 구조 변경 시 자동 초기화
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val log = HttpLoggingInterceptor()
        log.level = HttpLoggingInterceptor.Level.BODY // 통신 내용 로그 확인용
        return log
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenManager: TokenManager,
        userDao: UserProfileDao,
        authApi: javax.inject.Provider<AuthApi>
    ): AuthInterceptor {
        return AuthInterceptor(tokenManager, userDao, authApi)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor) // 토큰 자동 전송
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

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

    // --- API 제공 ---
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideUserProfileApi(retrofit: Retrofit): UserProfileApi = retrofit.create(UserProfileApi::class.java)

    @Provides
    @Singleton
    fun provideRestaurantApi(retrofit: Retrofit): RestaurantApi = retrofit.create(RestaurantApi::class.java)

    @Provides
    @Singleton
    fun provideRouletteSpinApi(retrofit: Retrofit): RouletteSpinApi = retrofit.create(RouletteSpinApi::class.java)

    @Provides
    @Singleton
    fun provideSearchApi(retrofit: Retrofit): SearchApi = retrofit.create(SearchApi::class.java)

    @Provides
    @Singleton
    fun provideCommunityApi(retrofit: Retrofit): CommunityApi = retrofit.create(CommunityApi::class.java)

    // [마이페이지용 API]
    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    // --- DAO 제공 ---
    @Provides
    @Singleton
    fun provideUserProfileDao(database: AppDatabase): UserProfileDao = database.userDao()

    @Provides
    @Singleton
    fun provideRestaurantDao(database: AppDatabase): RestaurantDao = database.restaurantDao()

    @Provides
    @Singleton
    fun providePostDao(database: AppDatabase): PostDao = database.postDao()

    @Provides
    @Singleton
    fun provideFeaturedRestaurantDao(database: AppDatabase): FeaturedRestaurantDao = database.featuredRestaurantDao()

    @Provides
    @Singleton
    fun provideUserPreferenceDao(database: AppDatabase): PlaceDao = database.userPreferenceDao()
}