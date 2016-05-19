package com.mi.game.module.event.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.util.Utilities;

/**
 * 兑换
 * 
 * @author Administrator
 *
 */
public class EventExchangeEntity extends BaseEntity {

	private static final long serialVersionUID = -5299028332126769111L;
	@Indexed
	private String playerID;
	private Map<Integer, Exchange> exchanges = new HashMap<Integer, Exchange>();
	// 开始时间
	private long startTime;
	// 结束时间
	private long endTime;
	// 刷新时间
	private String dateTime;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public Map<Integer, Exchange> getExchanges() {
		return exchanges;
	}

	public void setExchanges(Map<Integer, Exchange> exchanges) {
		this.exchanges = exchanges;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public void addExchange(Integer pid, Exchange exchange) {
		exchanges.put(pid, exchange);
	}

	/**
	 * 是否已刷新
	 * 
	 * @return
	 */
	public boolean isRefresh() {
		String nowTime = Utilities.getDateTime();
		if (dateTime == null || dateTime.isEmpty()) {
			dateTime = nowTime;
			return false;
		}
		if (!nowTime.equals(dateTime)) {
			dateTime = nowTime;
			return false;
		}
		return true;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("exchanges", map2List());
		result.put("startTime", startTime);
		result.put("endTime", endTime);
		return result;
	}

	private List<Exchange> map2List() {
		List<Exchange> list = new ArrayList<Exchange>();
		Set<Entry<Integer, Exchange>> set = exchanges.entrySet();
		for (Entry<Integer, Exchange> entry : set) {
			list.add(entry.getValue());
		}
		return list;
	}

	@Override
	public Object getKey() {
		return playerID;
	}

	@Override
	public String getKeyName() {
		return playerID;
	}

	@Override
	public void setKey(Object key) {
		this.playerID = key.toString();
	}

}
