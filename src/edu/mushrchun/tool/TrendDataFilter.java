package edu.mushrchun.tool;

import java.util.ArrayList;

import edu.mushrchun.model.TrendCurrency;

public class TrendDataFilter {
	public static ArrayList<TrendCurrency> Filter(ArrayList<TrendCurrency> tcl){
		ArrayList<TrendCurrency> ntcl = new ArrayList<TrendCurrency>();
		for(TrendCurrency tc : tcl){
			TrendCurrency ntc = new TrendCurrency();
			ArrayList<Double> dataList = tc.getData();
			
			//抽样数据
			if(dataList.size()==0){
				
			}
			else if(dataList.size()<10){
				ntc.setData(tc.getData());
				ntc.setLabels(tc.getLabels());
			}
			else{
				ArrayList<Double> nDataList = new ArrayList<Double>();
				ArrayList<Long> nLabelList = new ArrayList<Long>();
				for(int i=0;i<dataList.size();){
					nDataList.add(tc.getData().get(i));
					nLabelList.add(tc.getLabels().get(i));
					i+=dataList.size()/10;
				}
				ntc.setData(nDataList);
				ntc.setLabels(nLabelList);
			}
			
			
			ntc.setD_name(tc.getD_name());
			ntc.setSpan(tc.getSpan());
			ntcl.add(ntc);
		}
		return ntcl;
	}
}
