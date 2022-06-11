package com.example.plutus.composables.ui.transaction

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.plutus.composables.actions.booklet.addingBooklet
import com.example.plutus.composables.actions.transaction.updateTransaction
import com.example.plutus.composables.ui.header.HeaderView
import com.example.plutus.core.CategoryViewModel
import com.example.plutus.core.CurrentBookletViewModel
import com.example.plutus.core.TransactionViewModel
import com.example.plutus.core.classes.Category
import com.example.plutus.core.classes.Possede
import com.example.plutus.core.classes.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionGrid(navController: NavController,
                    viewModel: TransactionViewModel = viewModel(),
                    currentBookletViewModel: CurrentBookletViewModel,
                    seletedCategory: Category,
                    moneyState: MutableState<Int>
) {
    var infoSelect: Int by remember {
        mutableStateOf(0)
    }
    val viewState by viewModel.state.collectAsState()
    val currentBookletViewState by currentBookletViewModel.state.collectAsState()
    val format = SimpleDateFormat("dd/MM/yyy")
    if (viewState.transactions.isNotEmpty()) {
        val transactionByBookletId = viewState.transactions.filter { it.transaction.bookletIdT == currentBookletViewState.bookletcurr!!.id.toInt()}
        moneyState.value = transactionByBookletId.map { it.transaction.price }.sum()
        LazyVerticalGrid(
            columns = GridCells.Fixed(1)
        ) {
            items(transactionByBookletId) { item ->
                item.transaction.title;
                val textColor = if (item.transaction.price <0) Color.Red else Color.Green
                val categIdList = item.categories.map { e -> e.id }
                if(seletedCategory.title == "all") {
                    showALl(item,navController, textColor, format)
                } else {
                    showByCateg(item, categIdList, seletedCategory, navController, textColor, format)
                }
            }
        }
    }
}

@Composable
private fun showALl(
    item: Possede,
    navController: NavController,
    textColor: Color,
    format: SimpleDateFormat
) {
        Card(
            shape = RoundedCornerShape(14.dp),
            backgroundColor = Color.White,
            modifier = Modifier
                .padding(10.dp)
                .width(180.dp)
                .clickable { navController.navigate("transaction/${item.transaction.id}") },
            elevation = 10.dp

        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    text = item.transaction.title,
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = "${item.transaction.price} €",
                    style = TextStyle(
                        color = textColor,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = format.format(item.transaction.date),
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                )
            }
        }
}


@Composable
private fun showByCateg(
    item: Possede,
    categIdList: List<Long>,
    seletedCategory: Category,
    navController: NavController,
    textColor: Color,
    format: SimpleDateFormat
) {
    if (item.categories.isNotEmpty() && categIdList.contains(seletedCategory.id)) {
        Card(
            shape = RoundedCornerShape(14.dp),
            backgroundColor = Color.White,
            modifier = Modifier
                .padding(10.dp)
                .width(180.dp)
                .clickable { navController.navigate("transaction/${item.transaction.id}") },
            elevation = 10.dp

        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    text = item.transaction.title,
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = "${item.transaction.price} €",
                    style = TextStyle(
                        color = textColor,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = format.format(item.transaction.date),
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowTransaction(transaction: Transaction, navController: NavController,viewModel: TransactionViewModel = viewModel(),currentBookletViewModel: CurrentBookletViewModel, categModel: CategoryViewModel) {
    val selectedItem = remember { mutableStateOf("upload") }
    val coroutineScope = rememberCoroutineScope()
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
                updateTransaction(navController = navController, currentBookletViewModel = currentBookletViewModel, categModel = categModel,transaction = transaction)
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
                        BottomNavigation() {
                            BottomNavigationItem(
                                icon = {
                                    Icon(Icons.Filled.Home, "")
                                },
                                label = { Text(text = "All Transaction") },
                                selected = selectedItem.value == "all Transaction",
                                onClick = {
                                    navController.navigate("home")
                                },
                                alwaysShowLabel = false
                            )
                        }
                    }
                )
            }
        ) {


            TransactionLayout(transaction, navController, viewModel, onClick = {
                coroutineScope.launch {
                    sheetState.animateTo(ModalBottomSheetValue.Expanded)
                }})
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun TransactionLayout(transaction: Transaction, navController: NavController,viewModel: TransactionViewModel = viewModel(), onClick :() -> Unit){
    val format = SimpleDateFormat("dd/MM/yyy")
    val context = LocalContext.current;

    val openDialog = remember { mutableStateOf(false)  }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.height(240.dp)) {
            HeaderView()
        }
        Card(
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
            backgroundColor = Color.DarkGray,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp)
            ) {
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    IconButton(modifier = Modifier.
                    then(Modifier.size(24.dp)),
                        onClick = {
                            onClick()
                        }) {
                        Icon(
                            Icons.Filled.Edit,
                            "Edit",
                            tint = Color.White)
                    }
                    IconButton(modifier = Modifier
                        .padding(start = 15.dp)
                        .then(Modifier.size(24.dp)),
                        onClick = {
                            openDialog.value = true
                        }) {
                        Icon(
                            Icons.Filled.Delete,
                            "Delete",
                            tint = Color.White)
                    }
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 20.dp),
                    text = transaction.title,
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 20.dp),
                    text = "${transaction.price} €",
                    textAlign = TextAlign.Center,
                    fontSize = 60.sp
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 20.dp)
                        .align(Alignment.CenterHorizontally),
                    text = format.format(transaction.date),
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp
                )

            }
        }
    }

    if (openDialog.value) {

        AlertDialog(
            shape = RoundedCornerShape(14.dp),
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onCloseRequest.
                openDialog.value = false
            },
            title = {
                Text(fontSize = 22.sp,text = "Do you want to delete the transaction ${transaction.title} ?")
            },
            dismissButton = {
                Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                    onClick = {
                        openDialog.value = false
                    }) {
                    Text("Cancel")
                }
            },
            confirmButton = {
                Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),

                    onClick = {
                        openDialog.value = false
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.deleteById(transaction.id)
                        }
                        Toast.makeText(context, "Delete successful for ${transaction.title}", Toast.LENGTH_LONG).show()
                        navController.navigate("home"){

                            popUpTo("home")
                        }
                    }) {
                    Text("Confirm")
                }
            }
        )
    }

}
