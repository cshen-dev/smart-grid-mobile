package edu.mushrchun.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;



import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	private Calendar mCalendar;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}
	
	
	public void onDateSet(DatePicker view, int year, int month, int day) {
	// Do something with the date chosen by the user
		mCalendar = Calendar.getInstance();
		mCalendar.set(year, month, day);
		sendResult(0);
	}
	
	private void sendResult(int RESULT_CODE) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
		String result = df.format(mCalendar.getTime());
	    Intent intent = new Intent();
	    intent.putExtra("result", result);
	    getTargetFragment().onActivityResult(
	        getTargetRequestCode(), RESULT_CODE, intent);
	}
}