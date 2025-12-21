package com.example.sookplace.ui.community.postWrite

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.remote.response.PostWriteResponse
import com.example.sookplace.data.repository.CommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class PostWriteViewModel @Inject constructor(
    private val repository: CommunityRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    //게시물 작성 상태
    private val _writeState = MutableStateFlow<WriteUiState>(WriteUiState.Idle)
    val writeState: StateFlow<WriteUiState> = _writeState

    private val _displayImages = MutableStateFlow<List<Uri>>(emptyList())
    val displayImages: StateFlow<List<Uri>> = _displayImages

    //수정모드: 삭제할 이미지
    private val _removeImageUrls = mutableListOf<String>() // 삭제될 URL 보관함
    val removeImageUrls: List<String> get() = _removeImageUrls

    //입력상태저장
    val title = MutableStateFlow("")
    val content = MutableStateFlow("")
    val rating = MutableStateFlow(0.0f)
    val restaurantId = MutableStateFlow<Int?>(null)

    //버튼 활성화 여부
    val isFormValid: StateFlow<Boolean> = kotlinx.coroutines.flow.combine(
        title, content, rating, restaurantId
    ) { t, c, r, id ->
        t.isNotBlank() && c.isNotBlank() && r > 0.0f && id != null
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), false)

    //수정모드: 기존 이미지 세팅
    fun setInitialImages(uris: List<Uri>) {
        _displayImages.value = uris.toList()
    }

    //이미지 추가
    fun addImages(newUris: List<Uri>) {
        val currentList = _displayImages.value.toMutableList()
        val remainingSpace = 10 - currentList.size
        currentList.addAll(newUris.take(remainingSpace))
        _displayImages.value = currentList.toList()
    }

    //이미지 삭제
    fun removeImage(uri: Uri) {
        val currentList = _displayImages.value.toMutableList()
        val uriString = uri.toString()
        // 서버 이미지(http)인 경우에만 삭제 리스트에 보관
        if (uriString.startsWith("http")) {
            if (!_removeImageUrls.contains(uriString)) {
                _removeImageUrls.add(uriString)
            }
        }
        currentList.remove(uri)
        _displayImages.value = currentList.toList()
    }

    //서버 전송 공통 로직 (RequestBody 변환 중복 제거)
    private fun createCommonRequestBody(title: String, content: String, rating: Float, placeId: Int) = mapOf(
        "title" to title.toRequestBody("text/plain".toMediaTypeOrNull()),
        "content" to content.toRequestBody("text/plain".toMediaTypeOrNull()),
        "rating" to rating.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
        "placeId" to placeId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    )

    //게시물 등록 및 수정
    fun submitPost(isEditMode: Boolean, postId: String? = null) {
        viewModelScope.launch {
            _writeState.value = WriteUiState.Loading
            try {
                val titleBody = title.value.toRequestBody("text/plain".toMediaTypeOrNull())
                val contentBody = content.value.toRequestBody("text/plain".toMediaTypeOrNull())
                val ratingBody = rating.value.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val restaurantIdBody = restaurantId.value.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                val currentImages = _displayImages.value
                val imageParts = currentImages.filter { !it.toString().startsWith("http") }
                    .mapNotNull { prepareImagePart(it) }

                val response = if (isEditMode && postId != null) {
                    repository.updatePost(postId, titleBody, ratingBody, contentBody, restaurantIdBody, _removeImageUrls, if (imageParts.isEmpty()) null else imageParts)
                } else {
                    repository.createPost(titleBody, ratingBody, contentBody, restaurantIdBody, if (imageParts.isEmpty()) null else imageParts)
                }

                if (response.isSuccessful) _writeState.value = WriteUiState.Success(response.body()!!)
                else _writeState.value = WriteUiState.Error("실패했습니다.")
            } catch (e: Exception) {
                _writeState.value = WriteUiState.Error(e.message ?: "오류 발생")
            }
        }
    }

    //Uri를 서버 전송용 MultipartBody.Part로 변환하는 유틸리티
    private fun prepareImagePart(uri: Uri): MultipartBody.Part? {
        return try {
            val contentResolver = context.contentResolver
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val byteArray = inputStream.readBytes()
                val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("images", "img_${System.currentTimeMillis()}.jpg", requestBody)
            }
        } catch (e: Exception) { null }
    }
}

sealed class WriteUiState {
    object Idle : WriteUiState()
    object Loading : WriteUiState()
    data class Success(val data: PostWriteResponse) : WriteUiState()
    data class Error(val message: String) : WriteUiState()
}