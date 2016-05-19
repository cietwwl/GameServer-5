package com.mi.game.module.pay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.cache.bean.QueryBean;
import com.mi.core.dao.KeyGeneratorDAO;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.pojo.KeyGenerator;
import com.mi.core.protocol.BaseProtocol;
import com.mi.core.util.ConfigUtil;
import com.mi.core.util.MD5;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.RewardType;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.analyse.pojo.AnalyEntity;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.base.bean.init.server.ServerInfoData;
import com.mi.game.module.dailyLogin.DailyLoginModule;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.pojo.EventMonthCardEntity;
import com.mi.game.module.festival.FestivalModule;
import com.mi.game.module.pay.dao.AppstoreReceiptEntityDao;
import com.mi.game.module.pay.dao.PayEntityDao;
import com.mi.game.module.pay.dao.PayOrderEntityDao;
import com.mi.game.module.pay.data.PayData;
import com.mi.game.module.pay.data.PayTimeRewardData;
import com.mi.game.module.pay.define.PlatFromConstants;
import com.mi.game.module.pay.pojo.AppstoreReceiptEntity;
import com.mi.game.module.pay.pojo.PayEntity;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.pay.protocol.PayProtocol;
import com.mi.game.module.pay.servlet.BasePay;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.vip.VipModule;
import com.mi.game.module.vip.pojo.VipEntity;
import com.mi.game.util.Logs;

@Module(name = ModuleNames.PayModule, clazz = PayModule.class)
public class PayModule extends BaseModule {
	private static RewardModule rewardModule;
	private static VipModule vipModule;
	private static EventModule eventModule;
	private static AnalyseModule analyModule;
	private static FestivalModule festivalModule;
	private static PayEntityDao payEntityDao = PayEntityDao.getInstance();
	private static AppstoreReceiptEntityDao appstoreEntityDao = AppstoreReceiptEntityDao.getInstance();
	private static PayOrderEntityDao orderEntityDao = PayOrderEntityDao.getInstance();
	private final KeyGeneratorDAO keyGeneratorDAO = KeyGeneratorDAO.getInstance();
	private static DailyLoginModule dailyLoginModule;

	/**
	 * 创建订单
	 * 
	 * @param playerID
	 * @param type
	 * @param payMoney
	 * @param parse
	 *            月卡="monthCard" ,限时购买="timeLimit"
	 * @param protocol
	 */
	public PayOrderEntity createOrder(String playerID, int type, int payMoney, String parse) {
		// 创建订单信息
		PayOrderEntity orderEntity = new PayOrderEntity();
		orderEntity.setOrderID(orderID());
		orderEntity.setPlayerID(playerID);
		orderEntity.setPayType(type);
		orderEntity.setPayMoney(payMoney);
		orderEntity.setCreateTime(System.currentTimeMillis());
		orderEntity.setParse(parse);
		// 保存订单
		savePayOrderEntity(orderEntity);
		return orderEntity;
	}

	/**
	 * 获取订单信息
	 * 
	 * @param orderID
	 * @param protocol
	 */
	public void payOrderInfo(String playerID, String orderID, PayProtocol protocol) {
		PayOrderEntity orderEntity = getPayOrderEntity(orderID);
		if (orderEntity == null) {
			protocol.setCode(ErrorIds.ORDER_NOTFOUND);
			return;
		}
		if (orderEntity.getState() == 0) {
			protocol.setCode(ErrorIds.ORDER_NOTDISPOSE);
			return;
		}
		// vip信息
		VipEntity vipEntity = vipModule.initVipEntity(playerID);
		protocol.setVipEntity(vipEntity);
		// 订单信息
		protocol.setOrderEntity(orderEntity);

		if (orderEntity.getClientTime() == 0) {
			orderEntity.setClientTime(System.currentTimeMillis());
			savePayOrderEntity(orderEntity);
			// 物品信息
			protocol.setItemMap(orderEntity.getItemMap());
			// 月卡信息
			protocol.setMonthCardEntity(orderEntity.getMonthCardEntity());
			// 返回充值总额
			PayEntity payEntity = getPayEntity(playerID);
			protocol.setPayEntity(payEntity);
		}
	}

	/**
	 * 获取首冲信息
	 * 
	 * @param playerID
	 * @param protocol
	 */
	public void getPayFirstInfo(String playerID, PayProtocol protocol) {
		PayEntity payEntity = getPayEntity(playerID);
		if (payEntity != null) {
			protocol.setPayEntity(payEntity);
		}
	}

