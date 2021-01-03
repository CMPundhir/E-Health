package com.example.e_health.commons.pickers;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    public TimePickerFragment(OnTimeSelectedListener listener) {
        this.listener = listener;
    }

    public OnTimeSelectedListener listener;
    public interface OnTimeSelectedListener{
        void onTimeSelected(String date);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        listener.onTimeSelected(String.format(Locale.ENGLISH, "%02d:%02d",hourOfDay,minute));
    }
}