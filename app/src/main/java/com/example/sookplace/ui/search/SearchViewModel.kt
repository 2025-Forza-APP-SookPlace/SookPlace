package com.example.sookplace.ui.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.local.db.AppDatabase
import com.example.sookplace.data.local.entity.RestaurantEntity
import com.example.sookplace.data.local.entity.UserProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel (application: Application) : AndroidViewModel(application) {
    val context = getApplication<Application>().applicationContext
    val db = AppDatabase.getDatabase(context)


    fun getData() = viewModelScope.launch(Dispatchers.IO) {
        //Log.d("HomeViewModel", db.userProfileDao().getAllData().toString())
        Log.d("HomeViewModel", db.restaurantDao().getAllData().toString())
    }
}