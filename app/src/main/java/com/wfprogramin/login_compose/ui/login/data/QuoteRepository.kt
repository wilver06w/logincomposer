package com.wfprogramin.login_compose.ui.login.data

import com.wfprogramin.login_compose.ui.login.data.database.dao.QuoteDao
import com.wfprogramin.login_compose.ui.login.data.database.entities.QuoteEntity
import com.wfprogramin.login_compose.ui.login.data.model.QuoteModel
import com.wfprogramin.login_compose.ui.login.data.network.QuoteService
import com.wfprogramin.login_compose.ui.login.domain.model.Quote
import com.wfprogramin.login_compose.ui.login.domain.model.toDomain
import javax.inject.Inject

class QuoteRepository @Inject constructor(private val api: QuoteService, private val quoteDao: QuoteDao){

    suspend fun getAllQuotesFromApi(): List<Quote>{
        val response: List<QuoteModel> = api.getQuotes()
        return response.map {
            it.toDomain()
        }
    }

    suspend fun getAllQuotesFromDatabase(): List<Quote>{
        val response = quoteDao.getAllQuotes()
        return response.map { it.toDomain() }
    }

    suspend fun insertQuotes(quotes:List<QuoteEntity>){
        quoteDao.insertAll(quotes)
    }

    suspend fun clearQuotes(){
        quoteDao.deleteAllQuotes()
    }
}
