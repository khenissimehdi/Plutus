package com.example.plutus.composables.ui.booklet

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PanoramaFishEye
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.plutus.core.BookletViewModel
import com.example.plutus.core.CurrentBookletViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.plutus.composables.actions.booklet.updateBooklet
import com.example.plutus.composables.actions.transaction.updateTransaction
import com.example.plutus.composables.ui.transaction.TransactionLayout
import com.example.plutus.core.classes.Booklet
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.logging.SimpleFormatter


@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun BookletGrid(navController: NavController,onClick: () -> Unit, seleted: MutableState<String>, viewModel: BookletViewModel = viewModel(), editButtonClicked: MutableState<Boolean>,
                bookletToEdit: MutableState<Booklet>, currentBookletViewModel: CurrentBookletViewModel, currentNav: MutableState<String>) {
    val format = SimpleDateFormat("dd/MM/yyy")
    val viewState by viewModel.state.collectAsState()
    val currentBookletViewState by currentBookletViewModel.state.collectAsState()

    if (!viewState.booklets.isEmpty()) {
      LazyVerticalGrid(
            columns = GridCells.Fixed(1)
        ) {
            items(viewState.booklets) { item ->
                item.title
                Card(
                    modifier = Modifier
                        .padding(15.dp)
                        ,
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

                    Row(modifier= Modifier
                        .fillMaxSize()
                        .padding(25.dp), verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.End) {

                       Button(onClick = {
                           CoroutineScope(Dispatchers.IO).launch {
                           val go = currentBookletViewModel.update(item.bookletId.toLong())
                           if (go) {
                               currentNav.value = "Home"
                               seleted.value = "Home"
                           }
                       } }) {
                           if(currentBookletViewState.bookletcurr!!.id == item.bookletId.toLong()) {
                               Icon(
                                   Icons.Filled.Favorite,
                                   "Filled fav",
                                   tint = Color.White)
                           } else {
                               Icon(
                                   Icons.Outlined.FavoriteBorder,
                                   "Outlined fav",
                                   tint = Color.White)
                           }

                       }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(onClick = {
                            editButtonClicked.value = true;
                            bookletToEdit.value = item
                            onClick()
                        }) {
                            Icon(
                                Icons.Filled.Edit,
                                "Edit",
                                tint = Color.White)
                        }
                    }

                }
               
            }
        }

    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditBookletModal() {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    ModalBottomSheetLayout(
        sheetContent = {
            Card(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                backgroundColor = Color.Black,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp)

            ) {
                Text("heloooooooooooo")
                //updateTransaction(navController = navController, currentBookletViewModel = currentBookletViewModel, categModel = categModel,transaction = transaction)
            }
        },
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent
    ) {
        Scaffold(
            modifier = Modifier.padding(bottom = 24.dp),
            bottomBar = {
                BottomAppBar(
                    content = {
                    }
                )
            }
        ) {

        }
    }
}