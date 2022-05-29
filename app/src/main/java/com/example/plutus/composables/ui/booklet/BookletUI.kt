package com.example.plutus.composables.ui.booklet

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.plutus.core.BookletViewModel
import com.example.plutus.core.CurrentBookletViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.logging.SimpleFormatter


@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookletGrid(navController: NavController, seleted: MutableState<String>, viewModel: BookletViewModel = viewModel(), currentBookletViewModel: CurrentBookletViewModel, currentNav: MutableState<String>) {
    var infoSelect: Int by remember {
        mutableStateOf(0)
    }
    val format = SimpleDateFormat("dd/MM/yyy")
    val viewState by viewModel.state.collectAsState()
    val currentBookletViewState by currentBookletViewModel.state.collectAsState()

    if (!viewState.booklets.isEmpty()) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(1)
        ) {
            items(viewState.booklets) { item ->
                item.title
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .clickable {
                            CoroutineScope(Dispatchers.IO).launch {
                                val go = currentBookletViewModel.update(item.bookletId.toLong())
                                if(go) {
                                    currentNav.value = "Home"
                                    seleted.value = "Home"
                                }
                            }},
                    elevation = 10.dp

                ) {
                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.body1,
                        )
                        Text(
                            text = format.format(item.date),
                            style = MaterialTheme.typography.body1,
                        )
                    }
                }
            }
        }
    }
}