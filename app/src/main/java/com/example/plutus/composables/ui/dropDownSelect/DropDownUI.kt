package com.example.plutus.composables.ui.dropDownSelect

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize


 /**
  * from https://www.geeksforgeeks.org/drop-down-menu-in-android-using-jetpack-compose/
  * with some modifs
  * */
// Creating a composable function 
// to create an Outlined Text Field
// Calling this function as content
// in the above function
@Composable
fun DropDownInput(selected: MutableState<Int>,selectedString: MutableState<String>, categories: List<String> ){
    if(categories.isEmpty()) {
        return
    }
    // Declaring a boolean value to store 
    // the expanded state of the Text Field
    var mExpanded by remember { mutableStateOf(false) }

    // Create a string value to store the selected city
    var mSelectedText by remember { mutableStateOf("") }
  
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}
  
    // Up Icon when expanded and down icon when collapsed
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
     Row() {
        
        // Create an Outlined Text Field 
        // with icon and not expanded
        OutlinedTextField(
            value = mSelectedText,
            onValueChange = {
                mSelectedText = it
                selectedString.value = mSelectedText
               },
            modifier = Modifier
                .width(200.dp)
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to
                    // the DropDown the same width
                    mTextFieldSize = coordinates.size.toSize()
                },
            label = {Text("Categories")},
            trailingIcon = {
                Icon(icon,"contentDescription",
                    Modifier.clickable { mExpanded = !mExpanded })
            }
        )
          
        // Create a drop-down menu with list of cities, 
        // when clicked, set the Text Field text as the city selected
        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
        ) {
            categories.forEach { label ->
                DropdownMenuItem(onClick = {
                    mSelectedText = label
                    selectedString.value = label
                    selected.value = categories.indexOf(mSelectedText)+1;
                    mExpanded = false
                }) {

                    Text(text = label )

                }
            }
        }
    }
}


@Composable
fun CheckBox(setOf: MutableSet<String>, selected: String) {
    val checkedState = remember { mutableStateOf(false) }
    Checkbox(
        checked = checkedState.value,
        onCheckedChange = {
            checkedState.value = it
            if(it) {
                setOf.add(selected)

            } else {
                setOf.remove(selected)
            }


        }
    )
}
