package com.example.plutus.core
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plutus.Graph
import com.example.plutus.core.classes.Booklet
import com.example.plutus.core.classes.CurrentBooklet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class HomeCurrentBookLetViewState(
    var bookletcurr: CurrentBooklet = CurrentBooklet()
)

@OptIn(InternalCoroutinesApi::class)
class CurrentBookletViewModel(private val currentBookletRepo: CurrentBookletRepo = Graph.currentBookletRepository) : ViewModel() {
    private val _state = MutableStateFlow(HomeCurrentBookLetViewState())
    val state: StateFlow<HomeCurrentBookLetViewState>
        get() = _state

    suspend fun update(id: Long): Boolean {
        val currBookelt = CurrentBooklet(id)

        currentBookletRepo.insert(currentBooklet = currBookelt, _state.value.bookletcurr.id.toInt())
        _state.value = HomeCurrentBookLetViewState(bookletcurr = currBookelt)
        return true
    }

    init {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val value =  currentBookletRepo.currentBookLet()
               _state.value = HomeCurrentBookLetViewState(bookletcurr = value)
            }
        }
    }

}