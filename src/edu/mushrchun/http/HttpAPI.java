package edu.mushrchun.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.mushrchun.model.Device;
import edu.mushrchun.model.HistoricCurrency;
import edu.mushrchun.model.RealtimeCurrency;
import edu.mushrchun.model.Token;
import edu.mushrchun.model.TrendCurrency;
import edu.mushrchun.model.User;




/**
 * @author MushrChun
 *
 */
public class HttpAPI extends BaseAPI {

	private HttpAPI() {
	};

	private static HttpAPI userAPI = new HttpAPI();

	public static HttpAPI getInstance() {
		return userAPI;
	}

	private String respString;

	public String getRespString() {
		return respString;
	}

	public void setRespString(String respString) {
		this.respString = respString;
	}
	
	private String url;
		
	private Token token;
	
	private ArrayList<Device> deviceList;
	
	private String span;

	public String getSpan() {
		return span;
	}

	public ArrayList<Device> getDeviceList() {
		return deviceList;
	}
	
	private ArrayList<TrendCurrency> tcl;
	

	public ArrayList<TrendCurrency> getTcl() {
		return tcl;
	}

	public void setTcl(ArrayList<TrendCurrency> tcl) {
		this.tcl = tcl;
	}

	public void setDeviceList(ArrayList<Device> deviceList) {
		this.deviceList = deviceList;
	}

	public void setSpan(String span) {
		this.span = span;
	}

