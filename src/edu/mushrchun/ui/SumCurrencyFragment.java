package edu.mushrchun.ui;

import java.util.ArrayList;
import java.util.List;

import edu.mushrchun.http.HttpAPI;
import edu.mushrchun.item.R;
import edu.mushrchun.model.HistoricCurrency;
import edu.mushrchun.view.BarChart3D02View;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;


public class SumCurrencyFragment extends Fragment{
	
	private BarChart3D02View chart3d;
	private ProgressBar pb_refresh;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_barchart_3d, container,
				false);
		chart3d = (BarChart3D02View) rootView.findViewById(R.id.chart_view);
		
		Button bt_today = (Button) rootView.findViewById(R.id.bt_today);
		Button bt_7days = (Button) rootView.findViewById(R.id.bt_7days);
		Button bt_30days = (Button) rootView.findViewById(R.id.bt_30days);
		pb_refresh = (ProgressBar) rootView.findViewById(R.id.pb_refresh);
		pb_refresh.setVisibility(ProgressBar.INVISIBLE);
		chart3d.setVisibility(chart3d.INVISIBLE);
		bt_today.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new FindToday()).start();
				pb_refresh.setVisibility(ProgressBar.VISIBLE);
				
				
			}
		});
		
		bt_7days.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new Find7Days()).start();
				pb_refresh.setVisibility(ProgressBar.VISIBLE);
				
				
			}
		});
		
		bt_30days.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new Find30Days()).start();
				pb_refresh.setVisibility(ProgressBar.VISIBLE);
				
				
			}
		});
		
		return rootView;
	}
	
	final Handler handler = new Handler(){
		public void handleMessage(Message msg){
			pb_refresh.setVisibility(ProgressBar.GONE);
			Bundle bundle = msg.getData();
			ArrayList<HistoricCurrency> hisCurList = bundle.getParcelableArrayList("hisCurList");
			chart3d.chartDataSet(hisCurList,msg.what);
			chart3d.chartLabels(hisCurList);
  
   			chart3d.invalidate();
   			chart3d.setVisibility(chart3d.VISIBLE);
			super.handleMessage(msg);;
		}
	};
	
	
	class FindToday implements Runnable{

		@Override
		public void run() {
			
			Message message = Message.obtain();
				
			HttpAPI httpAPI = HttpAPI.getInstance();
			ArrayList<HistoricCurrency> hisCurList = httpAPI.GetHistory();
			if(hisCurList!=null){
				message.what=1;
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("hisCurList", hisCurList);
				message.setData(bundle);
			}
			else{
				message.what=0;
			}
			
			
			handler.sendMessage(message);
			
		}
		
		
	};
	
	class Find7Days implements Runnable{

		@Override
		public void run() {
			
			Message message = Message.obtain();
				
			HttpAPI httpAPI = HttpAPI.getInstance();
			ArrayList<HistoricCurrency> hisCurList = httpAPI.GetHistory();
			if(hisCurList!=null){
				message.what=2;
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("hisCurList", hisCurList);
				message.setData(bundle);
			}
			else{
				message.what=0;
			}
			
			
			handler.sendMessage(message);
			
		}
		
		
	};
	
	class Find30Days implements Runnable{

		@Override
		public void run() {
			
			Message message = Message.obtain();
				
			HttpAPI httpAPI = HttpAPI.getInstance();
			ArrayList<HistoricCurrency> hisCurList = httpAPI.GetHistory();
			if(hisCurList!=null){
				message.what=3;
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("hisCurList", hisCurList);
				message.setData(bundle);
			}
			else{
				message.what=0;
			}
			
			
			handler.sendMessage(message);
			
		}
		
		
	};
}
