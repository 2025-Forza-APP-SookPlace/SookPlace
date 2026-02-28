package com.example.sookplace.data.remote.api

import com.example.sookplace.data.remote.request.NicknameChangeRequest
import com.example.sookplace.data.remote.request.PasswordChangeRequest
import com.example.sookplace.data.remote.request.UniversityVerifyRequest
import com.example.sookplace.data.remote.response.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface UserApi {
