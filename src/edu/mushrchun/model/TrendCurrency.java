package edu.mushrchun.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class TrendCurrency implements Parcelable{
	public String getD_name() {
		return d_name;
	}
	public void setD_name(String d_name) {
		this.d_name = d_name;
	}
	public String getSpan() {
		return span;
	}
	public void setSpan(String span) {
		this.span = span;
	}
	public ArrayList<Double> getData() {
		return data;
	}
	public void setData(ArrayList<Double> data) {
		this.data = data;
	}
	public ArrayList<Long> getLabels() {
		return labels;
	}
	public void setLabels(ArrayList<Long> labels) {
		this.labels = labels;
	}
	private String d_name;
	private String span;
	private ArrayList<Double> data;
	private ArrayList<Long> labels;
	
	public TrendCurrency(){
		
	}
	
	protected TrendCurrency(Parcel in) {
        d_name = in.readString();
        span = in.readString();
        if (in.readByte() == 0x01) {
            data = new ArrayList<Double>();
            in.readList(data, Double.class.getClassLoader());
        } else {
            data = null;
        }
        if (in.readByte() == 0x01) {
            labels = new ArrayList<Long>();
            in.readList(labels, Long.class.getClassLoader());
        } else {
            labels = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(d_name);
        dest.writeString(span);
        if (data == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(data);
        }
        if (labels == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(labels);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TrendCurrency> CREATOR = new Parcelable.Creator<TrendCurrency>() {
        @Override
        public TrendCurrency createFromParcel(Parcel in) {
            return new TrendCurrency(in);
        }

        @Override
        public TrendCurrency[] newArray(int size) {
            return new TrendCurrency[size];
        }
    };
}
