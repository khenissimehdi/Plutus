
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

/**
 * from : https://www.geeksforgeeks.org/date-picker-in-android-using-jetpack-compose/
 * with some changes
 * */
// Creating a composable function to
// create two Images and a spacer between them
// Calling this function as content
// in the above function
@Composable
fun UIDatePicker(dateState: MutableState<Date>) {
    // Fetching the Local Context
    val mContext = LocalContext.current

    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    // Declaring a string value to
    // store date in string format
    val mDate = remember { mutableStateOf("$mDay/${mMonth+1}/$mYear") }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->

            mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
        }, mYear, mMonth, mDay
    )

    Row(modifier = Modifier.width(200.dp)) {

        // Creating a button that on
        // click displays/shows the DatePickerDialog
        Button(onClick = {
            mDatePickerDialog.show()
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray) ) {
            dateState.value = getDateFromDatePicker(mDatePickerDialog.datePicker)
            Text(text = mDate.value, fontSize = 20.sp, textAlign = TextAlign.Center)
        }

    }
}

/**
 *
 * @param datePicker
 * @return a java.util.Date
 */
fun getDateFromDatePicker(datePicker: DatePicker): Date {
    val day = datePicker.dayOfMonth
    val month = datePicker.month
    val year = datePicker.year
    val calendar = Calendar.getInstance()
    calendar[year, month] = day
    return calendar.time
}


