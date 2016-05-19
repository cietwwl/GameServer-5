package com.mi.game.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TestUserSendRewardUtil {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private static Map<String, String> testMap = new HashMap<String, String>();
	// 是否开启发送测试奖励
	public static boolean isSendReward = true;
	
	public static void main(String[] args) {
		URL url1 = TestUserSendRewardUtil.class.getResource("/props/pay-1-ios.properties");
		URL url2 = TestUserSendRewardUtil.class.getResource("/props/pay-2-ios.properties");
		Map<String, String> url1Map = new HashMap<String, String>();
		try{
			String wordsPath = url1.getPath();
			File file = new File(wordsPath);
			InputStreamReader in = new InputStreamReader(new FileInputStream(file));
			BufferedReader br = new BufferedReader(in);
			String line;
			while ((line = br.readLine()) != null) {
				String[] temp = line.split("#");
				String key = temp[0];
				String value = temp[1];
				url1Map.put(key, value);
			}
			br.close();
			
			String path2  = url2.getPath();
			File file2 = new File(path2);
			InputStreamReader in2 = new InputStreamReader(new FileInputStream(file2));
			BufferedReader br2 = new BufferedReader(in2);
			String line2;
			while ((line2 = br2.readLine()) != null) {
				String[] temp = line2.split("#");
				String key = temp[0];
				String value = temp[1];
				if(url1Map.containsKey(key)){
					System.out.println("相同key,合并" + key);
					url1Map.put(key, (Integer.parseInt(value) + Integer.parseInt(url1Map.get(key))+""));
				}else{
					url1Map.put(key,value);
				}			
			}
			br2.close();
			FileOutputStream out = new FileOutputStream(new File("pay.properties"));
			Set<Entry<String,String>> entrySet  = url1Map.entrySet();
			for(Entry<String, String> entry : entrySet){
				String key = entry.getKey();
				String value = entry.getValue();
				String info = key+"#" + value;
				out.write((info + "\r\n").getBytes());
			}
			out.close();
		}catch (Exception e) {
			System.out.println("用户支付合并错误 " + e);
		}
	
	}
	
	public static void init() {
		try {
			URL url = TestUserSendRewardUtil.class.getResource("/pay.properties");
			if (url != null) {
				String wordsPath = url.getPath();
				System.out.println("读取测试用户uid列表");
				File file = new File(wordsPath);
				InputStreamReader in = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(in);
				String line;
				while ((line = br.readLine()) != null) {
					String[] temp = line.split("#");
					String key = temp[0];
					String value = temp[1];
					testMap.put(key, value);
				}
				br.close();
				// 存在测试用户列表,开启奖励发放
				isSendReward = true;
				System.out.println("读取测试用户uid列表完成! 个数:" + testMap.size());
			}
		} catch (Exception e) {
			System.out.println("测试用户uid列表! " + e);
		}
	}

	/**
	 * 检查是否测试用户
	 * 
	 * @param message
	 * @return
	 */
	public static boolean checkTestUser(String uid) {
		if (testMap.containsKey(uid)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取测试用户充值数量
	 * 
	 * @param uid
	 * @return
	 */
	public static int getTestUserPayValue(String uid) {
		if (testMap.containsKey(uid)) {
			String value = testMap.get(uid);
			if (StringUtils.isNotEmpty(value)) {
				return Integer.parseInt(value);
			}
		}
		return 0;
	}

}
