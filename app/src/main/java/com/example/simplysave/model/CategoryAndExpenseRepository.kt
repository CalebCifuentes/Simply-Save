package com.example.simplysave.model

class CategoryAndExpenseRepository(private val catDao: CategoryDAO, private val expDao: ExpenseDAO){


suspend fun deleteCategory(category: Category) {
    catDao.deleteCategory(category)
}

suspend fun addCategory(vararg category:Category){
    catDao.addCategory(*category)
}

suspend fun updateCategory(category: Category){
    catDao.updateCategory(category)
}

suspend fun getAllCategories():List<Category>{
    return catDao.getAllCategories()
}

suspend fun getCategoryByID(id: Int): Category?{
      return catDao.getCategoryByID(id)
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

suspend fun getExpenseByCategory(catID: Int): List<Expense>{
    return expDao.getExpenseByCategory(catID)
}

suspend fun getExpenseByID(id: Int): Expense?{
    return expDao.getExpenseByID(id)
    }
}