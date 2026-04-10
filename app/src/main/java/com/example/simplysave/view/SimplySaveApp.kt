package com.example.simplysave.view

import androidx.compose.runtime.*
import com.example.simplysave.viewmodel.CategoryAndExpenseViewModel


@Composable
fun SimplySaveApp(viewModel: CategoryAndExpenseViewModel) {
    val categories by viewModel.categories.collectAsState()
    val monthlyIncome by viewModel.monthlyIncome.collectAsState()

    // Show welcome screen only when there are no categories yet
    var hasCompletedWelcome by remember { mutableStateOf(false) }

    if (!hasCompletedWelcome && categories.isEmpty()) {
        WelcomingScreen(
            onSaveIncome = {income ->
                viewModel.saveMonthlyIncome(income)
            },
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
