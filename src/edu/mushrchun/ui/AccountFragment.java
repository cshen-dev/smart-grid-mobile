package edu.mushrchun.ui;

import edu.mushrchun.item.R;
import edu.mushrchun.model.User;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountFragment extends Fragment {

	private User user;
	private TextView tv_loginname2;
	private TextView tv_name2;
	private TextView tv_mail2;
	
	public AccountFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		user = (User)getArguments().getSerializable("user");
		
		View rootView = inflater.inflate(R.layout.fragment_account, container,
				false);
		tv_loginname2 = (TextView)rootView.findViewById(R.id.tv_loginname2);
		tv_name2 = (TextView)rootView.findViewById(R.id.tv_name2);
		tv_mail2 = (TextView)rootView.findViewById(R.id.tv_mail2);
		tv_loginname2.setText(user.getLoginname());
		tv_name2.setText(user.getName());
		tv_mail2.setText(user.getMail());
		getActivity().setTitle("用户账户");
		return rootView;
	}

	

}