	/**
	 * 充值
	 * 
	 * @param playerID
	 * @param orderEntity
	 * @param protocol
	 */
	public synchronized void payGold(String playerID, PayOrderEntity orderEntity, String payfrom, String store, IOMessage ioMessage) {
		PayProtocol protocol = new PayProtocol();
		if (ioMessage != null) {
			ioMessage.setProtocol(protocol);
		}
		if (orderEntity == null) {
			protocol.setCode(ErrorIds.PAYID_ISNULL);
			Logs.pay.error("充值id为空");
			return;
		}

		// 首次充值
		int is_first = 0;
		int payType = orderEntity.getPayType();
		if (!payDataMap.containsKey(payType)) {
			protocol.setCode(ErrorIds.PAYID_NOTFOUND);
			Logs.pay.error("没有发现充值id");
			return;
		}

		Map<String, Object> itemMap = new HashMap<String, Object>();

		// 关卡物品限时购买, 已改为元宝购买
		// if ("timeLimit".equals(orderEntity.getParse())) {
		// if (PayTimeRewardDataMap.containsKey(payType)) {
		// PayTimeRewardData rewardData = PayTimeRewardDataMap.get(payType);
		// if (dungeonModule.verifyActLimitReward(playerID,
		// rewardData.getDungeonID()))
		// {
		// List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		// for (Entry<Integer, Integer> entry :
		// rewardData.getReward().entrySet()) {
		// goodsList.add(new GoodsBean(entry.getKey(), entry.getValue()));
		// }
		// rewardModule.addGoods(playerID, goodsList, true, null, itemMap,
		// ioMessage);
		// dungeonModule.removeActLimitReward(playerID,
		// rewardData.getDungeonID());
		// }
		// }
		// orderEntity.setItemMap(itemMap);
		// savePayOrderEntity(orderEntity);
		// // 记录统计订单信息.
		// analyModule.analyPay(orderEntity.getOrderID(),
		// orderEntity.getPlayerID(),
		// orderEntity.getPayMoney(), 0, payfrom + "", is_first, store);
		// return;
		// }

		// 月卡
		// if ("monthCard".equals(orderEntity.getParse())) {
		// 修改月卡判断条件 方式为 判断payType
		if (10808 == orderEntity.getPayType()) {
			// 增加月卡
			EventMonthCardEntity monthCardEntity = eventModule.eventMonthCardBuy(playerID, ioMessage);
			orderEntity.setMonthCardEntity(monthCardEntity);
			savePayOrderEntity(orderEntity);
			// 记录统计订单信息.
			analyModule.analyPay(orderEntity.getOrderID(), orderEntity.getPlayerID(), orderEntity.getPayMoney(), 0, payfrom, is_first, store);
			// return;
		}

		// 获取充值信息
		PayData payData = payDataMap.get(payType);

		PayEntity payEntity = getPayEntity(playerID);
		if (payEntity == null) {
			payEntity = new PayEntity();
			payEntity.setPlayerID(playerID);
			// 首冲礼包
			List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
			for (Entry<Integer, Integer> entry : payData.getFirstItem().entrySet()) {
				goodsList.add(new GoodsBean(entry.getKey(), entry.getValue()));
			}
			// 首冲物品放入奖励中心
			rewardModule.addReward(playerID, goodsList, RewardType.firstPayReward);
			is_first = 1;
			orderEntity.setFirstPay(is_first);
		}

		List<GoodsBean> goldList = new ArrayList<GoodsBean>();

		int goldValue = 0;

		if (!payEntity.isFirstPay(payType)) {
			payEntity.addFirstPay(payType);
			// 首冲3倍金币
			for (Entry<Integer, Integer> entry : payData.getFirstGold().entrySet()) {
				goldList.add(new GoodsBean(entry.getKey(), entry.getValue()));
				goldValue += entry.getValue();
			}

		} else {
			for (Entry<Integer, Integer> entry : payData.getGold().entrySet()) {
				goldList.add(new GoodsBean(entry.getKey(), entry.getValue()));
				goldValue += entry.getValue();
			}
		}

		// 增加金币
		rewardModule.addGoods(playerID, goldList, true, null, itemMap, null);

		// 充值金额
		int payValue = payData.getRmb() * 10;
		// 增加充值总数
		payEntity.addPayTotal(payValue);
		savePayEntity(payEntity);

		// 订单增加itemMap
		orderEntity.setItemMap(itemMap);
		savePayOrderEntity(orderEntity);
		// TODO 登录充值接口
		// 每日登录充值接口
		dailyLoginModule.intefaceDailyLoginPay(playerID, payValue);
		// 每日充值活动
		eventModule.intefaceEventPayEveryDay(playerID, payValue);
		// 充值送礼活动
		eventModule.intefaceEventPay(playerID, payValue);
		// 充值抽奖活动
		eventModule.intefaceDrawPay(playerID, payValue);
		// 春节充值加码活动
		festivalModule.intefaceFestivalPayMore(playerID, payValue);
		// 劳动节充值加码活动
		festivalModule.intefaceFestivalLaborDayPayMore(playerID, payValue);
		// 情人节活动
		festivalModule.intefaceFestivaValentine(playerID, payValue);
		// 愚人节活动
		festivalModule.intefaceFestivalFoolDay(playerID, payValue);
		// 记录统计订单信息.
		analyModule.analyPay(orderEntity.getOrderID(), orderEntity.getPlayerID(), orderEntity.getPayMoney(), goldValue, payfrom, is_first, store);
		// 统计充值数据
		AnalyseModule.interfaceAnalyse(playerID, store, orderEntity.getOrderID(), orderEntity.getPayMoney(), is_first, payfrom);
	}

