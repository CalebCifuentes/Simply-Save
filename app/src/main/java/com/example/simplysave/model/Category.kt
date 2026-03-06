package com.example.simplysave.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryID: Int = 0,
    @ColumnInfo(name = "category_name") val categoryName: String,
    @ColumnInfo(name = "budget_amount") val budgetAmount: Double,
    @ColumnInfo(name="spent_amount") val spentAmount: Double
)