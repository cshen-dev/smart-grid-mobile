package edu.mushrchun.ui;

import edu.mushrchun.http.HttpAPI;
import edu.mushrchun.item.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DeviceUpdateFragment extends DialogFragment{

//	public interface DeviceUpdateListener{
//		public void onDialogPositiveClick(DialogFragment dialog);
//		public void onDialogNegativeClick(DialogFragment dialog);
//	}
//	
//	DeviceUpdateListener mListener;
//	
//	// Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        // Verify that the host activity implements the callback interface
//        try {
//            // Instantiate the NoticeDialogListener so we can send events to the host
//            mListener = (DeviceUpdateListener) activity;
//        } catch (ClassCastException e) {
//            // The activity doesn't implement the interface, throw exception
//            throw new ClassCastException(activity.toString()
//                    + " must implement NoticeDialogListener");
//        }
//    }
	
	private EditText device_name;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final DeviceUpdateFragment mDeviceUpdateFragment = this;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.device_update_dialog, null);
		device_name = (EditText) view.findViewById(R.id.et_device_name);
		
		builder.setView(view);
		
		builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Toast.makeText(mDeviceUpdateFragment.getActivity(), "更新中", Toast.LENGTH_SHORT).show();
				String nameStr =  device_name.getText().toString().trim();
				Intent intent = new Intent();
				intent.putExtra("device_new_name",nameStr);
				int requestCode = getTargetRequestCode();
				getTargetFragment().onActivityResult(requestCode, 10, intent);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Toast.makeText(mDeviceUpdateFragment.getActivity(), "取消", Toast.LENGTH_SHORT).show();
			}
		});
		
		return builder.create();
	}
	
	
}
