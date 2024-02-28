package com.wfprogramin.login_compose.ui.login.domain

import com.wfprogramin.login_compose.ui.login.data.QuoteRepository
import com.wfprogramin.login_compose.ui.login.data.database.entities.toDatabase
import com.wfprogramin.login_compose.ui.login.domain.model.Quote
import javax.inject.Inject

class GetQuotesUseCase @Inject constructor(private val repository: QuoteRepository) {

    suspend operator fun invoke(): List<Quote>{
        val quotes = repository.getAllQuotesFromApi()

   return if(quotes.isNotEmpty()){
            repository.clearQuotes()
            repository.insertQuotes(quotes = quotes.map { it.toDatabase() })
            quotes
        }else{
            repository.getAllQuotesFromDatabase()
        }
    }
}