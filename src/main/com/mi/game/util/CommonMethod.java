package com.mi.game.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.mi.core.cache.CacheFactory;
import com.mi.core.cache.ICache;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.IResponseMap;
import com.mi.core.pojo.BaseEntity;
import com.mi.core.protocol.BaseProtocol;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.reward.data.GoodsBean;

/**
 * 通用方法
 * 
 * @author 李强 <br/>
 *
 *         创建时间：2014-1-8 上午11:44:10
 */
public class CommonMethod {

	/**
	 * 将输出实体列表转换为输出map列表
	 * 
	 * @param entityList
	 * @return
	 */
	public static List<Map<String, Object>> getResponseListMap(List<? extends IResponseMap> responseList) {
		List<Map<String, Object>> response = null;

		if (responseList != null && !responseList.isEmpty()) {
			response = new ArrayList<Map<String, Object>>();

			for (IResponseMap entity : responseList) {
				response.add(entity.responseMap());
			}
		}

		return response;
	}

	/**
	 * 将输出实体列表转换为输出map列表
	 * 
	 * @param entityList
	 * @return
	 */
	public static List<Map<String, Object>> getResponseListMap(Collection<? extends IResponseMap> responseList) {
		List<Map<String, Object>> response = null;

		if (responseList != null && !responseList.isEmpty()) {
			response = new ArrayList<Map<String, Object>>();

			for (IResponseMap entity : responseList) {
				response.add(entity.responseMap());
			}
		}

		return response;
	}

	/**
	 * 将输出实体列表转换为输出map列表
	 * 
	 * @param type
	 * @param responseList
	 * @return
	 */
	public static List<Map<String, Object>> getResponseListMap(int type, List<? extends IResponseMap> responseList) {
		List<Map<String, Object>> response = null;

		if (responseList != null && !responseList.isEmpty()) {
			response = new ArrayList<Map<String, Object>>();

			for (IResponseMap entity : responseList) {
				response.add(entity.responseMap(type));
			}
		}

		return response;
	}

	/**
	 * 将输出实体列表转换为输出map列表
	 * 
	 * @param Map
	 *            <String,Object> responseMap
	 * **/
	public static Map<String, Object> getResponseMap(Map<String, ? extends IResponseMap> responseMap) {
		Map<String, Object> response = null;
		if (responseMap != null && !responseMap.isEmpty()) {
			response = new HashMap<String, Object>();
			for (Entry<String, ? extends IResponseMap> entry : responseMap.entrySet()) {
				IResponseMap value = entry.getValue();
				String key = entry.getKey();
				if (value != null)
					response.put(key, value.responseMap());
			}
		}
		return response;
	}

	/**
	 * 将物品添加到返回集合
	 * 
	 * @param pid
	 * @param num
	 * @param goodsMap
	 */
	public static void addToGoodsMap(int pid, int num, Map<String, GoodsBean> goodsMap) {
		if (goodsMap != null) {
			String key = pid + "";
			GoodsBean bean = goodsMap.get(key);
			if (bean != null) {
				bean.setNum(bean.getNum() + num);
			} else {
				bean = new GoodsBean(pid, num);
				goodsMap.put(key, bean);
			}
		}
	}

	/**
	 * 校验等级是否符合要求，[minLv,maxLv]
	 * 
	 * @param curLv
	 *            当前等级
	 * @param minLv
	 *            最小等级
	 * @param maxLv
	 *            最大等级
	 * @return
	 */
	public static boolean checkLevel(int curLv, int minLv, int maxLv) {

		if (minLv < 0 && maxLv < 0) { // 如果没有等级要求
			return true;
		} else if (maxLv < 0) { // 如果有最小等级要求
			if (curLv >= minLv) {
				return true;
			}
		} else if (minLv < 0) { // 如果有最大等级要求
			if (curLv <= maxLv) {
				return true;
			}
		} else {
			if (curLv >= minLv && curLv <= maxLv) { // 如果最小等级与最大等级都有要求
				return true;
			}
		}

		return false;
	}

