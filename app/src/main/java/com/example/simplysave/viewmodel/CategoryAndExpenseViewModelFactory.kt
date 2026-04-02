package com.example.simplysave.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.simplysave.model.CategoryAndExpenseRepository

class CategoryAndExpenseViewModelFactory(
    private val repository: CategoryAndExpenseRepository
): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass:Class<T>): T{
        if(modelClass.isAssignableFrom(CategoryAndExpenseViewModel::class.java)){
            return CategoryAndExpenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}