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

import com.example.plutus.composables.ui.category.*

import com.example.plutus.composables.ui.booklet.BookletGrid
import com.example.plutus.composables.ui.header.HeaderTransactions
import com.example.plutus.composables.ui.transaction.TransactionGrid
import com.example.plutus.core.CategoryViewModel
import com.example.plutus.core.CurrentBookletViewModel
import com.example.plutus.core.TransactionViewModel
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

    ModalBottomSheetLayout(
        sheetContent = {
            Card(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                backgroundColor = Color.Black,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp)

            ) {

                if(currentNav.value.equals("Home")) {
                    content()
                } else {
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
    navController: NavController,
    onClick: () -> Unit,
    categoryViewModel: CategoryViewModel
) {
    val selectedCategory = remember { mutableStateOf(Category(1,"1")) }
    val result = remember { mutableStateOf("") }
    val selectedItem = remember { mutableStateOf("Home") }
    val fabShape = RoundedCornerShape(50)
    val viewState by categoryViewModel.state.collectAsState()



    Scaffold(
        modifier = Modifier.padding(bottom = 24.dp),
        floatingActionButton = {

            FloatingActionButton(
                onClick =  onClick ,
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
            /* CategoryTabs(
                categories = categories,
                selectedCategory = selectedCategory.value,
                onCategorySelected = {e -> selectedCategory.value = e},
            )*/

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.height(160.dp)) {
                HeaderTransactions()
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

                        TransactionGrid(navController = navController, currentBookletViewModel = viewModel, seletedCategory = selectedCategory.value)
                    } else {
                        BookletGrid(navController = navController, seleted = selectedItem, currentBookletViewModel = viewModel, currentNav = currentNav)
                    }

                }
            }

        }
    }
}