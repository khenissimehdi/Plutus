package com.example.plutus.core
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plutus.Graph
import com.example.plutus.core.classes.Booklet
import com.example.plutus.core.classes.Category
import com.example.plutus.core.classes.CurrentBooklet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class CategoryViewState(
    var categories: List<Category> = emptyList()
)

@OptIn(InternalCoroutinesApi::class)
class CategoryViewModel(private val currentBookletRepo: CategoryRepo = Graph.categryRepository) : ViewModel() {
    private val _state = MutableStateFlow(CategoryViewState())
    val state: StateFlow<CategoryViewState>
        get() = _state

    suspend fun insert(name: String): Long {
        return currentBookletRepo.insert(Category(name))
    }

    init {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val value =  currentBookletRepo.allCategories().collect {
                    _state.value = CategoryViewState(it)
                }
            }
        }
    }

}