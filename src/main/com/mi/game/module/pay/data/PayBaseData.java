package com.mi.game.module.pay.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.template.BaseTemplate;

public class PayBaseData extends BaseTemplate {

	/**
	 * 解析多重含义的xml节点内容
	 * 
	 * @param str
	 * @return
	 */
	public Map<Integer, Integer> setMultiple(String str) {
		Map<Integer, Integer> multiple = null;
		if (str != null && !str.isEmpty()) {
			multiple = new HashMap<Integer, Integer>();
			String[] temps = str.split(",");
			for (String temp : temps) {
				String[] costArr = temp.split("=");
				if (costArr != null) {
					multiple.put(Integer.parseInt(costArr[0]), Integer.parseInt(costArr[1]));
				}
			}
		}
		return multiple;
	}

}
