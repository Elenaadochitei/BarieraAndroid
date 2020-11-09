package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalDateTime;
import java.util.Calendar;

public class CustomDataTime implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    int day, month, year, hour, minute;
    private  TextView timeAndDate;
    private Context context;

    public CustomDataTime(Context context,  TextView timeAndDate){
        this.context = context;
        this.timeAndDate  = timeAndDate;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.of(year, month, day, hour, minute);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.setYear(year);
        this.setMonth(month);
        this.setDay(dayOfMonth);
        Calendar c = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, CustomDataTime.this, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.setHour(hourOfDay);
        this.setMinute(minute);
        timeAndDate.setText(getDateAsText());
    }

    public String getDateAsText(){
        return "Data "+this.getDay()+":"+this.getMonth()+":"+getYear()+" Ora "+this.getHour() + ":" + this.getMinute();
    }
}
