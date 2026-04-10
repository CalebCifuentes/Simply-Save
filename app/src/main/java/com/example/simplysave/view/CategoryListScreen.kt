package com.example.simplysave.view

import android.R.attr.text
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
import kotlin.concurrent.timer
import com.example.simplysave.viewmodel.CategoryAndExpenseViewModel

@Composable
fun CategoryListScreen(viewModel: CategoryAndExpenseViewModel) {
    val categories by viewModel.categories.collectAsState()

    // Track which card is being edited (null = none)
    var editingCategoryId by remember { mutableStateOf<Int?>(null) }
    // Whether to show an empty "add new" card
    var showAddCard by remember { mutableStateOf(false) }
    // For header that will appear and then go away
    var isVisible by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


     if(isVisible){
         Text(text = "Sweet! Now you can edit\n or make another category",
             fontSize = 18.sp,
             textAlign = TextAlign.Center,
             modifier = Modifier
                 .fillMaxWidth()
                 .padding(bottom = 16.dp)
         )
         timer(name = "background-timer", initialDelay = 3000, period = 3000) {
            isVisible = false;
         }
     }

        // List of saved categories
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                if (editingCategoryId == category.categoryID) {
                    // Editable card
                    CategoryCard(
                        initialName = category.categoryName,
                        initialBudget = category.budgetAmount.toString(),
                        initialAmountSpent = category.spentAmount.toString(),
                        onSave = { name, budget,spent ->
                            viewModel.updateCategory(
                                category.copy(
                                    categoryName = name,
                                    budgetAmount = budget,
                                    spentAmount = spent
                                )
                            )
                                // exits editing mode
                                editingCategoryId = null
                        },
                        onCancel = {editingCategoryId = null}
                    )
                } else {
                    CategoryDisplayCard(
                        category = category,
                        onEdit = { editingCategoryId = category.categoryID },
                        onDelete = { viewModel.deleteCategory(category) }
                    )
                }
            }

           // should be able to add another card or cancel
            if (showAddCard) {
                item {
                    CategoryCard(
                        onSave = { name, budget, spent ->
                            viewModel.createCategory(
                                Category(categoryName = name, budgetAmount = budget, spentAmount = spent)
                            )
                            showAddCard = false
                        },
                        onCancel = {showAddCard = false}
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
                Text("$${String.format("%.2f", category.spentAmount)}")
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