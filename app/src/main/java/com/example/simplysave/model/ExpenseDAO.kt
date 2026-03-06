package com.example.simplysave.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDAO {

    @Insert()
    suspend fun insertExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expense ex join category cat on ex.categoryID = cat.categoryID" +
            " where cat.categoryID = :catID")
     fun getExpenseByCategory(catID: Int) : Flow<List<Expense>>

    @Query("SELECT * FROM expense where expenseID = :id")
    suspend fun getExpenseByID(id: Int): Expense?
}