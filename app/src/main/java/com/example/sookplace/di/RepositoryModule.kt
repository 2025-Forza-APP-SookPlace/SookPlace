package com.example.sookplace.di

import com.example.sookplace.data.repository.UserRepository
// [중요] 아까 보여주신 UserApi 파일의 정확한 위치를 import 합니다.
import com.example.sookplace.data.remote.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    // 1. Hilt가 UserApi를 찾아서 이 함수의 userApi 파라미터에 넣어줍니다.
    fun provideUserRepository(userApi: UserApi): UserRepository {

        // 2. 여기서 UserRepository 생성자에 userApi를 "전달"해야 에러가 사라집니다.
        // 기존 오류: return UserRepository()  <-- 괄호 안이 비어서 에러 발생
        return UserRepository(userApi)
    }
}