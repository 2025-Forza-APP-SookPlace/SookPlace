package com.example.sookplace.ui.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.remote.response.*
import com.example.sookplace.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // UI 상태
    private val _userMe = MutableStateFlow<UserMeResponse?>(null)
    val userMe: StateFlow<UserMeResponse?> = _userMe.asStateFlow()

    private val _userQuests = MutableStateFlow<UserQuestResponse?>(null)
    val userQuests: StateFlow<UserQuestResponse?> = _userQuests.asStateFlow()

    private val _userStats = MutableStateFlow<UserStatsResponse?>(null)
    val userStats: StateFlow<UserStatsResponse?> = _userStats.asStateFlow()

    private val _myPlaces = MutableStateFlow<List<MyPlaceItem>>(emptyList())
    val myPlaces: StateFlow<List<MyPlaceItem>> = _myPlaces.asStateFlow()

    private val _myPosts = MutableStateFlow<List<MyPostItem>>(emptyList())
    val myPosts: StateFlow<List<MyPostItem>> = _myPosts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // [추가] 로그인 필요 여부 상태 (Guest Mode)
    private val _isGuestMode = MutableStateFlow(false)
    val isGuestMode: StateFlow<Boolean> = _isGuestMode.asStateFlow()

    // 에러 메시지
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadMyPage()
    }

    fun loadMyPage() {
        fetchAllMyPageData()
    }

    // 로그인 필요한 기능을 수행하려 할 때 호출할 함수
    fun checkLoginAndAction(action: () -> Unit) {
        if (_isGuestMode.value) {
            _errorMessage.value = "로그인이 필요한 서비스입니다."
        } else {
            action()
        }
    }

    private fun fetchAllMyPageData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isGuestMode.value = false // 일단 초기화

            try {
                supervisorScope {
                    // 병렬 호출 시작
                    val meDeferred = async { userRepository.getUserMe() }
                    val questsDeferred = async { userRepository.getUserQuests() }
                    val statsDeferred = async { userRepository.getUserStats() }
                    val placesDeferred = async { userRepository.getMyPlaces(1, 4) }
                    val postsDeferred = async { userRepository.getMyPosts(0, 3) }

                    // 결과 대기 및 저장
                    _userMe.value = meDeferred.await()
                    _userQuests.value = questsDeferred.await()
                    _userStats.value = statsDeferred.await()
                    _myPlaces.value = placesDeferred.await().items
                    _myPosts.value = postsDeferred.await().content
                }

            } catch (e: HttpException) {
                e.printStackTrace()
                // 401/403 에러 -> 비로그인(게스트) 상태로 전환
                if (e.code() == 403 || e.code() == 401) {
                    _isGuestMode.value = true

                    // 게스트용 더미 데이터 세팅 (빈 화면 대신 보여줄 기본값)
                    setGuestData()
                } else {
                    _errorMessage.value = "서버 에러: ${e.code()}"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "데이터를 불러오는데 실패했습니다."
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 비로그인 상태일 때 보여줄 기본 데이터 설정
    private fun setGuestData() {
        _userMe.value = UserMeResponse(
            id = "",
            userId = "",
            nickname = "로그인이 필요합니다",
            email = "숙명인 인증 후 이용해주세요",
            sookVerified = false,
            level = 0,
            levelTitleEn = "",
            nextLevel = null,
            nextLevelTitleEn = null,
            progressToNextPercent = 0,
            avatarId = "",
            avatarUrl = "",
            createdAt = "",
            updatedAt = ""
        )
        // 퀘스트, 통계 등은 null 혹은 0으로 둬서 화면에서 "0"이나 "데이터 없음"으로 뜨게 함
        _userStats.value = UserStatsResponse(
            postCount = 0,
            savedPlaceCount = 0,
            receivedLikeCount = 0
        )
        _userQuests.value = null
        _myPlaces.value = emptyList()
        _myPosts.value = emptyList()
    }
}