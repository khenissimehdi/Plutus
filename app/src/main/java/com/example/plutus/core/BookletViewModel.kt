package com.example.plutus.core

import androidx.lifecycle.*
import com.example.plutus.Graph
import com.example.plutus.core.classes.*
import com.example.plutus.core.dao.BookletDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

import kotlinx.coroutines.launch


@OptIn(InternalCoroutinesApi::class)
class BookletViewModel(private val bookletRepo: BookletRepo = Graph.bookletRepository) : ViewModel() {
    private val _state = MutableStateFlow(HomeBookLetViewState())
    private val _selectedCategory = MutableStateFlow(Booklet())

    val state: StateFlow<HomeBookLetViewState>
        get() = _state

    suspend fun insertBooklet(booklet: Booklet) {
        bookletRepo.insert(booklet = booklet)
    }

    suspend fun updateBooklet(booklet: Booklet) {
        bookletRepo.update(booklet = booklet)
    }

    init {
        viewModelScope.launch {
            bookletRepo.allBooklets().collect {
                _state.value = HomeBookLetViewState(booklets = it)
            }
        }
    }

}

data class HomeBookLetViewState(
    var booklets:List<Booklet> = emptyList()
)
