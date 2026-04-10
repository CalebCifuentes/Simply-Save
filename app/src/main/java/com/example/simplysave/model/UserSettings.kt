package com.example.simplysave.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserSettings")
data class UserSettings (
    @PrimaryKey val id: Int = 1,
    val monthlyIncome: Double
)