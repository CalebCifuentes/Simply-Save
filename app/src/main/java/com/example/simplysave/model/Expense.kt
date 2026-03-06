package com.example.simplysave.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "expense", indices = [Index("categoryID")],
    foreignKeys = [
        ForeignKey(
                entity = Category::class,
                parentColumns = ["categoryID"],
                childColumns = ["categoryID"],
                onDelete = ForeignKey.CASCADE
        )
    ])


data class Expense(
    @PrimaryKey(autoGenerate = true) val expenseID: Int = 0,
    @ColumnInfo("amount_spent") val amountSpent: Double,
    @ColumnInfo("categoryID") val categoryID: Int,
    @ColumnInfo("description") val description: String?,
    @ColumnInfo("expense_date") val expenseDate: Long?
)
