package com.mi;

import java.util.HashMap;
import java.util.Map;

public class Test {
	public static Map<String,String> queryStringToMap(String queryString){
		Map<String,String> data = new HashMap<String,String>();
		
		String[] items = queryString.split("&");
		
		if(items!=null && items.length>0){
			for(String item : items){
				String[] tmp = item.split("=");
				if(tmp!=null && tmp.length == 2){
					data.put(tmp[0], tmp[1]);
				}
			}
		}
		
		return data;
	}
	
	public static void main(String[] args) {
		String ss = "uid=34&name=liq&age=23";
		
		Map<String,String> data = queryStringToMap(ss);
		System.out.println(data);
	}
}
