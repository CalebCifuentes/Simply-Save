package com.example.simplysave.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplysave.model.Category
import com.example.simplysave.viewmodel.CategoryAndExpenseViewModel

@Composable
fun CategoryListScreen(viewModel: CategoryAndExpenseViewModel) {
    val categories by viewModel.categories.collectAsState()

    // Track which card is being edited (null = none)
    var editingCategoryId by remember { mutableStateOf<Int?>(null) }
    // Whether to show an empty "add new" card
    var showAddCard by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Header message after first save
        if (categories.isNotEmpty()) {
            Text(
                text = "Sweet! Now you can edit\nor make another category",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        // List of saved categories
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                if (editingCategoryId == category.categoryID) {
                    // Editable card
                    AddCategoryCard(
                        initialName = category.categoryName,
                        initialBudget = category.budgetAmount.toString(),
                        onSave = { name, budget, _->
                            viewModel.updateCategory(
                                category.copy(
                                    categoryName = name,
                                    budgetAmount = budget
                                )
                            )
                            editingCategoryId = null
                        }
                    )
                } else {
                    // Read-only display card with edit/delete actions
                    CategoryDisplayCard(
                        category = category,
                        onEdit = { editingCategoryId = category.categoryID },
                        onDelete = { viewModel.deleteCategory(category) }
                    )
                }
            }

            // New empty card when "+" is tapped
            if (showAddCard) {
                item {
                    AddCategoryCard(
                        onSave = { name, budget, spent ->
                            viewModel.createCategory(
                                Category(categoryName = name, budgetAmount = budget)
                            )
                            showAddCard = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Add new category button
        IconButton(
            onClick = { showAddCard = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Category",
                tint = SSGreen,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun CategoryDisplayCard(
    category: Category,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = category.categoryName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Budget:", fontWeight = FontWeight.SemiBold)
                Text("$${String.format("%.2f", category.budgetAmount)}")
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Amount Spent:", fontWeight = FontWeight.SemiBold)
                // Amount spent is tracked via expenses — show $0.00 as default
                Text("\$0.00")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Delete
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                // Edit
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}