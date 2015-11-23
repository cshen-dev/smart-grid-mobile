package edu.mushrchun.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.mushrchun.http.HttpAPI;
import edu.mushrchun.item.R;
import edu.mushrchun.model.Device;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceFragment extends Fragment {
	
	private DeviceFragment deviceFragment = this;
	private ListView lv_device;
	private ProgressBar pb_refresh;
	private EditText et_dname;
	private EditText et_did;
	private TextView tv_dname;
	private TextView tv_did;
	private Button bt_add;
	
	private Handler mHandler;
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(resultCode ==10){
			String device_new_name = data.getStringExtra("device_new_name");
			HttpAPI httpAPI = HttpAPI.getInstance();
			new Thread(new UpdateRunnable(httpAPI.getDeviceList().get(requestCode).getDevice_id(),device_new_name)).start();
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_device, container,
				false);
		
		lv_device = (ListView) rootView.findViewById(R.id.lv_device);
		pb_refresh = (ProgressBar) rootView.findViewById(R.id.pb_refresh);
		et_dname = (EditText) rootView.findViewById(R.id.et_dname);
		et_did = (EditText) rootView.findViewById(R.id.et_did);
		tv_dname = (TextView) rootView.findViewById(R.id.tv_dname);
		tv_did = (TextView) rootView.findViewById(R.id.tv_did);
		bt_add = (Button) rootView.findViewById(R.id.bt_add);
		bt_add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Device device = new Device();
				device.setName(et_dname.getText().toString().trim());
				device.setDevice_id(et_did.getText().toString().trim());
				new Thread(new AddRunnable(device)).start();
			}
		});
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				if(msg.what==0){
					pb_refresh.setVisibility(ProgressBar.GONE);
					lv_device.setAdapter(new MyDeviceAdapter(deviceFragment.getActivity()));
				}
				else if(msg.what==1){
					Toast.makeText(deviceFragment.getActivity(), "更新完成", Toast.LENGTH_SHORT).show();
				}
				else if(msg.what==2){
					Toast.makeText(deviceFragment.getActivity(), "删除完成", Toast.LENGTH_SHORT).show();
				}
				else if(msg.what==3){
					Toast.makeText(deviceFragment.getActivity(), "添加完成", Toast.LENGTH_SHORT).show();
				}
				
			}
			
		};
		new Thread(new ProgressRunnable()).start();
		return rootView;
	}
	
	public class MyDeviceAdapter extends BaseAdapter {  
        private LayoutInflater mInflater;// 动态布局映射  
  
        private HttpAPI httpAPI = HttpAPI.getInstance();
        
        public MyDeviceAdapter(Context context) {  
            this.mInflater = LayoutInflater.from(context);  
        }  
  
        // 决定ListView有几行可见  
        @Override  
        public int getCount() {  
            return httpAPI.getDeviceList().size();// ListView的条目数  
        }  
  
        @Override  
        public Object getItem(int arg0) {  
            return null;  
        }  
  
        @Override  
        public long getItemId(int arg0) {  
            return 0;  
        }  
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {
        	
        	final int sPosition = position;
            convertView = mInflater.inflate(R.layout.device_item, null);//根据布局文件实例化view  
            ImageView img = (ImageView) convertView.findViewById(R.id.iv_device);
            img.setBackgroundResource(R.drawable.device);
            
            TextView dName = (TextView) convertView.findViewById(R.id.tv_dname);
            dName.setText(httpAPI.getDeviceList().get(position).getName());
            //dName.setText(mData.get(position).get("d_name").toString());
            
            TextView dId = (TextView) convertView.findViewById(R.id.tv_did);
            dId.setText(httpAPI.getDeviceList().get(position).getDevice_id());
            //dId.setText(mData.get(position).get("d_did").toString());
            
            Button edit = (Button) convertView.findViewById(R.id.bt_editD);
            Button del = (Button) convertView.findViewById(R.id.bt_delD);
            
            edit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					DialogFragment fragment = new DeviceUpdateFragment();
					fragment.setTargetFragment(deviceFragment, sPosition);
					
					fragment.show(getFragmentManager(), "DeviceUpdateFragment");

					//Toast.makeText(deviceFragment.getActivity(), "click "+sPosition, Toast.LENGTH_SHORT).show();;
				}
			});
            
            del.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new Thread(new DeleteRunnable(httpAPI.getDeviceList().get(sPosition).getDevice_id())).start();
					//Toast.makeText(deviceFragment.getActivity(), "click "+sPosition, Toast.LENGTH_SHORT).show();;
				}
			});
            return convertView;  
        }  
        
    }  
	
    
    class ProgressRunnable implements Runnable{

		@Override
		public void run() {
			HttpAPI httpAPI = HttpAPI.getInstance();
			httpAPI.GetAllDevices();
			Message msg = Message.obtain();
			msg.what =0;
			mHandler.sendMessage(msg);
		}
    	
    }
    
    class UpdateRunnable implements Runnable{
    	
    	private String did;
    	private String name;
    	UpdateRunnable(String did,String name){
    		this.did = did;
    		this.name = name;
    	}
		@Override
		public void run() {
			HttpAPI httpAPI = HttpAPI.getInstance();
			httpAPI.UpdateDevice(did, name);
			mHandler.sendEmptyMessage(1);
		}
    	
    }
    
    class DeleteRunnable implements Runnable{
    	
    	private String did;
    	DeleteRunnable(String did){
    		this.did = did;
    	}
		@Override
		public void run() {
			HttpAPI httpAPI = HttpAPI.getInstance();
			httpAPI.DeleteDevice(did);
			mHandler.sendEmptyMessage(2);
		}
    	
    }
    
    class AddRunnable implements Runnable{

    	
    	private Device device;
    	AddRunnable(Device device){
    		this.device = device;
    	}
		@Override
		public void run() {
			HttpAPI httpAPI = HttpAPI.getInstance();
			httpAPI.AddDevice(device);
			mHandler.sendEmptyMessage(3);
		}
    	
    }
}
