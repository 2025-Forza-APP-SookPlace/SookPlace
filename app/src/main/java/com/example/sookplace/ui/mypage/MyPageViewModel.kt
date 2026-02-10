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
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // UI 상태 관리 (로딩, 에러, 데이터)
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

    init {
        fetchAllMyPageData()
    }

    fun fetchAllMyPageData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 병렬 호출 (동시에 실행해서 속도 최적화)
                val meDeferred = async { userRepository.getUserMe() }
                val questsDeferred = async { userRepository.getUserQuests() }
                val statsDeferred = async { userRepository.getUserStats() }
                val placesDeferred = async { userRepository.getMyPlaces(1, 4) } // 미리보기 4개
                val postsDeferred = async { userRepository.getMyPosts(0, 3) } // 미리보기 3개

                _userMe.value = meDeferred.await()
                _userQuests.value = questsDeferred.await()
                _userStats.value = statsDeferred.await()
                _myPlaces.value = placesDeferred.await().items
                _myPosts.value = postsDeferred.await().content

            } catch (e: Exception) {
                e.printStackTrace()
                // 에러 처리 로직 추가 가능 (Toast 메시지 등)
            } finally {
                _isLoading.value = false
            }
        }
    }
}