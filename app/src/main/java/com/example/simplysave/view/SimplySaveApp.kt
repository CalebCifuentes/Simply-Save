package com.example.simplysave.view

import androidx.compose.runtime.*
import com.example.simplysave.viewmodel.CategoryAndExpenseViewModel


@Composable
fun SimplySaveApp(viewModel: CategoryAndExpenseViewModel) {
    val categories by viewModel.categories.collectAsState()

    // Show welcome screen only when there are no categories yet
    var hasCompletedWelcome by remember { mutableStateOf(false) }

    if (!hasCompletedWelcome && categories.isEmpty()) {
        WelcomingScreen(
            onGetStarted = { name, budget ->
                viewModel.createCategory(
                    com.example.simplysave.model.Category(
                        categoryName = name,
                        budgetAmount = budget
                    )
                )

            }
        )
    } else {
        CategoryListScreen(viewModel = viewModel)
    }
}
