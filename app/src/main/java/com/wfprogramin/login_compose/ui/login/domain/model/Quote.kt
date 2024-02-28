package com.wfprogramin.login_compose.ui.login.domain.model

import com.wfprogramin.login_compose.ui.login.data.database.entities.QuoteEntity
import com.wfprogramin.login_compose.ui.login.data.model.QuoteModel

data class Quote(val quote:String, val author:String)

fun QuoteModel.toDomain() = Quote(quote, author)

fun QuoteEntity.toDomain() = Quote(quote, author)