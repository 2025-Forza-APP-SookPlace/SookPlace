package com.example.sookplace.data.repository

import com.example.sookplace.data.remote.api.UserApi
import javax.inject.Inject
import javax.inject.Singleton

// @Inject constructor가 있어야 Hilt가 이 클래스를 인식하고 에러를 내지 않습니다.
@Singleton
class MyPageRepository @Inject constructor(
    private val userApi: UserApi
) {
    // 현재는 ViewModel이 UserRepository를 쓰고 있어서 내용이 비어있어도 괜찮습니다.
    // 나중에 마이페이지 관련 기능을 분리하고 싶을 때 여기에 함수를 추가하세요.
}