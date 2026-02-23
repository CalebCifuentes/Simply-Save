package com.example.simplysave.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Category::class, Expense::class], version = 1)
abstract class SimplySaveDatabase : RoomDatabase(){
    abstract fun categoryDao(): CategoryDAO
    abstract fun expenseDao(): ExpenseDAO
}