	/**
	 * 获取两个数的百分比、千分比、万分比，分母必须大于等于分子
	 * 
	 * @param molecular
	 *            分子
	 * @param denominator
	 *            分母
	 * @param cardinal
	 *            比率基数 （100,10000,10000）
	 * @return
	 */
	public static int getExtremeRatio(long molecular, long denominator, int cardinal) {
		if (denominator == molecular) {
			return cardinal;
		}

		double d = molecular * 1.0 * cardinal / denominator;

		return (int) d;
	}

	public static void writeError(IOMessage ioMessage, int errCode) {
		ioMessage.setOutputResult(new BaseProtocol(errCode));
	}

	public static void writeOk(IOMessage ioMessage) {
		ioMessage.setOutputResult(new BaseProtocol());
	}

	public static boolean isErrorResult(IOMessage ioMessage) {
		if (ioMessage != null && ioMessage.getOutputResult() != null) {
			if (ioMessage.getOutputResult().getCode() != 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取一个实体数据
	 * 
	 * @param playerId
	 * @param isCreate
	 * @param ioMessage
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BaseEntity> T getEntity(String playerId, boolean isCreate, IOMessage ioMessage, Class<T> clazz) {
		T t = null;
		if (ioMessage != null) {
			t = (T) ioMessage.getInputParse(clazz.getName());
		}
		if (t == null) {
			ICache icache = CacheFactory.getICache();
			t = icache.getEntity(playerId, clazz);
			if (t == null && isCreate) { // 如果创建
				try {
					t = clazz.newInstance();
					t.setKey(playerId);
					icache.setEntity(t);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		if (t != null && ioMessage != null) {
			ioMessage.getInputParse().put(clazz.getName(), t);
		}
		return t;
	}

	/**
	 * 获取今天最后一秒的时间
	 * 
	 * @return
	 */
	public static long getTodayLastTime() {
		long time = 0;
		try {
			String curDate = DateTimeUtil.getStringDate("yyyy-MM-dd") + " 23:59:59";
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			time = formatDate.parse(curDate).getTime();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return time;
	}

	/**
	 * 获取明天的第一秒
	 * 
	 * @return
	 */
	public static long getTomorrowFirstTime() {
		long time = 0;
		try {
			String tomorrow = DateTimeUtil.getStringDate(System.currentTimeMillis() + DateTimeUtil.ONE_DAY_TIME_MS, "yyyy-MM-dd") + " 00:00:00";
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			time = formatDate.parse(tomorrow).getTime();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return time;
	}

	public static List<GoodsBean> stringToGoodsBeanList(String str) {
		List<GoodsBean> goodsBeanList = null;
		if (str != null && !str.isEmpty()) {
			goodsBeanList = new ArrayList<GoodsBean>();
			String[] strArr = str.split(",");
			for (String item : strArr) {
				String[] rewardArr = item.split("=");
				GoodsBean goodsBean = new GoodsBean(Integer.parseInt(rewardArr[0]), Integer.parseInt(rewardArr[1]));
				goodsBeanList.add(goodsBean);
			}
		}
		return goodsBeanList;
	}

	/**
	 * 聊天屏蔽关键字，过滤玩家不健康的聊天内容
	 * 
	 * @param input
	 * @return
	 */
	public static String chatShieldedKeyword(String input) {
		if (StringUtils.isBlank(input)) {
			return input;
		}
		// 过滤所有空格
		input = input.replaceAll(" ", "");
		input = input.replaceAll("　", "");

		for (String item : SysConstants.chatShieldedKeywordArray) {
			if (input.indexOf(item) >= 0) {
				input = input.replaceAll(item, SysConstants.chatShieldedKeywordToString.substring(0, item.length()));
			}
		}
		return input;
	}

	public static boolean checkNameShieldedKeyword(String input) {
		if (StringUtils.isBlank(input)) {
			return false;
		}
		// 过滤所有空格
		input = input.replaceAll(" ", "");
		input = input.replaceAll("　", "");

		for (String item : SysConstants.chatShieldedKeywordArray) {
			if (input.indexOf(item) >= 0) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		// String chatInfo = "共产党万岁";
	}
}
