package edu.mushrchun.ui;

import edu.mushrchun.http.HttpAPI;
import edu.mushrchun.item.R;
import edu.mushrchun.model.User;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener{

	private Button regButton;
	private EditText et_loginpass;
	private EditText et_loginname;
	private EditText et_email;
	private ProgressBar pb_refresh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView(){
		setContentView(R.layout.activity_register);
		regButton = (Button) findViewById(R.id.bt_register);
		et_loginpass = (EditText) findViewById(R.id.et_loginpass);
		et_loginname = (EditText) findViewById(R.id.et_loginname);
		et_email = (EditText) findViewById(R.id.et_email);
		pb_refresh = (ProgressBar) findViewById(R.id.pb_refresh);
		pb_refresh.setVisibility(pb_refresh.GONE);
		regButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bt_register:
			String loginpass = et_loginpass.getText().toString();
			String loginname = et_loginname.getText().toString();
			String email = et_email.getText().toString();
			regButton.setClickable(false);
			regButton.setText("正在注册");
			pb_refresh.setVisibility(pb_refresh.VISIBLE);
			RegisterTask rt = new RegisterTask();
			rt.execute(loginname,loginpass,email);
			break;
		}
	}
	
	private class RegisterTask extends AsyncTask<String, Integer, String>{

		
		@Override
		protected String doInBackground(String... params) {
			
			HttpAPI userAPI = HttpAPI.getInstance();
			User u = new User();
			u.setLoginname(params[0]);
			u.setLoginpass(params[1]);
			u.setMail(params[2]);
			boolean flag = userAPI.Register(u);
			if(flag){
				return "Fail";
			}
			return "Success";
		}

		@Override
		protected void onPostExecute(String result) {
//			Toast.makeText(getApplicationContext(), user.toString(),Toast.LENGTH_SHORT).show();
			pb_refresh.setVisibility(pb_refresh.GONE);
			regButton.setText("注册");
			if(result=="Success"){
				Toast.makeText(getApplicationContext(), "注册成功",Toast.LENGTH_SHORT).show();
				onBackPressed();
			}
			else{
				Toast.makeText(getApplicationContext(), "注册失败",Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}

}
