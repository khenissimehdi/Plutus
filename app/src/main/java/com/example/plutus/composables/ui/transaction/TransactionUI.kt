package com.example.plutus.composables.ui.transaction

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.plutus.composables.ui.header.HeaderView
import com.example.plutus.core.CurrentBookletViewModel
import com.example.plutus.core.TransactionViewModel
import com.example.plutus.core.classes.Category
import com.example.plutus.core.classes.Transaction
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionGrid(navController: NavController,
                    viewModel: TransactionViewModel = viewModel(),
                    currentBookletViewModel: CurrentBookletViewModel,
                    seletedCategory: Category

) {
    var infoSelect: Int by remember {
        mutableStateOf(0)
    }
    val viewState by viewModel.state.collectAsState()
    val currentBookletViewState by currentBookletViewModel.state.collectAsState()
    val format = SimpleDateFormat("dd/MM/yyy")
    if (!viewState.transactions.isEmpty()) {

        //Log.i("Categ", c.toString() )
        var transactionByBookletId = viewState.transactions.filter { it.transaction.bookletIdT == currentBookletViewState.bookletcurr.id.toInt()}

        LazyVerticalGrid(
            cells = GridCells.Fixed(1)
        ) {

            items(transactionByBookletId) { item ->

                item.transaction.title;
                val textColor = if (item.transaction.price <0) Color.Red else Color.Green

              if(item.categories.isNotEmpty() && item.categories[0] .id == seletedCategory.id) {
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
                                style =  TextStyle(
                                    color = textColor,
                                    fontSize = 16.sp
                                )
                            )
                            Text(
                                text = format.format(item.transaction.date),
                                style =  TextStyle(
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun ShowTransaction(transaction: Transaction, navController: NavController) {

    val result = remember { mutableStateOf("") }
    val selectedItem = remember { mutableStateOf("upload") }

    Scaffold(
        modifier = Modifier.padding(bottom = 24.dp),
        bottomBar = {
            BottomAppBar(
                content = {
                    BottomNavigation() {
                        BottomNavigationItem(
                            icon = {
                                Icon(Icons.Filled.Home , "")
                            },
                            label = { Text(text = "All Transaction")},
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


        TransactionLayout(transaction)
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun TransactionLayout(transaction: Transaction){
    val format = SimpleDateFormat("dd/MM/yyy")
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
                        .padding(top = 20.dp, bottom = 20.dp).align(Alignment.CenterHorizontally),
                    text = format.format(transaction.date),
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp
                )

            }
        }
    }

}