package edu.mushrchun.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.mushrchun.http.HttpAPI;
import edu.mushrchun.item.R;
import edu.mushrchun.model.HistoricCurrency;
import edu.mushrchun.model.TrendCurrency;
import edu.mushrchun.tool.TrendDataFilter;
import edu.mushrchun.view.SplineChart03View;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryCurrencyFragment extends Fragment {
	
	private TextView tv_startDate;
	private TextView tv_startDate2;
	private TextView tv_endDate;
	private TextView tv_endDate2;
	private Button bt_submit;
	private SplineChart03View chart;
	private ProgressBar pb_refresh;
	
	private Fragment mFragment = this;
	
	private ArrayList<TrendCurrency> mTCList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_splinechart, container,
				false);
		tv_startDate = (TextView) rootView.findViewById(R.id.tv_startDate);
		tv_startDate2 = (TextView) rootView.findViewById(R.id.tv_startDate2);
		tv_endDate = (TextView) rootView.findViewById(R.id.tv_endDate);
		tv_endDate2 = (TextView) rootView.findViewById(R.id.tv_endDate2);
		bt_submit = (Button) rootView.findViewById(R.id.bt_submitDate);
		chart = (SplineChart03View) rootView.findViewById(R.id.chart_view);
		pb_refresh = (ProgressBar) rootView.findViewById(R.id.pb_refresh);
		chart.setVisibility(chart.INVISIBLE);
		pb_refresh.setVisibility(pb_refresh.GONE);
		
		tv_startDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.setTargetFragment(mFragment, 1);
			    newFragment.show(fm, "datePicker");	
			}
		});
		
		tv_startDate2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new TimePickerFragment();
				newFragment.setTargetFragment(mFragment, 2);
			    newFragment.show(getFragmentManager(), "timePicker");				
			}
		});
		
		tv_endDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.setTargetFragment(mFragment, 3);
			    newFragment.show(getFragmentManager(), "datePicker");	
			}
		});
		
		tv_endDate2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new TimePickerFragment();
				newFragment.setTargetFragment(mFragment, 4);
				newFragment.show(getFragmentManager(), "timePicker");	
			}
		});
		
		bt_submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pb_refresh.setVisibility(ProgressBar.VISIBLE);
				String startDate = tv_startDate.getText()+" "+tv_startDate2.getText();
				String endDate = tv_endDate.getText()+" "+tv_endDate2.getText();
				SimpleDateFormat df= new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
				Date sDate;
				Date eDate;
				try {
					sDate = df.parse(startDate);
					eDate = df.parse(endDate);
					Toast.makeText(mFragment.getActivity().getApplicationContext(), sDate.getTime()+"-"+eDate.getTime(),Toast.LENGTH_SHORT).show();
					new Thread(new GetTrend(sDate,eDate)).start();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		return rootView;
	}
	
	final Handler handler = new Handler(){
		public void handleMessage(Message msg){
			pb_refresh.setVisibility(ProgressBar.GONE);
			switch(msg.what){
			case 1:
				
				HttpAPI httpAPI = HttpAPI.getInstance();
				ArrayList<TrendCurrency> tcl = httpAPI.getTcl();
				ArrayList<TrendCurrency> ntcl = TrendDataFilter.Filter(tcl);
				chart.chartDataSet(ntcl);
				chart.chartLabels(ntcl);
				break;
			case 0:
				chart.chartDataSet();
				chart.chartLabels();
			}
			
			
   			chart.invalidate();
	   		chart.setVisibility(chart.VISIBLE);
			super.handleMessage(msg);;
		}
	};
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		case 1:tv_startDate.setText(data.getStringExtra("result"));break;
		case 2:tv_startDate2.setText(data.getStringExtra("result"));break;
		case 3:tv_endDate.setText(data.getStringExtra("result"));break;
		case 4:tv_endDate2.setText(data.getStringExtra("result"));break;
		}
		
	}
	
	class GetTrend implements Runnable{

		private Date startDate;
		private Date endDate;
		
		GetTrend(Date sd,Date ed){
			startDate = sd;
			endDate = ed;
		}
		@Override
		public void run() {
			
			Message message = Message.obtain();
				
			HttpAPI httpAPI = HttpAPI.getInstance();
			httpAPI.GetTrends(startDate, endDate);
			
			if(httpAPI.getTcl()!=null){
				message.what=1;
				
			}
			else{
				message.what=0;
			}
			
			
			handler.sendMessage(message);
			
		}
		
		
	};

	
}
