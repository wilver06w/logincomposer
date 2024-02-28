package com.wfprogramin.login_compose.ui.login.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wfprogramin.login_compose.ui.login.data.database.dao.QuoteDao
import com.wfprogramin.login_compose.ui.login.data.database.entities.QuoteEntity

@Database(entities = [QuoteEntity::class], version = 1)
abstract class QuoteDatabase : RoomDatabase(){

    abstract fun getQuoteDao(): QuoteDao
}