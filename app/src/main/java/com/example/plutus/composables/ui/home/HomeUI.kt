package com.example.plutus.composables.ui.home


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.plutus.composables.actions.booklet.addingBooklet
import com.example.plutus.composables.actions.booklet.updateBooklet

import com.example.plutus.composables.ui.category.*

import com.example.plutus.composables.ui.booklet.BookletGrid
import com.example.plutus.composables.ui.booklet.EditBookletModal
import com.example.plutus.composables.ui.header.HeaderTransactions
import com.example.plutus.composables.ui.transaction.TransactionGrid
import com.example.plutus.core.CategoryViewModel
import com.example.plutus.core.CurrentBookletViewModel
import com.example.plutus.core.TransactionViewModel
import com.example.plutus.core.classes.Booklet
import com.example.plutus.core.classes.Category
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterialApi
@Composable
fun ModalBottomSheet(viewModel: TransactionViewModel = viewModel(),
                     currentBookletViewModel: CurrentBookletViewModel,
                     selectedCategory2: Category,
                     onCategorySelected: (Category) -> Unit,
                     navController: NavController,
                     currentNav: MutableState<String>,
                     categoryViewModel: CategoryViewModel,
                     content: @Composable() () -> Unit,

                     ) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val bookletToEdit = remember {
        mutableStateOf(Booklet())
    }
    val editButtonClicked = remember {
        mutableStateOf(false)
    }


    ModalBottomSheetLayout(
        sheetContent = {
            Card(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                backgroundColor = Color.Black,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp)

            ) {

                if(currentNav.value.equals("Home") ) {
                    content()

                }
                if(editButtonClicked.value) {
                    updateBooklet(navController = navController, currentBookletViewModel = currentBookletViewModel, booklet =  bookletToEdit.value)
                }
                if(!editButtonClicked.value) {
                    addingBooklet(navController = navController, currentBookletViewModel = currentBookletViewModel)
                }
            }
        },
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent
    ) {
        MainContent(
            selectedCategory2 = selectedCategory2,
            onCategorySelected =  onCategorySelected,
            navController = navController,
            editButtonClicked = editButtonClicked,
            bookletToEdit = bookletToEdit,
            currentNav = currentNav,
            onClick = {
                coroutineScope.launch {
                    sheetState.animateTo(ModalBottomSheetValue.Expanded)

                }}, viewModel = currentBookletViewModel, categoryViewModel = categoryViewModel )
    }
}

@Composable
fun MainContent(selectedCategory2: Category,
                viewModel: CurrentBookletViewModel,
                onCategorySelected: (Category) -> Unit,
                navController: NavController,
                editButtonClicked: MutableState<Boolean>,
                bookletToEdit: MutableState<Booklet>,
                onClick: () -> Unit,
                currentNav: MutableState<String>,
                modifier: Modifier = Modifier,
                categoryViewModel: CategoryViewModel
) {

    HomeContent(
        selectedCategory2 = selectedCategory2,
        onCategorySelected = onCategorySelected,
        navController = navController,
        currentNav = currentNav,
        editButtonClicked = editButtonClicked,
        bookletToEdit = bookletToEdit,
        onClick = onClick,
        viewModel = viewModel,
        categoryViewModel = categoryViewModel
    )

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeContent(
    selectedCategory2: Category,
    onCategorySelected: (Category) -> Unit,
    viewModel: CurrentBookletViewModel,
    currentNav: MutableState<String>,
    editButtonClicked: MutableState<Boolean>,
    bookletToEdit: MutableState<Booklet>,
    navController: NavController,
    onClick: () -> Unit,
    categoryViewModel: CategoryViewModel
) {
    val selectedCategory = remember { mutableStateOf(Category(Int.MAX_VALUE.toLong(),"all")) }
    val result = remember { mutableStateOf("") }
    val selectedItem = remember { mutableStateOf("Home") }
    val fabShape = RoundedCornerShape(50)
    val viewState by categoryViewModel.state.collectAsState()

    val moneyState = remember { mutableStateOf(0)}



    Scaffold(
        modifier = Modifier.padding(bottom = 24.dp),
        floatingActionButton = {

            FloatingActionButton(
                onClick =  {
                    onClick()
                    editButtonClicked.value = false
                 },
                shape = fabShape,
                backgroundColor = Color(0xFFFF8C00)
            ) {
                Icon(Icons.Filled.Add,"")
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomAppBar(
                cutoutShape = fabShape,
                content = {
                    BottomNavigation() {
                        BottomNavigationItem(
                            icon = {
                                Icon(Icons.Filled.Home , "")
                            },
                            label = { Text(text = "Home") },
                            selected = selectedItem.value == "Home",
                            onClick = {
                                currentNav.value = "Home"
                                selectedItem.value = "Home"
                            },
                            alwaysShowLabel = false
                        )

                        BottomNavigationItem(
                            icon = {
                                Icon(Icons.Filled.Book  ,  "Booklets")
                            },

                            label = { Text(text = "Booklets") },
                            selected = selectedItem.value == "Booklets",
                            onClick = {
                                currentNav.value = "Booklets"
                                selectedItem.value = "Booklets"

                            },
                            alwaysShowLabel = false
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.height(160.dp)) {
                HeaderTransactions(moneyState)
            }
            Card(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                backgroundColor = Color.DarkGray,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 70.dp)

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    CategoryTabs(
                        categories = viewState.categories,
                        selectedCategory = selectedCategory.value,
                        onCategorySelected = { e ->
                            run {
                                selectedCategory.value = e
                            }
                        },
                    )
                    if (selectedItem.value.equals("Home")) {
                        TransactionGrid(
                            navController = navController,
                            currentBookletViewModel = viewModel,
                            seletedCategory = selectedCategory.value,
                            moneyState = moneyState)
                    } else {
                        BookletGrid(
                            navController = navController,
                            seleted = selectedItem,
                            editButtonClicked = editButtonClicked,
                            bookletToEdit = bookletToEdit,
                            onClick = onClick,
                            currentBookletViewModel = viewModel,
                            currentNav = currentNav, )
                    }

                }
            }

        }
    }
}