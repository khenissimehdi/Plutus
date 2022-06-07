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
    var bookletcurr: CurrentBooklet?
)

@OptIn(InternalCoroutinesApi::class)
class CurrentBookletViewModel(private val currentBookletRepo: CurrentBookletRepo = Graph.currentBookletRepository) : ViewModel() {
    private val _state = MutableStateFlow(HomeCurrentBookLetViewState(CurrentBooklet(-1)))


    val state: StateFlow<HomeCurrentBookLetViewState>
        get() = _state

    suspend fun update(id: Long): Boolean {
        val currBookelt = CurrentBooklet(id)
        if(_state.value.bookletcurr != null) {
            currentBookletRepo.insert(currentBooklet = currBookelt, _state.value.bookletcurr!!.id.toInt())
            _state.value = HomeCurrentBookLetViewState(bookletcurr = currBookelt)
            return true
        }
        return false

    }


    init {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val value =  currentBookletRepo.currentBookLet()
                if(value != null) {
                    _state.value = HomeCurrentBookLetViewState(bookletcurr = value)
                }
            }
        }
    }

}