package com.example.simplysave.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplysave.model.Category
import com.example.simplysave.model.CategoryAndExpenseRepository
import com.example.simplysave.model.Expense
import com.example.simplysave.model.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryAndExpenseViewModel(
    private val categoryAndExpenseRepository: CategoryAndExpenseRepository
): ViewModel(){

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories


    private val _monthlyIncome = MutableStateFlow<UserSettings?>(null)
    val monthlyIncome :StateFlow<UserSettings?> = _monthlyIncome
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    private var _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady
    init {
      viewModelScope.launch {
          categoryAndExpenseRepository.getAllCategories()
              .collect { _categories.value = it }
      }

        viewModelScope.launch{
            categoryAndExpenseRepository.fetchMonthlyIncome()
                .collect { _monthlyIncome.value = it.firstOrNull()
                    _isReady.value = true
                }
        }
    }

 fun deleteCategory(category:Category) {
     viewModelScope.launch {
         categoryAndExpenseRepository.deleteCategory(category)

     }
 }

     fun createCategory(category: Category){
         viewModelScope.launch{
             categoryAndExpenseRepository.addCategory(category)
         }
     }

    fun updateCategory(category: Category){
        viewModelScope.launch{
            categoryAndExpenseRepository.updateCategory(category)

        }
    }

    fun getCategoryByID(catID:Int){
        viewModelScope.launch {
            categoryAndExpenseRepository.getCategoryByID(catID)
        }
    }

    fun saveMonthlyIncome(income: Double){
        viewModelScope.launch {  categoryAndExpenseRepository.upsertMonthlyIncome(UserSettings(id=1, monthlyIncome = income)) }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            categoryAndExpenseRepository.deleteExpense(expense)
        }
    }

    fun insertExpense(expense: Expense){
         viewModelScope.launch {
             categoryAndExpenseRepository.insertExpense(expense)
         }
    }

    fun updateExpense(expense: Expense){
        viewModelScope.launch{
             categoryAndExpenseRepository.updateExpense(expense)
        }
    }


    fun loadExpenseByCategory(catID: Int){
        viewModelScope.launch {
            categoryAndExpenseRepository.getExpenseByCategory(catID)
                .collect { _expenses.value = it }
          }
        }


    fun loadExpenseByID(expenseID:Int){
        viewModelScope.launch {
            categoryAndExpenseRepository.getExpenseByID(expenseID)
        }
    }

    fun createCategoryAndExpense(categoryName: String, budgetAmount: Double, spentAmount: Double){
        viewModelScope.launch {
           val newCategoryID = categoryAndExpenseRepository.addCategory(
               Category(
                   categoryName = categoryName,
                   budgetAmount = budgetAmount,
                   spentAmount = spentAmount
               )
           )

        }
    }
}


