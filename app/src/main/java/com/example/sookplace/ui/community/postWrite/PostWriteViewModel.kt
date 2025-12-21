package com.example.sookplace.ui.community.postWrite

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.remote.response.PostWriteResponse
import com.example.sookplace.data.repository.CommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _writeState = MutableStateFlow<WriteUiState>(WriteUiState.Idle)
    val writeState: StateFlow<WriteUiState> = _writeState

    //게시물 등록
    fun uploadPost(
        title: String,
        content: String,
        rating: Float,
        restaurantId: Int,
        imageUris: List<Uri>
    ) {
        viewModelScope.launch {
            _writeState.value = WriteUiState.Loading
            try {
                //텍스트 데이터를 RequestBody로 변환 (Multipart 전송 규격)
                val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
                val contentBody = content.toRequestBody("text/plain".toMediaTypeOrNull())
                val ratingBody = rating.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val restaurantIdBody = restaurantId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                //이미지 Uri 리스트를 MultipartBody.Part 리스트로 변환
                val imageParts = imageUris.mapNotNull { uri ->
                    prepareImagePart(uri)
                }

                //서버 전송
                val response = repository.createPost(
                    titleBody, ratingBody, contentBody, restaurantIdBody,
                    if (imageParts.isEmpty()) null else imageParts
                )

                if (response.isSuccessful && response.body() != null) {
                    _writeState.value = WriteUiState.Success(response.body()!!)
                } else {
                    _writeState.value = WriteUiState.Error("게시글 등록에 실패했습니다.")
                }
            } catch (e: Exception) {
                _writeState.value = WriteUiState.Error(e.message ?: "네트워크 오류가 발생했습니다.")
            }
        }

    }

    //Uri를 서버 전송용 MultipartBody.Part로 변환하는 유틸리티
    private fun prepareImagePart(uri: Uri): MultipartBody.Part? {
        val contentResolver = context.contentResolver
        //Uri로부터 입력 스트림 열기
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val byteArray = inputStream.readBytes()
        inputStream.close()

        // RequestBody 생성
        val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())

        // 서버가 기대하는 키값("images")과 파일명으로 파트 생성
        return MultipartBody.Part.createFormData(
            "images",
            "post_image_${System.currentTimeMillis()}.jpg",
            requestBody
        )
    }


}

sealed class WriteUiState {
    object Idle : WriteUiState()
    object Loading : WriteUiState()
    data class Success(val data: PostWriteResponse) : WriteUiState()
    data class Error(val message: String) : WriteUiState()
}