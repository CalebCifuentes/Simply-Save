package com.example.simplysave.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CategoryDAO {

    @Insert
    fun addCategory(vararg category: Category)

    @Delete
    fun deleteCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

/*
may be over specifying
    @Query("UPDATE category set category_name = :name" +
            " WHERE categoryID = :id")
    fun updateCategoryName(name:String, id:Int)


    @Query("UPDATE category set budget_amount = :amount" +
    " WHERE categoryID = :id")
    fun updateCategoryBudgetAmount(amount:Double, id:Int)

 */
}