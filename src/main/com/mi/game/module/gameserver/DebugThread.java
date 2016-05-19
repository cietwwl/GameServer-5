package com.mi.game.module.gameserver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

class DebugThread extends Thread {

	private int num;

	public DebugThread(int num) {
		super();
		this.num = num;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < num; i++) {
				String urlStr = "http://127.0.0.1:8001/browserGame/json.do?type=9000&playerID=3cddf350&storeType=item&itemID=10177&itemNum=20&uniqueKey=123456";
				URL url;
				String result = "";
				url = new URL(urlStr);
				URLConnection conn = url.openConnection();
				// 超时设置500毫秒
				conn.setConnectTimeout(500);
				conn.setReadTimeout(5000);
				conn.connect();
				InputStream is = conn.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
				String line;
				while ((line = br.readLine()) != null) {
					result += line;
				}
				JSONObject json = JSON.parseObject(result);
				int code = json.getIntValue("code");
				if (code == 0) {
					System.out.println(getName() + "---->" + i + "--->" + code);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}