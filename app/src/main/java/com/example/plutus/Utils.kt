package com.example.plutus


import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import com.example.plutus.db.PlutusRoomDatabase
import java.io.File
import java.io.FileWriter
import java.util.*


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



class ExportUtils {
    companion object {
        fun export(db: PlutusRoomDatabase, doneExport: MutableState<Boolean>) {
            val exportDir =  File(Environment.getExternalStorageDirectory(), "Download/export-${UUID.randomUUID()}")
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            for (tableName in db.getTablesNames()) {
                val file = File(exportDir, "$tableName-${UUID.randomUUID()}.csv")
                try {

                    file.createNewFile()
                    val csvWrite = CSVWriter(FileWriter(file))
                    val curCSV: Cursor = db.query("SELECT * FROM $tableName", null)
                    csvWrite.writeNext(curCSV.columnNames)
                    while (curCSV.moveToNext()) {
                        //Which column you want to export
                        val arrStr = arrayOfNulls<String>(curCSV.columnCount)
                        for (i in 0 until curCSV.columnCount - 1) arrStr[i] = curCSV.getString(i)
                        csvWrite.writeNext(arrStr)
                    }
                    csvWrite.close()
                    curCSV.close()
                    doneExport.value = true;
                } catch (sqlEx: Exception) {
                    Log.e("MainActivity", sqlEx.message, sqlEx)
                }
            }

        }

    }
}