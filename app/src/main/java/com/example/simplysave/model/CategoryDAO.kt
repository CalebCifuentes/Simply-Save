package com.example.simplysave.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO {


    @Insert
    suspend fun addCategory(category: Category):Long

    @Delete
    suspend fun deleteCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Query("SELECT * FROM category")
     fun getAllCategories() : Flow<List<Category>>

    @Query("SELECT * FROM category where categoryID = :id")
    suspend fun getCategoryByID(id: Int): Category?

}