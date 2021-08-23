package com.test.myapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DatePickerHandler {
    Context context;
    EditText dateEditText;
    DatePickerDialog datePickerDialog;
    public DatePickerHandler(Context context, EditText resource, java.util.Date date) throws Exception {
        if (context == null) throw new Exception("Context can not be null");
        if (resource == null) throw new Exception("View can not be null");
        this.context = context;
        this.dateEditText = resource;
        if (date != null) {
            dateEditText.setText(CustomDate.getStringDate(date));
        }
        else {
            dateEditText.setText(null);
        }
        dateEditText.setHint(context.getString(R.string.date_format_placeholder));
        dateEditText.setOnClickListener(this::handleBirthdayPicker);
        dateEditText.setFocusable(false);
    }

    public void setDate(java.util.Date date) {
        if (date != null) {
            dateEditText.setText(CustomDate.getStringDate(date));
        }
        else {
            dateEditText.setText(null);
        }
    }

    private void handleBirthdayPicker(View view) {
        int mYear, mMonth, mDay;
        java.util.Date dateShown = new java.util.Date();
        if(dateEditText.getText() != null){
            String dateStr = dateEditText.getText().toString();
            SimpleDateFormat formatter = new SimpleDateFormat(context.getString(R.string.date_format), Locale.ENGLISH);
            try {
                dateShown = formatter.parse(dateStr);
            }catch (Exception ignored){

            }
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateShown);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(context,
                (v, year, monthOfYear, dayOfMonth) -> {
                    int monthInt = monthOfYear + 1;
                    String monthString = "" + monthInt;
                    if(monthInt < 10){
                        monthString = "0" + monthString; //add leading zero to the month
                    }
                    String dayString = "" + dayOfMonth;
                    if(dayOfMonth < 10){
                        dayString = "0" + dayOfMonth; //add leading zero to the date
                    }
                    dateEditText.setText(String.format(context.getString(R.string.date_format_placeholder_signature), dayString, monthString, year));
                    dateEditText.setSelection(dateEditText.getText().length());
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
