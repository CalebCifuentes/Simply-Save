package com.example.simplysave.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.simplysave.viewmodel.CategoryAndExpenseViewModel


@Composable
fun SimplySaveApp(viewModel: CategoryAndExpenseViewModel) {
    val categories by viewModel.categories.collectAsState()
    val monthlyIncome by viewModel.monthlyIncome.collectAsState()

    // Show welcome screen only when there are no categories yet
    var hasCompletedWelcome by remember { mutableStateOf(false) }
    //Show screen when no monthly income has been set
    val isReady by viewModel.isReady.collectAsState()

    if(!isReady) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SSGreen)
        }
    }else{
            android.util.Log.d(
                "SimplySaveApp",
                "isReady=$isReady, monthlyIncome=$monthlyIncome, categories=${categories.size}"
            )
    }
     if(monthlyIncome == null){
        firstTimeEnteringIncomeScreen (
            onSaveIncome = {income -> viewModel.saveMonthlyIncome(income)}
        )
    }
    else if (categories.isEmpty()) {
        WelcomingScreen(
            onGetStarted = { name, budget, spent ->
                viewModel.createCategoryAndExpense(

                        categoryName = name,
                        budgetAmount = budget,
                        spentAmount = spent

                    )
            }
        )
    } else {
        CategoryListScreen(viewModel = viewModel)
    }
}
