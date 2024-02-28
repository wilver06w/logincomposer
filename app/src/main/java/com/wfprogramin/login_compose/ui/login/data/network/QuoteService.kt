package com.wfprogramin.login_compose.ui.login.data.network

import com.wfprogramin.login_compose.ui.login.data.model.QuoteModel
import com.wfprogramin.login_compose.ui.login.data.network.QuoteApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuoteService @Inject constructor(private val api: QuoteApiClient){

    suspend fun getQuotes(): List<QuoteModel>{
        return withContext(Dispatchers.IO){
            val response = api.getAllQuotes()
            response.body()?: emptyList()
        }
    }
}