package com.example.simplysave.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CategoryDAO {

    @Insert
    suspend fun addCategory(vararg category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Query("SELECT * FROM category")
    suspend fun getAllCategories() : List<Category>

    @Query("SELECT * FROM category where categoryID = :id")
    suspend fun getCategoryByID(id: Int): Category?

}