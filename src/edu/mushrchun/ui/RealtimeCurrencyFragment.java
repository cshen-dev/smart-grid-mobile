package edu.mushrchun.ui;

import edu.mushrchun.http.HttpAPI;
import edu.mushrchun.item.R;
import edu.mushrchun.model.RealtimeCurrency;
import edu.mushrchun.view.GaugeChart01View;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RealtimeCurrencyFragment extends Fragment {

	
	private GaugeChart01View chart = null;
	
	private TextView meter = null;
	
	private ProgressBar pb_refresh;
	
	private int hAngle = 0;
	
	public boolean stopTimer=false;
	
	private Runnable timerRunnable;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_gauge_chart, container,
				false);
		
		chart = (GaugeChart01View)rootView.findViewById(R.id.chart_view);
		
		meter = (TextView)rootView.findViewById(R.id.tv_meter);

		pb_refresh = (ProgressBar) rootView.findViewById(R.id.pb_refresh);
		
		timerRunnable = new MyTimer();
		new Thread(timerRunnable).start();
		
		return rootView;
		
		
	}
	
	
	final Handler handler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case 1:
				pb_refresh.setVisibility(ProgressBar.GONE);
				String tReal = msg.getData().getString("real");
				meter.setText(tReal+"KW");
				int tRealN = Integer.valueOf(tReal);
				int tAngle = (tRealN-tRealN/1000*1000)*180/(tRealN/1000*1000+1000);
				int increasement = tAngle-hAngle;
				hAngle = tAngle;
				for(int i=0;i<20;i++){
					chart.setAngle(hAngle+increasement*i/20);
					chart.chartLabels(tRealN/1000+"KW", tRealN/1000+0.5+"KW", tRealN/1000+1+"KW");
		   			chart.chartRender();
		   			chart.invalidate();
				}
				
			}
			super.handleMessage(msg);;
		}
	};
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		stopTimer=true;
		handler.removeCallbacks(timerRunnable);
		super.onStop();
	}

	public class MyTimer implements Runnable{

		
		@Override
		public void run() {
			
			while(!stopTimer){
				try {
					Thread.sleep(1000);
					
					Message message = Message.obtain();
					
					HttpAPI httpAPI = HttpAPI.getInstance();
					RealtimeCurrency realtimeCurrency = httpAPI.GetRealtime();
					Bundle bundle = new Bundle();
					if(realtimeCurrency.getReal()==null){
						stopTimer=true;
					}
					else{
						bundle.putString("real", realtimeCurrency.getReal());
						message.what=1;
						message.setData(bundle);
						
						handler.sendMessage(message);
					}
					
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
