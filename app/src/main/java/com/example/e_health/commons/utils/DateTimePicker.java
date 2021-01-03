package com.example.e_health.commons.utils;

import androidx.fragment.app.FragmentManager;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateTimePicker {

    public interface OnDateSelectedListener{
        void onDateSelected(Date date, String formatDate, String format2Date);
    }

    private static final String TAG = "DateTimePicker";
    /*
    * add maven { url "https://jitpack.io" } in build.gradle of project
    * add implementation 'com.github.Kunzisoft:Android-SwitchDateTimePicker:2.0' in build.gradle
    * of app
    * add
    *    <!-- SwitchDateTime style is independent, each element is optional-->
         <!-- Year Precise -->
         <style name="Theme.SwitchDateTime.DateLabelYear">
            <item name="android:textSize">58sp</item>
            <item name="android:textColor">#fff</item>
            <item name="android:textStyle">bold</item>
        </style>
    * in style
    * */

    public static String format1(Date date){
        if(date == null){
            return "";
        }
        return  new SimpleDateFormat("EEE, dd MMM yyyy @ HH:mm", Locale.getDefault()).format(date);
    }

    public static String format2(Date date){
        if(date == null){
            return "";
        }
        return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date);
    }

    public static Date toDate(String str) throws ParseException {
        if(str==null){
            return new Date();
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(str);
    }

    public static String changeFormat(String str) {
        if(str==null){
            return "";
        }
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(str);
            return format1(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
