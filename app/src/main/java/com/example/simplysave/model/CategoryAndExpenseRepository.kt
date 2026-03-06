package com.example.simplysave.model

import kotlinx.coroutines.flow.Flow

class CategoryAndExpenseRepository(private val catDao: CategoryDAO, private val expDao: ExpenseDAO){


suspend fun deleteCategory(category: Category) {
    catDao.deleteCategory(category)
}

suspend fun addCategory( category:Category): Long{
    return catDao.addCategory(category)
}

suspend fun updateCategory(category: Category){
    catDao.updateCategory(category)
}

 fun getAllCategories():Flow<List<Category>>{
    return catDao.getAllCategories()
}

suspend fun getCategoryByID(catID: Int): Category?{
      return catDao.getCategoryByID(catID)
}

suspend fun deleteExpense(expense: Expense){
    expDao.deleteExpense(expense)
}

suspend fun insertExpense(expense:Expense){
        expDao.insertExpense(expense)
    }

suspend fun updateExpense(expense: Expense){
    expDao.updateExpense(expense)
}

 fun getExpenseByCategory(catID: Int): Flow<List<Expense>>{
    return expDao.getExpenseByCategory(catID)
}

suspend fun getExpenseByID(expenseID: Int): Expense?{
    return expDao.getExpenseByID(expenseID)
    }
}