	//Generally completed
	public User Login(String loginname, String loginpass) {
		try {
			
			url = baseUrl + "/api/user/login.json";
			

			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", "application/json");
			
			JSONObject json = new JSONObject();
			json.put("username", loginname);
			json.put("password", loginpass);
			
			httpPost.setEntity(new StringEntity(json.toString()));

			HttpClient hc = new DefaultHttpClient();

			
			HttpResponse hr = hc.execute(httpPost);
			
			
			respString = hr.getStatusLine().toString();
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				
				
				String s = EntityUtils.toString(hr.getEntity());
				JSONObject jo = new JSONObject(s);
				
				token = new Token();
				token.setSessid(jo.getString("sessid"));
				token.setSession_name(jo.getString("session_name"));
				token.setToken(jo.getString("token"));
				
				JSONObject jsUser = jo.getJSONObject("user");
				
				User user = new User();
				user.setUid(jsUser.getString("uid"));
				user.setName(jsUser.getString("name"));
				user.setMail(jsUser.getString("mail"));
				user.setLoginname(loginname);
				user.setLoginpass(loginpass);
				return user;
			} 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	
	//Generally completed
	public boolean Logout() {
		try {
			url = baseUrl + "/api/user/logout.json";
			HttpClient hc = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("X-CSRF-Token", token.getToken());
			httpPost.addHeader("Cookie", token.getSession_name()+"="+token.getSessid());
			
			HttpResponse hr = hc.execute(httpPost);
			
			respString = hr.getStatusLine()+"";
			
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return true;
			} else {
				return false;

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

	}

	//Generally completed
	public boolean Register(User user) {
		try {
			String url = baseUrl + "/api/user/register.json";
			HttpClient hc = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", "application/json");
			JSONObject json = new JSONObject();
			json.put("name", user.getLoginname());
			json.put("pass", user.getLoginpass());
			json.put("mail", user.getMail());
			httpPost.setEntity(new StringEntity(json.toString()));

			HttpResponse hr = hc.execute(httpPost);
			respString = hr.getStatusLine() + hr.getEntity().toString();
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				String s = EntityUtils.toString(hr.getEntity());
				JSONObject jo = new JSONObject(s);
				if (jo.getString("uid") != null) {
					user.setUid(jo.getString("uid"));
					return true;
				}

				return false;
			} 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return false;
	}

	// TODO Didn't complete because service lack relative module
	public boolean ResetPassword(User user) {
		try {
			url = baseUrl + "/api/user/request_new_password.json";
			HttpClient hc = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", "application/json");
			JSONObject json = new JSONObject();
			json.put("name", user.getLoginname());
			httpPost.setEntity(new StringEntity(json.toString()));

			HttpResponse hr = hc.execute(httpPost);
			respString = hr.getStatusLine() + hr.getEntity().toString();
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				return true;

			} 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return false;
	}

	//Generally completed 
	//Each time run this function refresh the deviceList
	public boolean GetAllDevices() {
		try {
			url = baseUrl + "/api/device.json";
			HttpClient hc = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("X-CSRF-Token", token.getToken());
			httpGet.addHeader("Cookie", token.getSession_name()+"="+token.getSessid());
			
			
			HttpResponse hr = hc.execute(httpGet);
			respString = hr.getStatusLine().toString();
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				deviceList = new ArrayList<Device>();
				String s = EntityUtils.toString(hr.getEntity());
				JSONArray ja=  new JSONArray(s);
				for(int i=0;i<ja.length();i++){
					Device device = new Device();
					JSONObject jo = ja.getJSONObject(i);
					device.setDevice_id(jo.getString("device_id"));
					device.setDid(jo.getString("did"));
					device.setName(jo.getString("name"));
					device.setType(jo.getString("type"));
					device.setUid(jo.getString("user_id"));
					deviceList.add(device);
				}
				return true;

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return false;
	}

	// TODO haven't completed
//	public boolean GetDevice(String did) {
//		try {
//			url = baseUrl + "/api/device/" + did + ".json";
//			HttpClient hc = new DefaultHttpClient();
//			HttpGet httpGet = new HttpGet(url);
//
//			HttpResponse hr = hc.execute(httpGet);
//			respString = hr.getStatusLine().toString();
//			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//
//				return true;
//
//			} 
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return false;
//	}

	// TODO require test
	public boolean UpdateDevice(String did,String name) {
		try {
			String url = baseUrl + "/api/device/" + did + ".json";

			HttpPut httpPut = new HttpPut(url);
			httpPut.addHeader("Content-Type", "application/json");
			httpPut.addHeader("X-CSRF-Token", token.getToken());
			httpPut.addHeader("Cookie", token.getSession_name()+"="+token.getSessid());
			JSONObject json = new JSONObject();
			json.put("name", name);
			httpPut.setEntity(new StringEntity(json.toString()));
			HttpClient hc = new DefaultHttpClient();
			HttpResponse hr = hc.execute(httpPut);
			respString = hr.getStatusLine().toString();
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				return true;

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return false;
	}


	public boolean AddDevice(Device device) {
		try {
			String url = baseUrl + "/api/device";
			HttpClient hc = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", "application/json");
			httpPost.addHeader("X-CSRF-Token", token.getToken());
			httpPost.addHeader("Cookie", token.getSession_name()+"="+token.getSessid());
			JSONObject json = new JSONObject();
			json.put("device_id", device.getDevice_id());
			json.put("name", device.getName());
			httpPost.setEntity(new StringEntity(json.toString()));
			HttpResponse hr = hc.execute(httpPost);
			respString = hr.getStatusLine().toString();
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				return true;

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return false;
	}


	public boolean DeleteDevice(String did) {
		try {
			String url = baseUrl + "/api/device/" + did + ".json";
			HttpClient hc = new DefaultHttpClient();
			HttpDelete httpDelete = new HttpDelete(url);
			httpDelete.addHeader("Content-Type", "application/json");
			httpDelete.addHeader("X-CSRF-Token", token.getToken());
			httpDelete.addHeader("Cookie", token.getSession_name()+"="+token.getSessid());
			HttpResponse hr = hc.execute(httpDelete);
			respString = hr.getStatusLine().toString();
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				return true;

			} 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	// Generally completed
	public RealtimeCurrency GetRealtime() {
		try {
			
			url = baseUrl + "/power/realtime";
			
			HttpClient hc = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("X-CSRF-Token", token.getToken());
			httpGet.addHeader("Cookie", token.getSession_name()+"="+token.getSessid());
			
			HttpResponse hr = hc.execute(httpGet);
			respString = hr.getStatusLine().toString();
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String s = EntityUtils.toString(hr.getEntity());
				
				JSONObject jo = new JSONObject(s);
				
				RealtimeCurrency realtimeCurrency = new RealtimeCurrency();
				realtimeCurrency.setReal(jo.getString("real"));
				realtimeCurrency.setAvg(jo.getString("avg"));
				realtimeCurrency.setLow(jo.getString("low"));
				realtimeCurrency.setHigh(jo.getString("high"));
				
				return realtimeCurrency;

			} 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	// Generally completed
	public boolean GetTrends(Date startDate, Date endDate) {
		try {
			String str1 = "b="+startDate.getTime();
			String str2 = "e="+endDate.getTime();
			String url = baseUrl + "/power/trends/data?"+str1+"&"+str2;
			HttpClient hc = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("X-CSRF-Token", token.getToken());
			httpGet.addHeader("Cookie", token.getSession_name()+"="+token.getSessid());
			HttpResponse hr = hc.execute(httpGet);
			respString = hr.getStatusLine().toString();
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String s = EntityUtils.toString(hr.getEntity());
				JSONObject jo = new JSONObject(s);
				JSONObject jo_range = jo.getJSONObject("range");
				span = jo_range.getString("span");
				
				ArrayList<TrendCurrency> tl = new ArrayList<TrendCurrency>();
				
				JSONArray jo_series = jo.getJSONArray("series");
				for(int i=0;i<jo_series.length();i++){
					JSONObject j =jo_series.getJSONObject(i);
					TrendCurrency tc = new TrendCurrency();
					tc.setD_name(j.getString("name"));
					ArrayList<Double> data = new ArrayList<Double>();
					ArrayList<Long> labels = new ArrayList<Long>();
					JSONArray a =j.getJSONArray("data");
					for(int k=0;k<a.length();k++){
							JSONArray a2 = a.getJSONArray(k);
							
							labels.add(Long.valueOf(a2.getLong(0)));
							data.add(Double.valueOf(a2.getDouble(1)));
							
						}
					
					
					tc.setData(data);
					tc.setLabels(labels);
					tl.add(tc);
				}
				tcl = tl;
				return true;

			} 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return false;
	}

	//Generally completed
	public ArrayList<HistoricCurrency> GetHistory() {
		ArrayList<HistoricCurrency> hisCurrentList=null;
		try {
			url = baseUrl + "/power/history";
			HttpClient hc = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("X-CSRF-Token", token.getToken());
			httpGet.addHeader("Cookie", token.getSession_name()+"="+token.getSessid());
			HttpResponse hr = hc.execute(httpGet);
			respString = hr.getStatusLine().toString();
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				hisCurrentList= new ArrayList<HistoricCurrency>();
				
				String s = EntityUtils.toString(hr.getEntity());
				 
				JSONObject jo = new JSONObject(s);
				JSONObject jo_usage = jo.getJSONObject("usage");
				
				JSONObject u_today = jo_usage.getJSONObject("today");
				JSONObject u_7day = jo_usage.getJSONObject("7day");
				JSONObject u_30day = jo_usage.getJSONObject("30day");
				
				GetAllDevices();
				
				for(int i =0;i<deviceList.size();i++){
					HistoricCurrency hisCurrency = new HistoricCurrency();
					String device_id = deviceList.get(i).getDevice_id();
					hisCurrency.setDeviceID(device_id);
					hisCurrency.setToday(u_today.getString(device_id));
					hisCurrency.setIn7days(u_7day.getString(device_id));
					hisCurrency.setIn30days(u_30day.getString(device_id));
					hisCurrentList.add(hisCurrency);
				}
				

			} 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hisCurrentList;

		
	}

}
