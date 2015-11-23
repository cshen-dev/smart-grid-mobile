package edu.mushrchun.ui;


import edu.mushrchun.http.HttpAPI;
import edu.mushrchun.model.User;
import edu.mushrchun.item.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button loginButton;
	private EditText et_loginpass;
	private EditText et_loginname;
	private CheckBox cb_auto;
	private ProgressBar pb_refresh;
	private TextView tv_newUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
		
	}
	
	private void initView(){
		setContentView(R.layout.activity_login);
		loginButton = (Button) findViewById(R.id.bt_login);
		et_loginpass = (EditText) findViewById(R.id.et_loginpass);
		et_loginname = (EditText) findViewById(R.id.et_loginname);
		cb_auto = (CheckBox) findViewById(R.id.cb_auto);
		pb_refresh = (ProgressBar) findViewById(R.id.pb_refresh);
		tv_newUser = (TextView) findViewById(R.id.textView2);
		tv_newUser.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
		});
		
		pb_refresh.setVisibility(pb_refresh.GONE);
		
		loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = et_loginname.getText().toString().trim();
				String password = et_loginpass.getText().toString().trim();
				loginButton.setClickable(false);
				loginButton.setText("正在登录");
				pb_refresh.setVisibility(pb_refresh.VISIBLE);
				LoginTask myTask = new LoginTask();
				myTask.execute(name,password);
				
			}
		});
		
		
		SharedPreferences sp_setting = getSharedPreferences("setting",MODE_PRIVATE);
		if(sp_setting.getBoolean("auto_login",false)){
			cb_auto.setChecked(true);
			SharedPreferences sp_user = getSharedPreferences("user", MODE_PRIVATE);
			et_loginname.setText(sp_user.getString("loginname", ""));
			et_loginpass.setText(sp_user.getString("loginpass", ""));
		}
	}
	
	private class LoginTask extends AsyncTask<String, Integer, String>{

		private User user;
		@Override
		protected String doInBackground(String... params) {
			
			HttpAPI userAPI = HttpAPI.getInstance();
			user = userAPI.Login(params[0], params[1]);
			if(user==null){
				return "Fail";
			}
			return "Success";
		}

		@Override
		protected void onPostExecute(String result) {
//			Toast.makeText(getApplicationContext(), user.toString(),Toast.LENGTH_SHORT).show();
			if(result=="Success"){
				SharedPreferences sp_user = getSharedPreferences("user",MODE_PRIVATE);
				SharedPreferences.Editor editor = sp_user.edit();
				editor.putString("loginname", user.getLoginname());
				editor.putString("loginpass", user.getLoginpass());
				editor.putString("mail", user.getMail());
				editor.putString("name", user.getName());
				editor.commit();
				if(cb_auto.isChecked()){
					
					SharedPreferences sp_setting = getSharedPreferences("setting",MODE_PRIVATE);
					editor = sp_setting.edit();
					editor.putBoolean("auto_login", true);
					editor.commit();
				}
				else{
					SharedPreferences sp_setting = getSharedPreferences("setting",MODE_PRIVATE);
					editor = sp_setting.edit();
					editor.putBoolean("auto_login", false);
					editor.commit();
				}
				Toast.makeText(getApplicationContext(), "登录成功",Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
			}
			else{
				Toast.makeText(getApplicationContext(), "登录失败",Toast.LENGTH_SHORT).show();
			}
			pb_refresh.setVisibility(pb_refresh.GONE);
			loginButton.setClickable(true);
			loginButton.setText("登录");
		}
		
	}

}
