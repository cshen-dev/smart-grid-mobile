package edu.mushrchun.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
	
	private Calendar mCalendar;
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
    	mCalendar = Calendar.getInstance();
    	mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
    	mCalendar.set(Calendar.MINUTE, minute);
    	sendResult(0);
    }
	private void sendResult(int RESULT_CODE) {
		SimpleDateFormat df = new SimpleDateFormat("HH时mm分");
		String result = df.format(mCalendar.getTime());
	    Intent intent = new Intent();
	    intent.putExtra("result", result);
	    getTargetFragment().onActivityResult(
	        getTargetRequestCode(), RESULT_CODE, intent);
	}
}
