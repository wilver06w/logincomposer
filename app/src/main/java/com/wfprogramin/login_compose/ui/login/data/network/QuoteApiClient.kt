package com.wfprogramin.login_compose.ui.login.data.network

import com.wfprogramin.login_compose.ui.login.data.model.QuoteModel
import retrofit2.Response
import retrofit2.http.GET

interface QuoteApiClient {
    @GET("/.json")
    suspend fun getAllQuotes(): Response<List<QuoteModel>>
}