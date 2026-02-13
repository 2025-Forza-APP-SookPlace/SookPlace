package com.example.sookplace.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel // import 추가
import javax.inject.Inject // import 추가

// [수정 1] Hilt를 쓰려면 이 어노테이션이 꼭 필요합니다.
@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() { // [수정 2] 생성자에 @Inject constructor() 추가

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}