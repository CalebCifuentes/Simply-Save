package com.example.simplysave.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/*
  Should define an abstract class that extends RoomDatabase.
  The  companion object defines a static method getDatabase()
  that returns a instance of the SimplySaveDatabase
  Makes sure that only one instance of the DB is used in app
*/

@Database(entities = [Category::class, Expense::class], version = 1)
 abstract class SimplySaveDatabase : RoomDatabase(){

     abstract fun  categoryDao(): CategoryDAO
     abstract fun expenseDao(): ExpenseDAO


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
                 ).build()
                 INSTANCE = instance
                 instance
             }
         }
     }
}

