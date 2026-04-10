package com.example.simplysave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.simplysave.model.CategoryAndExpenseRepository
import com.example.simplysave.model.CategoryDAO
import com.example.simplysave.model.SimplySaveDatabase
import com.example.simplysave.ui.theme.SimplySaveTheme
import com.example.simplysave.view.SimplySaveApp
import com.example.simplysave.view.WelcomingScreen
import com.example.simplysave.viewmodel.CategoryAndExpenseViewModel
import com.example.simplysave.viewmodel.CategoryAndExpenseViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = SimplySaveDatabase.getDatabase(applicationContext)
        val repository = CategoryAndExpenseRepository(db.categoryDao(),db.expenseDao())
        val viewModel: CategoryAndExpenseViewModel by lazy{
            ViewModelProvider(
                this,

                CategoryAndExpenseViewModelFactory(repository)
            ) [CategoryAndExpenseViewModel::class.java]
        }
        setContent {
            SimplySaveTheme {
                //Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                  //  Greeting(
                        //name = "Android",
                       // modifier = Modifier.padding(innerPadding)
                    //)
                SimplySaveApp(viewModel = viewModel)

                }
            }
        }
    }
