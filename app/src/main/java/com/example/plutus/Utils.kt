package com.example.plutus


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.Composable
import org.json.JSONArray
import org.json.JSONObject

 class BitMapUtils {


    companion object {
        private var cachedFlag: Bitmap? = null
        fun getIcon(context: Context, name: String): Bitmap? {
            return cachedFlag ?: run {
                cachedFlag = loadImage(context, name)
                Log.d("bitmap", "$cachedFlag")
                cachedFlag
            }
        }
        private const val ICONS_DIRECTORY = "icons"
        private fun loadImage(context: Context, code: String): Bitmap {
            return context.assets.open("$ICONS_DIRECTORY/${code.lowercase()}.png").use {
                BitmapFactory.decodeStream(it)
            }
        }
    }
}