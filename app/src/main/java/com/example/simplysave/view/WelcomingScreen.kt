package com.example.simplysave.view


import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplysave.model.CategoryAndExpenseRepository
import com.example.simplysave.viewmodel.CategoryAndExpenseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow


val SSGreen = Color(0xFF006B5F)


    @Composable
    fun WelcomingScreen(onGetStarted:(name:String, budget:Double) -> Unit) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

                Text(
                    text = "SIMPLY SAVE",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = SSGreen
                )


            Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Welcome! Please fill out the \ncategory to get Started :)",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )


            Spacer(modifier = Modifier.height(32.dp))
            IntroCategoryCard(onSave = onGetStarted)
        }
    }

    @Composable
    fun IntroCategoryCard(onSave: (name: String, budget:Double) -> Unit) {
        var categoryName by remember { mutableStateOf("") }
        var budgetAmount by remember { mutableStateOf("")}
        //var amountSpent by remember {mutableStateOf("")}


        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(modifier = Modifier.padding(16.dp)){

                OutlinedTextField(
                    value = categoryName,
                    onValueChange = {categoryName = it},
                    label = {Text("Category Name")},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Budget:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    OutlinedTextField(
                        value = budgetAmount,
                        onValueChange = {budgetAmount = it},
                        placeholder = {Text("$0.00")},
                        modifier = Modifier.width(130.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Amount Spent:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    OutlinedTextField(
                        value = "$0.00",
                        onValueChange = {},
                        enabled = false,
                        modifier = Modifier.width(130.dp),
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val budget = budgetAmount.replace("$", "").toDoubleOrNull() ?:0.0
                        if(categoryName.isNotBlank()){
                            onSave(categoryName, budget)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SSGreen),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ){
                    Text("Save", color = White)
                }
            }
        }
    }


