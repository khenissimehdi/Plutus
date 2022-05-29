package com.example.plutus.composables.actions.booklet

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.plutus.core.BookletViewModel
import com.example.plutus.core.CurrentBookletViewModel
import com.example.plutus.core.TransactionViewModel
import com.example.plutus.core.classes.Booklet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun addingBooklet(viewModel: BookletViewModel = viewModel(), navController: NavController, currentBookletViewModel: CurrentBookletViewModel){
    Scaffold(
        topBar = {

        },
        content = {
            var text by remember { mutableStateOf(TextFieldValue("")) }

            var price by remember { mutableStateOf(TextFieldValue("")) }
            //keyboard stuff
            val (focusRequester) = FocusRequester.createRefs()
            val keyboardController = LocalSoftwareKeyboardController.current
            val viewState by currentBookletViewModel.state.collectAsState()
            var accepteTransaction  by remember { mutableStateOf(false) }

            Column(modifier = Modifier.padding(50.dp)) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                        accepteTransaction = text.text.isNotEmpty()
                    },
                    label = { Text(text = "Title") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide()
                            //accepteTransaction = true
                        }
                    ),
                    modifier = Modifier.focusRequester(focusRequester),
                )

                Button(modifier = Modifier.padding(20.dp),
                    enabled = accepteTransaction
                    ,onClick = {
                        if(text.text.isNotEmpty()){
                            CoroutineScope(Dispatchers.IO).launch {
                                var booklet = Booklet(title = text.text, date = "helo")
                              viewModel.insertBooklet(booklet)

                            }
                            navController.navigate("home")
                        }
                    }) {
                    Text(text = "ADD")
                }
            }
        }
    )
}
