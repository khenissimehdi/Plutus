package com.example.plutus

import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceActivity
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.plutus.core.TransactionViewModel
import com.example.plutus.ui.theme.PlutusTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlutusTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android" )
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Greeting(name: String, viewModel: TransactionViewModel = viewModel() ) {

    val viewState by viewModel.state.collectAsState()

    

    if (!viewState.transactions.isEmpty()) {
        LazyColumn {
            items(viewState.transactions) { item ->
                Card(
                    modifier = Modifier.padding(4.dp),
                    backgroundColor = Color.LightGray
                ) {
                    Text(
                        text = item.transaction.title,
                        modifier = Modifier.padding(24.dp),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )

                }
            }
           
        }
    }
    Button(modifier = Modifier.size(10.dp).height(10.dp), onClick = {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.insert(title = "ThinkPad","today", 80,5,3,7);

        }
    }) {
        Text(text = "ADD")
    }





}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlutusTheme {
        Greeting("Android")
    }
}