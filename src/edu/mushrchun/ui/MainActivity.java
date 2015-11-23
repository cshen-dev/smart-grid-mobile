package edu.mushrchun.ui;


import edu.mushrchun.model.User;
import edu.mushrchun.http.HttpAPI;
import edu.mushrchun.item.R;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private Toolbar toolbar;
	private String[] navDrawerItems;
	private DrawerLayout drawerLayout;
	private ListView slidingList;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		SharedPreferences sp_user = getSharedPreferences("user", MODE_PRIVATE);
		user = new User();
		user.setName(sp_user.getString("name", ""));
		user.setMail(sp_user.getString("mail", ""));
		user.setLoginname(sp_user.getString("loginname", ""));
//		Toast.makeText(getApplicationContext(), user.getName()+user.getMail()+user.getLoginname(),Toast.LENGTH_SHORT).show();
		if (savedInstanceState == null) {
		      selectItem(1);
		}

	}



	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		actionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			new LogoutTask().execute();
			super.onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initView() {
		setContentView(R.layout.activity_main);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		navDrawerItems = getResources().getStringArray(
				R.array.navigation_drawer_items);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		slidingList = (ListView) findViewById(R.id.left_drawer);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		slidingList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, navDrawerItems));
		slidingList.setOnItemClickListener(new ListView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectItem(position);
				
			}
			
		});
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				toolbar, R.string.app_name, R.string.app_name);
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		
	}

	private void selectItem(int position) {
		Fragment fragment=null;
		switch (position) {
		case 0:
			fragment = new AccountFragment();break;
		case 1:
			fragment = new RealtimeCurrencyFragment();break;
		case 2:
			fragment = new SumCurrencyFragment();break;
		case 3:
			fragment = new HistoryCurrencyFragment();break;
		case 4:
			fragment = new DeviceFragment();break;
		case 5:
			fragment = new SettingFragment();break;
		default:
			break;	

		}
//		Toast.makeText(getApplicationContext(), "select:"+position,Toast.LENGTH_SHORT).show();
		Bundle args = new Bundle();
		args.putSerializable("user", user);
		fragment.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();
		
		

		
		slidingList.setItemChecked(position, true);
		setTitle(navDrawerItems[position]);
		drawerLayout.closeDrawer(slidingList);
	}
	
	  @Override
	  public void setTitle(CharSequence title) {
	    getSupportActionBar().setTitle(title);
	  }
	  
	  private class LogoutTask extends AsyncTask<String, Integer, String>{

			@Override
			protected String doInBackground(String... params) {
				HttpAPI userAPI = HttpAPI.getInstance();
				
				boolean flag = userAPI.Logout();
				if(flag){
					return "注销成功";
				}
				return "注销失败";
			}

			@Override
			protected void onPostExecute(String result) {
				Toast.makeText(getApplicationContext(), result,Toast.LENGTH_SHORT).show();
				
			}
			
		}

	@Override
	public void onBackPressed() {
		// 关闭应用程序
		finish();
		// 返回桌面操作
		 Intent home = new Intent(Intent.ACTION_MAIN);
		 home.addCategory(Intent.CATEGORY_HOME);
		 home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 startActivity(home);
	}
	  
	  
}
