package com.mi.game.module.welfare.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mi.core.template.BaseTemplate;

public class WelfareBaseData extends BaseTemplate {

	public List<Integer> getKeys(Map<Integer, Integer> map) {
		List<Integer> list = new ArrayList<Integer>();
		Set<Entry<Integer, Integer>> set = map.entrySet();
		for (Entry<Integer, Integer> entry : set) {
			list.add(entry.getKey());
		}
		return list;
	}

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
