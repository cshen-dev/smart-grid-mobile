package edu.mushrchun.model;

import android.os.Parcel;
import android.os.Parcelable;

public class HistoricCurrency implements Parcelable{
	private String deviceID;
	private String today;
	private String in7days;
	private String in30days;
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getToday() {
		return today;
	}
	public void setToday(String today) {
		this.today = today;
	}
	public String getIn7days() {
		return in7days;
	}
	public void setIn7days(String in7days) {
		this.in7days = in7days;
	}
	public String getIn30days() {
		return in30days;
	}
	public void setIn30days(String in30days) {
		this.in30days = in30days;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(deviceID);
		dest.writeString(today);
		dest.writeString(in7days);
		dest.writeString(in30days);
		
	}
	
	public static final Parcelable.Creator<HistoricCurrency> CREATOR = new Creator<HistoricCurrency>() {
		 
        @Override
        public HistoricCurrency[] newArray(int size) {
            return null;
        }
 
        @Override
        public HistoricCurrency createFromParcel(Parcel source) {
        	HistoricCurrency result = new HistoricCurrency();
            result.deviceID = source.readString();
            result.today = source.readString();
            result.in7days = source.readString();
            result.in30days = source.readString();
            return result;
        }
    };
}
