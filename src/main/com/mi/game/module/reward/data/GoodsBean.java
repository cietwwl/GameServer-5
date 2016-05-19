package com.mi.game.module.reward.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.IResponseMap;

/**
 * @author 刘凯旋	
 * 物品信息
 * 2014年6月12日 下午2:43:16
 */
public class GoodsBean implements IResponseMap{
	private int pid; // 物品id
	private int num; // 数量
	private Object param;
	public GoodsBean(){
		
	}
	
	public GoodsBean(int pid,int num){
		this.pid = pid;
		this.num = num;
	}
	
	
	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("pid", pid);
		data.put("num", num);
		return data;
	}


	@Override
	public Map<String, Object> responseMap(int arg0) {
		return this.responseMap();
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

	/**
	 * @return the pid
	 */
	public int getPid() {
		return pid;
	}
	/**
	 * @param pid the pid to set
	 */
	public void setPid(int pid) {
		this.pid = pid;
	}
	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}
	/**
	 * @param num the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return responseMap().toString();
	}
	
	
}
