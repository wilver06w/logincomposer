package com.wfprogramin.login_compose.ui.login.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wfprogramin.login_compose.ui.login.domain.GetQuotesUseCase
import com.wfprogramin.login_compose.ui.login.domain.GetRandomQuoteUseCase
import com.wfprogramin.login_compose.ui.login.domain.model.Quote


import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val getQuotesUseCase : GetQuotesUseCase,
    private val getRandomQuoteUseCase : GetRandomQuoteUseCase,
) : ViewModel() {


    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email
    private val _passWord = MutableLiveData<String>()
    val password : LiveData<String> = _passWord
    private val _loginEnabled = MutableLiveData<Boolean>()
    val loginEnabled : LiveData<Boolean> = _loginEnabled

    val isLoading = MutableLiveData<Boolean>()

    val quoteModel = MutableLiveData<Quote>()
    fun onLoginChanged(email:String, password:String){
        _email.value = email
        _passWord.value = password
        _loginEnabled.value = isValidEmail(email) && isValidPassword(password)
    }
    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPassword(password: String): Boolean = password.length > 6

    suspend fun onLoginSelected(){
        isLoading.postValue(true)
        delay(4000)
        isLoading.postValue(false)
    }


    fun onCreate(){
        viewModelScope.launch{
            isLoading.postValue(true)
            val result = getQuotesUseCase()

            if(result.isNotEmpty()){
                quoteModel.postValue(result[0])
                isLoading.postValue(false)
            }
        }
    }

    fun randomQuote(){
        viewModelScope.launch {


        isLoading.postValue(true)
        val quote = getRandomQuoteUseCase()

        if(quote!=null){
            quoteModel.postValue(quote ?: quote)
        }
        isLoading.postValue(false)
    }
    }
}