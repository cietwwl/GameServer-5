package com.mi.game.module.bag.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

@XmlTemplate(template = { "com/mi/template/PropsItemPrototype.xml" })
public class BagData extends BaseTemplate {
	private int type;
	private int price;
	private int sell;
	private Map<Integer, Integer> useFunc;
	private Map<Integer, Integer> delFunc;

	public Map<Integer, Integer> getDelFunc() {
		return delFunc;
	}

	public void setDelFunc(Map<Integer, Integer> delFunc) {
		this.delFunc = delFunc;
	}

	public Map<Integer, Integer> getUseFunc() {
		return useFunc;
	}

	public void setUseFunc(String useFunc) {
		if (useFunc != null && !useFunc.isEmpty()) {
			this.useFunc = new HashMap<Integer, Integer>();
			String[] tempArr = useFunc.split(",");
			if (tempArr.length > 1) {
				this.delFunc = new HashMap<>();
				String delTemp = tempArr[1];
				String[] delArr = delTemp.split("=");
				if (delArr != null) {
					this.delFunc.put(Integer.parseInt(delArr[0]), Integer.parseInt(delArr[1]));
				}
			}
			String temp = tempArr[0];
			String[] costArr = temp.split("=");
			if (costArr != null) {
				this.useFunc.put(Integer.parseInt(costArr[0]), Integer.parseInt(costArr[1]));
			}
		}
	}

	public int getSell() {
		return sell;
	}

	public void setSell(int sell) {
		this.sell = sell;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(String price) {
		if (price != null) {
			String[] strArr = price.split("=");
			this.price = Integer.parseInt(strArr[1]);
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
