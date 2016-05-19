package com.mi.game.module.analyse;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;

import com.alibaba.fastjson.JSONObject;
import com.mi.core.dao.KeyGeneratorDAO;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.util.ConfigUtil;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.analyse.dao.AnalyEntityDao;
import com.mi.game.module.analyse.dao.AnalyPayEntityDao;
import com.mi.game.module.analyse.dao.DeviceEntityDao;
import com.mi.game.module.analyse.pojo.AnalyEntity;
import com.mi.game.module.analyse.pojo.AnalyPayEntity;
import com.mi.game.module.analyse.pojo.DeviceEntity;
import com.mi.game.module.analyse.protocol.AnalyProtocol;
import com.mi.game.module.analyse.task.AnalyseTask;
import com.mi.game.module.analyse.task.ExportPayDBTask;
import com.mi.game.module.analyse.task.ExportUserDBTask;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.dungeon.data.ActData;
import com.mi.game.module.friend.FriendModule;
import com.mi.game.module.friend.pojo.FriendEntity;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.vip.VipModule;
import com.mi.game.module.vip.pojo.VipEntity;
import com.mi.game.module.wallet.WalletModule;
import com.mi.game.module.wallet.pojo.WalletEntity;
import com.mi.game.util.Logs;
import com.mi.game.util.TestUserSendRewardUtil;
import com.mi.game.util.Utilities;

@Module(name = ModuleNames.AnalyseModule, clazz = AnalyseModule.class)
public class AnalyseModule extends BaseModule {

	private final KeyGeneratorDAO keyGeneratorDAO = KeyGeneratorDAO.getInstance();
	private static AnalyEntityDao analyEntityDao = AnalyEntityDao.getInstance();
	private static DeviceEntityDao deviceEntityDao = DeviceEntityDao.getInstance();
	private static AnalyPayEntityDao analyPayDao = AnalyPayEntityDao.getInstance();
	private static LoginModule loginModule;
	private static VipModule vipModule;
	private static HeroModule heroModule;
	private static WalletModule walletModule;
	private static FriendModule friendModule;

	private static boolean open = ConfigUtil.getBoolean("analyse.open");
	private static boolean clientOpen = ConfigUtil.getBoolean("analyse.client.open");
	private static int exportTime = ConfigUtil.getInt("analyse.export.time", 3);

	private String getDID() {
		String clsName = SysConstants.DeviceIDEntity;
		long did = keyGeneratorDAO.updateInc(clsName);
		return did + "";
	}

	/**
	 * 记录设备
	 * 
	 * @param device_id
	 * @param phonetype
	 * @param platform
	 */
	public void deviceAnaly(String device_id, String phonetype, String platform, AnalyProtocol protocol) {
		DeviceEntity deviceEntity = getDeviceEntity(device_id);
		if (deviceEntity == null) {
			deviceEntity = new DeviceEntity();
			deviceEntity.setDid(getDID());
			deviceEntity.setDevice_id(device_id);
			deviceEntity.setPhonetype(phonetype);
			deviceEntity.setPlatform(platform);
			deviceEntity.setCreateTime(System.currentTimeMillis());
			protocol.setStatus("new");
		} else {
			protocol.setStatus("old");
			deviceEntity.setUpdateTime(System.currentTimeMillis());
		}
		saveDeviceEntity(deviceEntity);
	}

