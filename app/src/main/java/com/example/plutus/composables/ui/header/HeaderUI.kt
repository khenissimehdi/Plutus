package com.example.plutus.composables.ui.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plutus.R


@Composable
 fun HeaderView() {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Image(
            modifier = Modifier.wrapContentWidth(),
            bitmap = ImageBitmap.imageResource(id = R.drawable.wallet),
            contentDescription ="Wallet"
        )
    }
}

@Composable
fun HeaderTransactions() {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 20.dp, top=60.dp)
    ) {


        Text(
            text = "Total Balance",
            color = Color.White,
            style = TextStyle(
                fontSize = 22.sp,
                letterSpacing = 2.sp
            )
        )
        Text(
            text = "13.790€",
            color = Color.White,
            style = TextStyle(
                fontSize = 42.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight(500)
            )
        )

    }

}