	public void getPayInfo() {
		long total = payEntityDao.getPayEntityCount();
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setTotal(total);
		queryInfo.initTotalPage();
		QueryBean queryBean = new QueryBean("payTotal", QueryType.GREATERTHAN, 0);
		queryInfo.addQueryBean(queryBean);
		List<PayEntity> payList = null;
		Map<String, Integer> payMap = new HashMap<>();
		while (queryInfo.getPage() <= queryInfo.getTotalPage()) {
			payList = payEntityDao.queryPage(queryInfo);
			if (payList == null || payList.isEmpty()) {
				break;
			}

			for (int i = 0; i < payList.size(); i++) {
				PayEntity payEntity = payList.get(i);
				int payTotal = payEntity.getPayTotal();
				String key = payEntity.getKey().toString();
				if (payMap.containsKey(key)) {
					payMap.put(key, payMap.get(key) + payTotal);
				} else {
					payMap.put(key, payTotal);
				}
			}
			queryInfo.setPage(queryInfo.getPage() + 1);
		}

		Set<Entry<String, Integer>> entrySet = payMap.entrySet();
		try {
			FileOutputStream out = new FileOutputStream(new File("pay.properties"));
			for (Entry<String, Integer> entry : entrySet) {
				String key = entry.getKey();
				int value = entry.getValue();
				String platformID = "";
				AnalyEntity analyEntity = analyModule.getAnalyEntity(key);
				if (analyEntity != null) {
					platformID = analyEntity.getUid();
				} else {
					logger.error(key + "的平台ID为空");
				}
				String info = platformID + "#" + value;
				out.write((info + "\r\n").getBytes());
			}
			out.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			logger.error("文件未找到");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("文件写入错误");
			e.printStackTrace();
		}

	}

	/**
	 * 初始化订单ID
	 * */
	private void initOrderID() {
		String clsName = SysConstants.orderIDEntity;
		KeyGenerator keyGenerator = keyGeneratorDAO.getEntity(clsName);
		if (keyGenerator == null) {
			keyGenerator = new KeyGenerator();
			keyGenerator.setKey(clsName);
			keyGenerator.setNextId(SysConstants.orderStatID);
			keyGeneratorDAO.save(keyGenerator);
		}
	}

	/**
	 * 获取订单ID
	 * */
	public String orderID() {
		String clsName = SysConstants.orderIDEntity;
		long orderID = keyGeneratorDAO.updateInc(clsName);
		return orderID + "";
	}

	@Override
	public void init() {
		rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		vipModule = ModuleManager.getModule(ModuleNames.VipModule, VipModule.class);
		eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		analyModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		festivalModule = ModuleManager.getModule(ModuleNames.FestivalModule, FestivalModule.class);
		dailyLoginModule = ModuleManager.getModule(ModuleNames.DailyLoginModule, DailyLoginModule.class);
		initPayData();
		initOrderID();
		initServerListData();
		initPayTimeRewardData();
	}

	// 充值金额奖励配置
	public static Map<Integer, PayData> payDataMap = new HashMap<Integer, PayData>();
	// 限时购买物品列表
	public static Map<Integer, PayTimeRewardData> PayTimeRewardDataMap = new HashMap<Integer, PayTimeRewardData>();
	// 服务器列表
	public static Map<String, ServerInfoData> serverListMap = new HashMap<String, ServerInfoData>();