	/**
	 * 每日定时导出 user数据
	 */
	public void exportUserDB() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		// 开始导出
		if (hour == exportTime) {
			try {
				Timer timer = new Timer();
				ExportUserDBTask task = new ExportUserDBTask();
				timer.schedule(task, 100);
			} catch (Exception e) {
				e.printStackTrace();
				Logs.logger.error("统计数据导出出错!");
			}
		}
	}

	/**
	 * 每日定时导出 订单数据
	 */
	public void exportPayDB() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		// 开始导出
		if (hour == exportTime) {
			try {
				Timer timer = new Timer();
				ExportPayDBTask task = new ExportPayDBTask();
				timer.schedule(task, 100);
			} catch (Exception e) {
				e.printStackTrace();
				Logs.logger.error("统计数据导出出错!");
			}
		}
	}

	/**
	 * 后台执行导出 user数据
	 */
	public void manageExportUserDB() {
		try {
			Timer timer = new Timer();
			ExportUserDBTask task = new ExportUserDBTask();
			timer.schedule(task, 100);
		} catch (Exception e) {
			e.printStackTrace();
			Logs.logger.error("用户数据导出出错!");
		}
	}

	/**
	 * 后台执行导出 订单数据
	 */
	public void manageExportPayDB() {
		try {
			Timer timer = new Timer();
			ExportPayDBTask task = new ExportPayDBTask();
			timer.schedule(task, 100);
		} catch (Exception e) {
			e.printStackTrace();
			Logs.logger.error("订单数据导出出错!");
		}
	}

	/**
	 * 客户端统计
	 * 
	 * @param playerID
	 * @param clientType
	 *            //统计类型
	 * @param params
	 *            //统计内容
	 * @param protocol
	 */
	public void clientAnaly(String playerID, String clientType, JSONObject params, AnalyProtocol protocol) {
		try {
			if ("guide".equals(clientType)) {
				analyLog(playerID, "DOT_RECORDS", params, null, null, null);
			} else {
				if (clientOpen) {
					analyLog(playerID, "CLIENT_ANALY", params, null, null, null);
				}
			}
		} catch (Exception e) {
			Logs.logger.error("客户端统计错误");
		}
	}

	/**
	 * 订单数据记录
	 * 
	 * @param payno
	 * @param uid
	 * @param pay_money
	 * @param add_coin
	 * @param payfrom
	 * @param level
	 * @param is_first
	 */
	public void analyPay(String payno, String player_id, int pay_money, int add_coin, String payfrom, int is_first, String store) {
		try {
			AnalyPayEntity analyPayEntity = getAnalyPayEntity(payno);
			if (analyPayEntity == null) {
				analyPayEntity = new AnalyPayEntity();
				AnalyEntity analyEntity = getAnalyEntity(player_id);
				analyPayEntity.setAdd_coin(add_coin);
				analyPayEntity.setDevice_id(analyEntity.getDevice_id());
				analyPayEntity.setImei(analyEntity.getImei());
				analyPayEntity.setIs_first(is_first);
				PlayerEntity playerEntity = loginModule.getPlayerEntity(player_id);
				analyPayEntity.setLevel(playerEntity.getLevel());
				analyPayEntity.setMac(analyEntity.getMac());
				analyPayEntity.setOsversion(analyEntity.getOsversion());
				analyPayEntity.setPay_money(pay_money);
				analyPayEntity.setPayfrom(payfrom);
				analyPayEntity.setPayno(payno);
				analyPayEntity.setPaytime(Utilities.getDateTime("yyyy-MM-dd HH:mm:ss"));
				analyPayEntity.setPhonetype(analyEntity.getPhonetype());
				analyPayEntity.setPlayer_id(player_id);
				analyPayEntity.setServer(analyEntity.getServer());
				analyPayEntity.setStore(store);
				analyPayEntity.setUid(analyEntity.getUid());
				saveAnalyPayEntity(analyPayEntity);
			} else {
				Logs.pay.info("订单重复调用");
			}
		} catch (Exception e) {
			Logs.pay.info("获取统计数据异常");
		}

	}

	/**
	 * 统计数据
	 * 
	 * @param playerID
	 * @param puid
	 * @param sex
	 * @param device_id
	 * @param osversion
	 * @param phonetype
	 * @param imei
	 * @param mac
	 * @param store
	 * @param server
	 * @param os
	 * @param gameid
	 * @param ip
	 * @param protocol
	 */
	public void analyInfo(String playerID, String puid, int sex, String device_id, String osversion, String phonetype, String imei, String mac, String store,
			String server, String os, String gameid, String ip, AnalyProtocol protocol) {
		try {
			AnalyEntity analyEntity = getAnalyEntity(playerID);
			String nowTime = Utilities.getDateTime("yyyy-MM-dd HH:mm:ss");
			PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
			VipEntity vipEntity = vipModule.initVipEntity(playerID);
			Hero hero = heroModule.getLead(playerID, null);
			WalletEntity walletEntity = walletModule.getwalletEntity(playerID);
			FriendEntity friendEntity = friendModule.getFriendEntity(playerID);
			if (analyEntity == null) {
				analyEntity = new AnalyEntity();
				analyEntity.setUid(puid);
				analyEntity.setDevice_id(device_id);
				analyEntity.setAdd_time(nowTime);
				analyEntity.setFirst_login_ip(ip);
				// 最后登录ip
				analyEntity.setLast_login_ip(ip);
				// 最后登录时间
				analyEntity.setLast_login_time(nowTime);
				// 登录总天数
				analyEntity.setTotal_num(1);
				// 连续登录天数
				analyEntity.setLogin_num(1);
				analyEntity.setPlayer_id(playerID);
				analyEntity.setRole_name(playerEntity.getNickName());
				analyEntity.setOccupational(playerEntity.getSex() + "");
				analyEntity.setRole_sex(playerEntity.getSex());
				analyEntity.setCreate_time(nowTime);
				// 用户等级
				analyEntity.setLevel(playerEntity.getLevel());
				// vip等级
				analyEntity.setVip_level(vipEntity.getVipLevel());
				// 经验值
				analyEntity.setExp(hero.getExp());
				// 元包数
				analyEntity.setGoldingot((int) walletEntity.getGold());
				// 好友数量
				analyEntity.setFriends_num(friendEntity.getFriendList().size());
				analyEntity.setOsversion(osversion);
				analyEntity.setPhonetype(phonetype);
				analyEntity.setImei(imei);
				analyEntity.setMac(mac);
				analyEntity.setStore(store);
				analyEntity.setServer(server);
				analyEntity.setOs(os);
				analyEntity.setGameid(gameid);
				saveAnalyEntity(analyEntity);
				protocol.setStatus("new");
			} else {
				Date lastDate = DateTimeUtil.getDate(analyEntity.getLast_login_time(), "yyyy-MM-dd");
				if (!DateTimeUtil.isToday(lastDate)) {
					// 登录总天数
					analyEntity.setTotal_num(analyEntity.getTotal_num() + 1);
				}
				if (DateTimeUtil.getDiffDay(new Date(), lastDate) > 1) {
					analyEntity.setLogin_num(1);
				} else {
					// 连续登录天数
					if (!DateTimeUtil.isToday(lastDate)) {
						analyEntity.setLogin_num(analyEntity.getLogin_num() + 1);
					}
				}
				// 最后登录ip
				analyEntity.setLast_login_ip(ip);
				// 最后登录时间
				analyEntity.setLast_login_time(nowTime);
				// 用户等级
				analyEntity.setLevel(playerEntity.getLevel());
				// vip等级
				analyEntity.setVip_level(vipEntity.getVipLevel());
				// 经验值
				analyEntity.setExp(hero.getExp());
				// 元包数
				analyEntity.setGoldingot((int) walletEntity.getGold());
				// 好友数量
				analyEntity.setFriends_num(friendEntity.getFriendList().size());
				saveAnalyEntity(analyEntity);
				protocol.setStatus("old");
			}
		} catch (Exception e) {
			Logs.logger.error("统计数据收集出错");
		} finally {
			// 发送测试用户奖励
			if (TestUserSendRewardUtil.isSendReward) {
				loginModule.testUserSendReward(playerID);
			}
			// 记录统计日志
			analyLog(playerID, "PHONE_INFO", null, null, null, null);
		}
	}

	@Override
	public void init() {
		// print();

		loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		vipModule = ModuleManager.getModule(ModuleNames.VipModule, VipModule.class);
		heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		walletModule = ModuleManager.getModule(ModuleNames.WalletModule, WalletModule.class);
		friendModule = ModuleManager.getModule(ModuleNames.FriendMoudle, FriendModule.class);
	}

	public AnalyPayEntity getAnalyPayEntity(String payno) {
		return analyPayDao.getAnalyPayEntity(payno);
	}

	public List<AnalyPayEntity> getAllAnalyPayEntity() {
		return analyPayDao.getAllAnalyPayEntity();
	}

	public void saveAnalyPayEntity(AnalyPayEntity entity) {
		analyPayDao.save(entity);
	}

	public AnalyEntity getAnalyEntity(String playerID) {
		return analyEntityDao.getAnalyEntity(playerID);
	}

	public List<AnalyEntity> getAllAnalyEntity() {
		return analyEntityDao.getAllAnalyEntity();
	}

	public void saveAnalyEntity(AnalyEntity entity) {
		analyEntityDao.save(entity);
	}

	public DeviceEntity getDeviceEntity(String device_id) {
		return deviceEntityDao.getDeviceEntity(device_id);
	}

	public void saveDeviceEntity(DeviceEntity entity) {
		deviceEntityDao.save(entity);
	}

	/**
	 * 
	 * 有爱统计数据收集
	 * 
	 * @param playerID
	 *            用户id
	 * @param action
	 *            动作
	 * @param v1
	 *            参数
	 * @param v2
	 *            参数
	 * @param v3
	 *            参数
	 * @param v4
	 *            参数
	 */
	public void analyLog(String playerID, String action, JSONObject v1, JSONObject v2, JSONObject v3, JSONObject v4) {
		try {
			AnalyEntity entity = getAnalyEntity(playerID);
			if (entity != null) {
				String scen = "0";
				int stay = 1;
				String ts = Utilities.getDateTime("yyyy-MM-dd HH:mm:ss");
				Logs.analy(ts, entity.getUid(), entity.getDevice_id(), playerID, scen, entity.getLevel(), stay, action, v1, v2, v3, v4, entity.getLast_login_ip(),
						entity.getOsversion(), entity.getPhonetype(), entity.getImei(), entity.getMac(), entity.getStore(), entity.getServer(), entity.getOs(),
						entity.getGameid());
			}
		} catch (Exception e) {
			Logs.logger.error("统计日志收集出错!");
		}
	}

	/**
	 * 元宝消耗日志
	 * 
	 * @param playerID
	 *            用户id
	 * @param money
	 *            消费金额
	 * @param wpnum
	 *            物品数量
	 * @param price
	 *            物品价格
	 * @param wpid
	 *            物品id
	 * @param wptype
	 *            物品类型
	 */
	public void goldCostLog(String playerID, int money, int wpnum, int price, String wpid, String wptype) {
		try {
			AnalyEntity entity = getAnalyEntity(playerID);
			if (entity != null) {
				JSONObject params = new JSONObject();
				params.put("money", money);
				params.put("wpnum", wpnum);
				params.put("price", price);
				params.put("wpid", wpid);
				params.put("wptype", wptype);
				params.put("level", entity.getLevel());
				analyLog(playerID, "GOLD_COST", params, null, null, null);
			}
		} catch (Exception e) {
			Logs.logger.error("统计日志收集出错!");
		}

	}

	/**
	 * 操作数据接口
	 * 
	 */
	public static void interfaceAnalyse(IOMessage ioMessage) {
		if (open) {
			try {
				Timer timer = new Timer();
				AnalyseTask task = new AnalyseTask(ioMessage);
				timer.schedule(task, 100);
			} catch (Exception e) {
				e.printStackTrace();
				Logs.logger.error("统计数据收集出错!!!");
			}
		}
	}

	/**
	 * 充值统计
	 * 
	 * @param playerID
	 *            用户id
	 * @param platform
	 *            平台类型
	 * @param orderID
	 *            订单号
	 * @param money
	 *            充值金额
	 * @param first
	 *            是否首冲
	 * @param payfrom
	 *            充值渠道
	 */
	public static void interfaceAnalyse(String playerID, String platform, String orderID, int money, int first, String payfrom) {
		if (open) {
			try {
				Timer timer = new Timer();
				Map<String, Object> payMap = new HashMap<String, Object>();
				payMap.put("playerID", playerID);
				payMap.put("platform", platform);
				payMap.put("orderID", orderID);
				payMap.put("money", money);
				payMap.put("first", first);
				payMap.put("payfrom", payfrom);
				AnalyseTask task = new AnalyseTask(payMap);
				timer.schedule(task, 100);
			} catch (Exception e) {
				e.printStackTrace();
				Logs.logger.error("统计数据收集出错!!!");
			}
		}
	}

	// ////////
	// ////////////////////////////////////
	// ///////////////////////
	// ////////////////////////////////////

	// 关卡数据

	public void print() {
		daGuan();
		xiaoGuan();
	}

	private static void xiaoGuan() {
		JSONObject result = new JSONObject();
		String id = "1010";
		for (int i = 1; i < 262; i++) {
			JSONObject temp = new JSONObject();
			temp.put("attackNum", 0);
			temp.put("starLevel", 1);
			temp.put("templateID", Integer.parseInt(id + i));
			result.put(id + i, temp);
		}
		System.out.println(result);
	}

	private static void daGuan() {
		JSONObject result = new JSONObject();
		List<ActData> list = TemplateManager.getTemplateList(ActData.class);
		String id = "1011";
		int i = 1;
		for (ActData ad : list) {
			JSONObject temp = new JSONObject();
			temp.put("actPoint", ad.getMaxStar());
			temp.put("pass", true);
			JSONObject rewardList = new JSONObject();
			Set<Entry<Integer, List<GoodsBean>>> set = ad.getReward().entrySet();
			for (Entry<Integer, List<GoodsBean>> keyValue : set) {
				JSONObject value = new JSONObject();
				value.put("point", keyValue.getKey());
				value.put("state", 0);
				rewardList.put(keyValue.getKey() + "", value);
			}
			temp.put("rewardlist", rewardList);
			temp.put("templateID", Integer.parseInt(id + i));
			result.put(id + i, temp);
			i++;
		}
		System.out.println(result);
	}

}
