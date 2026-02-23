package com.example.simplysave.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface ExpenseDAO {

    @Insert
    fun insertExpense(expense: Expense)

    @Update
    fun updateExpense(expense: Expense)

    @Delete
    fun deleteExpense(expense: Expense)
}