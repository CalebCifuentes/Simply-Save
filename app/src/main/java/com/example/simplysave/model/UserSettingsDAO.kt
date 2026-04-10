package com.example.simplysave.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDAO {

    @Upsert
    suspend fun upsertMonthlyIncome(userSettings: UserSettings)

    @Query("Select * FROM UserSettings WHERE id = 1")
    fun fetchMonthlyIncome(): Flow<List<UserSettings?>>


}