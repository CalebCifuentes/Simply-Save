package com.example.simplysave.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.simplysave.view.SimplySaveApp


/*
  Should define an abstract class that extends RoomDatabase.
  The  companion object defines a static method getDatabase()
  that returns a instance of the SimplySaveDatabase
  Makes sure that only one instance of the DB is used in app
*/

@Database(entities = [Category::class, Expense::class, UserSettings::class], version = 3)
 abstract class SimplySaveDatabase : RoomDatabase(){

     abstract fun  categoryDao(): CategoryDAO
     abstract fun expenseDao(): ExpenseDAO
     abstract fun userSetDao(): UserSettingsDAO



     companion object{
         // INSTANCE marked with Volatile annotation so that it is up to-date
         @Volatile
         private var INSTANCE: SimplySaveDatabase? = null

         // synchronized block makes sure one thread can access method at a time
         fun getDatabase(context: Context): SimplySaveDatabase{
             return INSTANCE ?: synchronized(this) {
                 val instance = Room.databaseBuilder(
                     context.applicationContext,
                     SimplySaveDatabase::class.java,
                     "simplysave_database"
                 ).fallbackToDestructiveMigration(false)
                     .build()
                 INSTANCE = instance
                 instance
             }
         }
     }
}

