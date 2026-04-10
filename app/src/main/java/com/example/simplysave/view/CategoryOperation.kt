package com.example.simplysave.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.simplysave.viewmodel.CategoryAndExpenseViewModel

// Class saves card or cancels when you are entering info
// Should have named class better

@Composable
fun CategoryCard(
    isFirstCard: Boolean = false,
    initialName: String = "",
    initialBudget: String = "$0.00",
    initialAmountSpent: String = "$0.00",
    onSave: (name: String, budget: Double, spent: Double) -> Unit,
    onCancel: () -> Unit
) {
    var categoryName by remember { mutableStateOf(initialName) }
    var budgetAmount by remember { mutableStateOf(if (initialBudget == "$0.00") "" else initialBudget) }
    var amountSpent by remember { mutableStateOf(if (initialAmountSpent == "$0.00") "" else initialAmountSpent) }



    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Category Name Field
            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text("Category Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Budget Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Budget:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                OutlinedTextField(
                    value = budgetAmount,
                    onValueChange = { budgetAmount = it },
                    placeholder = { Text("\$0.00") },
                    modifier = Modifier.width(120.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Amount Spent Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Amount Spent:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                OutlinedTextField(
                    value = amountSpent,
                    onValueChange = { amountSpent = it },
                    placeholder = { Text("\$0.00") },
                    modifier = Modifier.width(120.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = {
                    val budget = budgetAmount.replace("$", "").toDoubleOrNull() ?: 0.0
                    val spent = amountSpent.replace("$", "").toDoubleOrNull() ?: 0.0

                    if (categoryName.isNotBlank()) {
                        onSave(categoryName, budget, spent)
                    }
                    if(spent > budget){
                        Toast.makeText(context, "You have gone over budget", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SSGreen),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Save", color = Color.White)
            }
            // Cancel Button
            Button(
                onClick = {
                    onCancel();
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ){
                Text("Cancel", color = Color.White)
            }

        }
    }
}



