package com.example.simplysave.view

import com.example.simplysave.model.UserSettings
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplysave.model.Category
import kotlin.concurrent.timer
import com.example.simplysave.viewmodel.CategoryAndExpenseViewModel
import kotlin.math.cos
import kotlin.math.sin


val sliceColors = listOf(
    Color(0xFF4CAF50),
    Color(0xFF2196F3),
    Color(0xFFFF9800),
    Color(0xFF9C27B0),
    Color(0xFFE91E63),
    Color(0xFF00BCD4),
)
val remainingSliceColor = Color(0xFFE0E0E0)

@Composable
fun CategoryListScreen(viewModel: CategoryAndExpenseViewModel) {
    val categories by viewModel.categories.collectAsState()
    val monthlyIncome by viewModel.monthlyIncome.collectAsState()

    // Track which card is being edited (null = none)
    var editingCategoryId by remember { mutableStateOf<Int?>(null) }
    // Whether to show an empty "add new" card
    var showAddCard by remember { mutableStateOf(false) }
    // For header that will appear and then go away
    var isVisible by remember { mutableStateOf(true) }

    val income = monthlyIncome?.monthlyIncome ?: 0.0
    val totalSpent = categories.sumOf { it.spentAmount }
    val remaining = income - totalSpent
    val isOverBudget = totalSpent > income
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IncomeCard(
            monthlyIncome = monthlyIncome,
            totalSpent = totalSpent,
            remaining = remaining,
            onSaveIncome = { newIncome -> viewModel.saveMonthlyIncome(newIncome) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pie chart
        SimplySavePieChart(
            categories = categories,
            monthlyIncome = income,
            isOverBudget = isOverBudget
        )

        Spacer(modifier = Modifier.height(16.dp))

/*
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

 */


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
fun IncomeCard(
    monthlyIncome: UserSettings?,
    totalSpent: Double,
    remaining: Double,
    onSaveIncome: (Double) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var incomeInput by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Monthly Income",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "$${String.format("%.2f", monthlyIncome?.monthlyIncome ?: 0.0)}",
                        fontSize = 20.sp,
                        color = SSGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
                // Pencil edit icon
                IconButton(onClick = {
                    isEditing = !isEditing
                    incomeInput = monthlyIncome?.monthlyIncome?.toString() ?: ""
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Income",
                        tint = Color.Gray
                    )
                }
            }

            // Inline edit field — only shows when pencil is tapped
            if (isEditing) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = incomeInput,
                    onValueChange = { incomeInput = it },
                    label = { Text("New Monthly Income") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val newIncome = incomeInput.replace("$", "").toDoubleOrNull()
                        if (newIncome != null && newIncome > 0) {
                            onSaveIncome(newIncome)
                            isEditing = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SSGreen),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Save", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Remaining budget row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Spent:", fontWeight = FontWeight.SemiBold)
                Text("$${String.format("%.2f", totalSpent)}", color = Color.Red)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Remaining:", fontWeight = FontWeight.SemiBold)
                Text(
                    text = "$${String.format("%.2f", remaining)}",
                    color = if (remaining < 0) Color.Red else SSGreen
                )
            }
        }
    }
}

// Pie chart drawn with Canvas — no library needed
@Composable
fun SimplySavePieChart(
    categories: List<Category>,
    monthlyIncome: Double,
    isOverBudget: Boolean
) {
    val totalSpent = categories.sumOf { it.spentAmount }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Spending Breakdown",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Canvas(
                modifier = Modifier
                    .size(220.dp)
                    .padding(8.dp)
            ) {
                val diameter = size.minDimension
                val radius = diameter / 2f
                val topLeft = Offset(
                    x = (size.width - diameter) / 2f,
                    y = (size.height - diameter) / 2f
                )
                val arcSize = Size(diameter, diameter)

                if (isOverBudget) {
                    // Whole chart goes red if over budget
                    drawArc(
                        color = Color.Red,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = true,
                        topLeft = topLeft,
                        size = arcSize
                    )
                    drawCenteredText("OVER BUDGET", center, radius * 0.5f)
                } else if (monthlyIncome <= 0.0) {
                    drawArc(
                        color = remainingSliceColor,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = true,
                        topLeft = topLeft,
                        size = arcSize
                    )
                } else {
                    var startAngle = -90f

                    // Draw each category slice
                    categories.forEachIndexed { index, category ->
                        val sweep = ((category.spentAmount / monthlyIncome) * 360f).toFloat()
                        if (sweep > 0f) {
                            val color = if (isOverBudget) Color.Red
                            else sliceColors[index % sliceColors.size]
                            drawArc(
                                color = color,
                                startAngle = startAngle,
                                sweepAngle = sweep,
                                useCenter = true,
                                topLeft = topLeft,
                                size = arcSize
                            )
                            // Draw label on slice
                            val midAngle = Math.toRadians((startAngle + sweep / 2.0))
                            val labelRadius = radius * 0.65f
                            val labelX = center.x + (labelRadius * cos(midAngle)).toFloat()
                            val labelY = center.y + (labelRadius * sin(midAngle)).toFloat()
                            val percent = (category.spentAmount / monthlyIncome * 100).toInt()
                            drawSliceLabel(
                                label = "${category.categoryName}\n\$${String.format("%.0f", category.spentAmount)} (${percent}%)",
                                x = labelX,
                                y = labelY
                            )
                            startAngle += sweep
                        }
                    }

                    // Remaining/unallocated slice
                    val remainingPercent = ((monthlyIncome - totalSpent) / monthlyIncome)
                    if (remainingPercent > 0f) {
                        val remainingSweep = (remainingPercent * 360f).toFloat()
                        drawArc(
                            color = remainingSliceColor,
                            startAngle = startAngle,
                            sweepAngle = remainingSweep,
                            useCenter = true,
                            topLeft = topLeft,
                            size = arcSize
                        )
                        val midAngle = Math.toRadians((startAngle + remainingSweep / 2.0))
                        val labelRadius = radius * 0.65f
                        val labelX = center.x + (labelRadius * cos(midAngle)).toFloat()
                        val labelY = center.y + (labelRadius * sin(midAngle)).toFloat()
                        drawSliceLabel(
                            label = "Future\nExpenses",
                            x = labelX,
                            y = labelY
                        )
                    }
                }
            }
        }
    }
}

// Helper to draw text labels on slices
fun DrawScope.drawSliceLabel(label: String, x: Float, y: Float) {
    val lines = label.split("\n")
    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = 28f
        textAlign = android.graphics.Paint.Align.CENTER
        isFakeBoldText = true
    }
    lines.forEachIndexed { i, line ->
        drawContext.canvas.nativeCanvas.drawText(
            line,
            x,
            y + (i * 32f) - ((lines.size - 1) * 16f),
            paint
        )
    }
}

fun DrawScope.drawCenteredText(text: String, center: Offset, size: Float) {
    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = size
        textAlign = android.graphics.Paint.Align.CENTER
        isFakeBoldText = true
    }
    drawContext.canvas.nativeCanvas.drawText(text, center.x, center.y + size / 3f, paint)
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