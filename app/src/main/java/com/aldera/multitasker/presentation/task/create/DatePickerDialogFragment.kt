package com.aldera.multitasker.presentation.task.create

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.aldera.multitasker.ui.util.ConstantDateDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class DatePickerDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private val currentData = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = currentData.get(Calendar.YEAR)
        val month = currentData.get(Calendar.MONTH)
        val day = currentData.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(requireActivity(), this, year, month, day)
        datePickerDialog.datePicker.minDate =
            (System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(1))
        return datePickerDialog
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        currentData.set(Calendar.YEAR, year)
        currentData.set(Calendar.MONTH, month)
        currentData.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val selectedDate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(currentData.time)
        val selectedDateBundle = Bundle()
        selectedDateBundle.putString(ConstantDateDialogFragment.SELECTED_DATE, selectedDate)

        setFragmentResult(ConstantDateDialogFragment.REQUEST_KEY, selectedDateBundle)
    }
}
