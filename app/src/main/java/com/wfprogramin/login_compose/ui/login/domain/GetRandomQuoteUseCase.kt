package com.wfprogramin.login_compose.ui.login.domain

import com.wfprogramin.login_compose.ui.login.data.QuoteRepository
import com.wfprogramin.login_compose.ui.login.domain.model.Quote
import javax.inject.Inject

class GetRandomQuoteUseCase @Inject constructor(private val repository: QuoteRepository) {
  suspend  operator fun invoke(): Quote?{
        val quotes = repository.getAllQuotesFromDatabase()
        if(quotes.isNotEmpty()){
            val randomNumber = (quotes.indices).random()
            return quotes[randomNumber]
        }
        return null
    }
}