	public void initServerListData() {
		List<ServerInfoData> dataList = TemplateManager.getTemplateList(ServerInfoData.class);
		for (ServerInfoData serverInfo : dataList) {
			serverListMap.put(serverInfo.getServerID() + "", serverInfo);
		}
	}

	private void initPayTimeRewardData() {
		List<PayTimeRewardData> dataList = TemplateManager.getTemplateList(PayTimeRewardData.class);
		for (PayTimeRewardData data : dataList) {
			PayTimeRewardDataMap.put(data.getPayID(), data);
		}
	}

	private void initPayData() {
		List<PayData> dataList = TemplateManager.getTemplateList(PayData.class);
		for (PayData data : dataList) {
			payDataMap.put(data.getPid(), data);
		}
	}

	public PayOrderEntity getPayOrderEntity(String orderID) {
		return orderEntityDao.getPayOrderEntity(orderID);
	}

	public void savePayOrderEntity(PayOrderEntity payOrderEntity) {
		orderEntityDao.save(payOrderEntity);
	}

	public PayEntity getPayEntity(String playerID) {
		return payEntityDao.getPayEntity(playerID);
	}

	public void savePayEntity(PayEntity payEntity) {
		payEntityDao.save(payEntity);
	}

	public AppstoreReceiptEntity getAppstoreReceiptEntity(String key) {
		return appstoreEntityDao.getEntity(key);
	}

	public void saveAppstoreReceiptEntity(AppstoreReceiptEntity entity) {
		appstoreEntityDao.save(entity);
	}

	/**
	 * 腾讯平台,查询用户账户余额
	 * 
	 * @param ioMessage
	 * @param baseProtocol
	 * @throws UnsupportedEncodingException
	 */
	public void getTencentUserBalance(IOMessage ioMessage, BaseProtocol baseProtocol) throws UnsupportedEncodingException {
		// 参数字符串a=1&b=2&c=3
		String paramsStr = (String) ioMessage.getInputParse("paramStr");
		if (paramsStr == null || "".equals(paramsStr)) {
			baseProtocol.setCode(ErrorIds.ParamWrong);
			ioMessage.setProtocol(baseProtocol);
		}
		String[] params = paramsStr.split("&");
		Map<String, String> paraMap = new HashMap<String, String>();
		for (String keyValue : params) {
			String[] arr = keyValue.split("=");
			paraMap.put(arr[0], URLEncoder.encode(arr[1], "UTF-8"));
		}
		// 测试地址
		String baseUrl = "http://119.147.19.43/v3/user/get_info?";
		String balanceResult = BasePay.sendPlatFormRequest(baseUrl, paraMap);
		baseProtocol.putExtMap("balanceResult", balanceResult);
		ioMessage.setProtocol(baseProtocol);
	}

	/** 腾讯平台appid和appkey **/
	public final static String TENCENT_APP_ID = ConfigUtil.getString("tencent.appid");
	public final static String TENCENT_APP_KEY = ConfigUtil.getString("tencent.appkey");

	/**
	 * 比对订单信息
	 * 
	 * @param infoMD5
	 * @param orderId
	 */
	public void tencentPay(String infoMD5, String orderId, IOMessage ioMessage) {
		BaseProtocol protocol = new BaseProtocol();
		PayOrderEntity orderEntity = orderEntityDao.getPayOrderEntity(orderId);
		if (orderEntity == null) {
			protocol.setCode(ErrorIds.PAYID_NOTFOUND);
			ioMessage.setProtocol(protocol);
			return;
		}
		int payMoney = orderEntity.getPayMoney();
		String info = orderId + "_|" + payMoney + "_|" + TENCENT_APP_ID + "_|" + TENCENT_APP_KEY;
		String result = MD5.md5(info);
		if (!result.equals(infoMD5)) {
			protocol.setCode(ErrorIds.ParamWrong);
			ioMessage.setProtocol(protocol);
			return;
		}

		// 订单状态不是初始状态
		if (orderEntity.getState() != 0) {
			Logs.pay.error("订单：" + orderId + " 不是初始状态");
			protocol.setCode(ErrorIds.ORDER_DISPOSED);
			ioMessage.setProtocol(protocol);
			return;
		}

		// 订单初始状态
		if (orderEntity.getState() == 0) {
			Logs.pay.error("订单：" + orderId + "支付完成，开始添加玩家元宝");
			orderEntity.setState(1);
			orderEntity.setCallbackTime(System.currentTimeMillis());
			orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_BAIDU);
			this.savePayOrderEntity(orderEntity);
			// 处理充值
			this.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_TENCENT, null);
		}
		protocol.setCode(0);
		ioMessage.setProtocol(protocol);
	}

}
