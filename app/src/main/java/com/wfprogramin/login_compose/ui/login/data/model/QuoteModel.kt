package com.wfprogramin.login_compose.ui.login.data.model

import com.google.gson.annotations.SerializedName

data class QuoteModel (
    @SerializedName("quote") val quote: String,
    @SerializedName("author") val author: String
)