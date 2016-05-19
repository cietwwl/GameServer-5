package com.mi.game.module.cdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mi.core.dao.KeyGeneratorDAO;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.pojo.KeyGenerator;
import com.mi.core.util.ConfigUtil;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.admin.handler.TemplateManageHandler;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.cdk.dao.CDKEntityDao;
import com.mi.game.module.cdk.dao.CDKRewardEntityDao;
import com.mi.game.module.cdk.dao.CDKTypeEntityDao;
import com.mi.game.module.cdk.dao.PlayerCDKEntityDao;
import com.mi.game.module.cdk.pojo.CDKEntity;
import com.mi.game.module.cdk.pojo.CDKRewardEntity;
import com.mi.game.module.cdk.pojo.CDKTypeEntity;
import com.mi.game.module.cdk.pojo.PlayerCDKEntity;
import com.mi.game.module.cdk.protocol.CDKProtocol;
import com.mi.game.module.cdk.task.CDKTask;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.pojo.EventExpenseEntity;
import com.mi.game.module.login.dao.LoginInfoDAO;
import com.mi.game.module.login.pojo.LoginInfoEntity;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.vip.VipModule;
import com.mi.game.module.wallet.WalletModule;
import com.mi.game.module.wallet.pojo.WalletEntity;
import com.mi.game.util.Logs;

@Module(name = ModuleNames.CDKModule, clazz = CDKModule.class)
public class CDKModule extends BaseModule {

	private static String[] publicPlat = {
			"ml","hg","dn","tg","ya","3d"
	};
	private static List<String> filterPlats = Arrays.asList(publicPlat);

	private final KeyGeneratorDAO keyGeneratorDAO = KeyGeneratorDAO.getInstance();

	private static CDKEntityDao CDKDao = CDKEntityDao.getInstance();
	private static PlayerCDKEntityDao playerCDKDao = PlayerCDKEntityDao.getInstance();
	private static CDKRewardEntityDao CDKRewardDao = CDKRewardEntityDao.getInstance();
	private static CDKTypeEntityDao CDKTypeDao = CDKTypeEntityDao.getInstance();
	private static boolean isLoginServer = ConfigUtil.getBoolean("isLoginServer");
	private static String servertype = ConfigUtil.getString("servertype");

	public void getBug() {
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		VipModule vipModule = ModuleManager.getModule(ModuleNames.VipModule, VipModule.class);
		List<EventExpenseEntity> expenseRank = eventModule.getExpenseRank();
		for (EventExpenseEntity expense : expenseRank) {
			String playerID = expense.getPlayerID();
			int payTotal = vipModule.playerPayTotal(playerID);
			logger.error("消费最多1000名用户##playerID=" + playerID + ",ExpenseTotal=" + expense.getExpenseTotal() + ",payTotal=" + payTotal + "");
		}
		WalletModule walletModule = ModuleManager.getModule(ModuleNames.WalletModule, WalletModule.class);
		List<WalletEntity> walletRank = walletModule.getWalletRank();
		for (WalletEntity entity : walletRank) {
			String playerID = entity.getPlayerID();
			int payTotal = vipModule.playerPayTotal(playerID);
			logger.error("金币最多1000名用户##playerID=" + playerID + ",gold=" + entity.getGold() + ",payTotal=" + payTotal);
		}
	}

	public void getCDKType(String playerID, String cdk, CDKProtocol protocol) {
		if (!isLoginServer) {
			logger.error("应用服务器没有这个协议!");
		}
		if (cdk == null || cdk.isEmpty()) {
			protocol.setCode(ErrorIds.CDK_IS_NULL);
			return;
		}
		cdk = cdk.toLowerCase();
		CDKEntity cdkEntity = getCDKEntity(cdk);
		if (cdkEntity == null) {
			protocol.setCode(ErrorIds.CDK_NOT_FOUND);
			return;
		}
		long nowTime = System.currentTimeMillis();
		if (cdkEntity.getStartTime() != 0 && cdkEntity.getStartTime() > nowTime) {
			protocol.setCode(ErrorIds.CDK_TIME_ERROR);
			return;
		}
		if (cdkEntity.getEndTime() != 0 && cdkEntity.getEndTime() < nowTime) {
			protocol.setCode(ErrorIds.CDK_TIME_ERROR);
			return;
		}
		String typeID = cdkEntity.getTypeID();
		CDKTypeEntity typeEntity = getCdkTypeEntity(typeID);
		if (typeEntity == null) {
			protocol.setCode(ErrorIds.CDK_TYPE_ERROR);
			return;
		}
		protocol.setTypeID(typeID);
		protocol.setpNum(typeEntity.getPnum());
	}

	/**
	 * cdk 兑换
	 * 
	 * @param playerID
	 * @param cdk
	 * @param protocol
	 */
	public void useCDK(String playerID, String cdk, CDKProtocol protocol) {
		int code = 0;

		// ///////////////////////////////////////////////
		// ////// 本地处理逻辑
		// //////
		// ////////////////////////////////////////////
		if (!isLoginServer) {
			// 获取cdk 类型 使用次数
			String result = getRemoteResult(playerID, cdk, HandlerIds.CDK_TYPE + "");
			JSONObject json = JSON.parseObject(result);
			code = json.getIntValue("code");
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			// cdk类型
			String typeID = json.getString("typeID");
			// 每人可使用次数
			int pNum = json.getIntValue("pNum");
			PlayerCDKEntity playerCDKEntity = getPlayerCDKEntity(playerID);
			if (playerCDKEntity == null) {
				playerCDKEntity = new PlayerCDKEntity();
				playerCDKEntity.setPlayerID(playerID);
			}
			Map<String, Integer> useCdk = playerCDKEntity.getUseCDK();
			if (useCdk.containsKey(typeID)) {
				int useNum = useCdk.get(typeID);
				if (useNum >= pNum) {
					protocol.setCode(ErrorIds.CDK_USED);
					return;
				}
			}

			// 获取cdk奖励
			result = getRemoteResult(playerID, cdk, HandlerIds.CDK_USE + "");
			json = JSON.parseObject(result);
			code = json.getIntValue("code");
			if (code != 0) {
				protocol.setCode(code);
				return;
			}

			// cdk类型
			typeID = json.getString("typeID");
			// 每人可使用次数
			pNum = json.getIntValue("pNum");

			if (useCdk.containsKey(typeID)) {
				int useNum = useCdk.get(typeID);
				if (useNum >= pNum) {
					protocol.setCode(ErrorIds.CDK_USED);
					return;
				} else {
					useCdk.put(typeID, useNum + 1);
				}
			} else {
				useCdk.put(typeID, 1);
			}
			savePlayerCDKEntity(playerCDKEntity);

			List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
			String rewards = json.getString("rewards");
			goodsList = JSON.parseArray(rewards, GoodsBean.class);

			Map<String, Object> itemMap = new HashMap<String, Object>();
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
			code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			protocol.setItemMap(itemMap);
			protocol.setShowMap(goodsList);
			return;
		}

		// ///////////////////////////////////////////////
		// ////// 登录服务器处理逻辑
		// //////
		// ////////////////////////////////////////////

		if (cdk == null || cdk.isEmpty()) {
			protocol.setCode(ErrorIds.CDK_IS_NULL);
			return;
		}
		cdk = cdk.toLowerCase();
		CDKEntity cdkEntity = getCDKEntity(cdk);
		if (cdkEntity == null) {
			protocol.setCode(ErrorIds.CDK_NOT_FOUND);
			return;
		}
		long nowTime = System.currentTimeMillis();
		if (cdkEntity.getStartTime() != 0 && cdkEntity.getStartTime() > nowTime) {
			protocol.setCode(ErrorIds.CDK_TIME_ERROR);
			return;
		}
		if (cdkEntity.getEndTime() != 0 && cdkEntity.getEndTime() < nowTime) {
			protocol.setCode(ErrorIds.CDK_TIME_ERROR);
			return;
		}
		String typeID = cdkEntity.getTypeID();
		CDKTypeEntity typeEntity = getCdkTypeEntity(typeID);
		if (typeEntity == null) {
			protocol.setCode(ErrorIds.CDK_TYPE_ERROR);
			return;
		}

		String platform = cdkEntity.getPlatFrom();
		if (StringUtils.isNotEmpty(platform)) {
			platform = platform.toLowerCase();
		}
		if (StringUtils.isNotEmpty(platform) && !filterPlats.contains(platform)) {
			LoginInfoEntity loginInfoEntity = LoginInfoDAO.getInstance().getEntity(playerID);
			if (loginInfoEntity != null) {
				String playerName = loginInfoEntity.getPlayerName();
				if (playerName.indexOf(platform) == -1) {
					protocol.setCode(ErrorIds.CDK_TYPE_ERROR);
					return;
				}
			} else {
				logger.error("没有发现 " + playerID + " 的信息");
			}
		}

		if (cdkEntity.isUsed() >= typeEntity.getNum()) {
			protocol.setCode(ErrorIds.CDK_IS_USED);
			return;
		}

		CDKRewardEntity rewardEntity = getCdkRewardEntity(cdkEntity.getRewardID());
		if (rewardEntity == null) {
			protocol.setCode(ErrorIds.CDK_REWARD_ERROR);
			return;
		}

		protocol.setRewards(rewardEntity.getItems());
		protocol.setTypeID(typeID);
		protocol.setpNum(typeEntity.getPnum());
		cdkEntity.setUseTime(nowTime);
		cdkEntity.setUsed(cdkEntity.isUsed() + 1);
		saveCDKEntity(cdkEntity);
	}

	private String getRemoteResult(String playerID, String cdk, String handlerID) {
		Map<String, String> params = new HashMap<>();
		String loginPlayerID = playerID;
		String[] strArr = playerID.split("-");
		if (strArr.length > 1) {
			loginPlayerID = playerID.split("-")[1];
		}
		params.put("type", handlerID);
		params.put("playerID", loginPlayerID);
		params.put("cdk", cdk);
		params.put("uniqueKey", "123456");
		String address = getAddressByType();
		String result = sendRequest(address, params);
		return result;
	}

	@Override
	public void init() {
		if (!hasCDKReward()) {
			CDKRewardEntity entity = new CDKRewardEntity();
			entity.setDesc("初始化奖励,请误删除!");
			entity.setRewardID("1");
			List<GoodsBean> rewardList = new ArrayList<GoodsBean>();
			// 进阶丹一枚
			rewardList.add(new GoodsBean(10174, 1));
			entity.setItems(rewardList);
			saveCDKRewardEntity(entity);
		}
		if (!hasCDKType()) {
			CDKTypeEntity entity = new CDKTypeEntity();
			entity.setTypeID("1");
			entity.setDesc("初始化类型,请误删除!");
			saveCDKTypeEntity(entity);
		}

		initCDKID();
		// try {
		// loadCDK();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

	/**
	 * 生成cdk
	 * 
	 * @param type
	 * @param platFrom
	 * @param rewardID
	 * @param num
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String addCDK(String type, String platFrom, String rewardID, int num, String startTime, String endTime) {
		String result = "";
		Set<Integer> cdkCode = new HashSet<Integer>();
		try {
			String path = TemplateManageHandler.class.getResource("/").getPath().split("WEB-INF")[0];
			// cdk 批次号
			String batch = cdkID();
			File cdkDir = new File(path + "/cdk/");
			cdkDir.mkdirs();
			String fileName = "/" + platFrom + "_" + type + "_" + batch + "_" + rewardID + ".txt";
			File cdkFile = new File(cdkDir.getPath() + fileName);
			if (cdkFile.isFile()) {
				cdkFile.delete();
			}
			cdkFile.createNewFile();
			FileWriter fw = new FileWriter(cdkFile);
			for (int i = 0; i < num; i++) {
				String cdkString = CDK(batch, type, platFrom, i + "");
				if (!cdkCode.contains(cdkString.hashCode())) {
					cdkCode.add(cdkString.hashCode());
					fw.write(cdkString + "\n");
				}
			}
			fw.close();
			result = "/cdk" + fileName;
			// 异步cdk入库
			asynInsertCDK(num, batch, type, platFrom, rewardID, startTime, endTime);

		} catch (Exception e) {
			e.printStackTrace();
			Logs.logger.error("生成cdk文件错误!");
		}
		return result;
	}

	private static String CDK(String batch, String type, String platFrom, String num) {
		String intTemp = batch + type + num;
		String temp = Integer.toHexString(Integer.parseInt(intTemp));
		return (platFrom + temp).toLowerCase();
	}

	private String cdkID() {
		String clsName = SysConstants.cdkIDEntity;
		long cdkID = keyGeneratorDAO.updateInc(clsName);
		return cdkID + "";
	}

	public String cdkTypeID() {
		String clsName = SysConstants.CDKTypeIDEntity;
		long cdkTypeID = keyGeneratorDAO.updateInc(clsName);
		return cdkTypeID + "";
	}

	public String cdkRewardID() {
		String clsName = SysConstants.CDKRewardIDEntity;
		long cdkRewardID = keyGeneratorDAO.updateInc(clsName);
		return cdkRewardID + "";
	}

	private void asynInsertCDK(int num, String batch, String type, String platFrom, String rewardID, String startTime, String endTime) {
		Timer timer = new Timer();
		CDKTask cdkTask = new CDKTask(num, batch, type, platFrom, rewardID, startTime, endTime);
		timer.schedule(cdkTask, 100);
	}

	private boolean hasCDKReward() {
		List<CDKRewardEntity> list = CDKRewardDao.getRewardEntitys();
		if (list == null || list.size() == 0) {
			return false;
		}
		return true;
	}

	private boolean hasCDKType() {
		List<CDKTypeEntity> list = CDKTypeDao.getTypeEntitys();
		if (list == null || list.size() == 0) {
			return false;
		}
		return true;
	}

	public CDKTypeEntity getCdkTypeEntity(String typeID) {
		return CDKTypeDao.getEntity(typeID);
	}

	public void saveCDKTypeEntity(CDKTypeEntity entity) {
		CDKTypeDao.save(entity);
	}

	public CDKRewardEntity getCdkRewardEntity(String rewardID) {
		return CDKRewardDao.getEntity(rewardID);
	}

	public void saveCDKRewardEntity(CDKRewardEntity entity) {
		CDKRewardDao.save(entity);
	}

	public PlayerCDKEntity getPlayerCDKEntity(String playerID) {
		return playerCDKDao.getPlayerCDKEntity(playerID);
	}

	public void savePlayerCDKEntity(PlayerCDKEntity entity) {
		playerCDKDao.save(entity);
	}

	public void saveCDKEntity(CDKEntity entity) {
		CDKDao.save(entity);
	}

	public CDKEntity getCDKEntity(String cdk) {
		return CDKDao.getCdkEntityByCdk(cdk);
	}

	public String sendRequest(String address, Map<String, String> params) {
		String result = "";
		try {
			URL url;
			String requestUrl = getRequestUrl(address, params);
			System.out.println(requestUrl);
			url = new URL(requestUrl);
			URLConnection conn = url.openConnection();
			// 超时设置500毫秒
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String line;
			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	private String getRequestUrl(String address, Map<String, String> params) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append(address);
		Set<Entry<String, String>> set = params.entrySet();
		int i = 0;
		for (Entry<String, String> entry : set) {
			if (i != 0) {
				sb.append("&");
			}
			sb.append(entry.getKey());
			sb.append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			i++;
		}
		logger.debug("send request:" + sb.toString());
		return sb.toString();
	}

	private String getAddressByType() {
		String address = "";
		if (servertype != null) {
			servertype = servertype.toLowerCase();
		}
		switch (servertype) {
		case "android":
			address = "http://login-android.millergame.net/browserGame/json.do?";
			break;
		case "ios":
			address = "http://login-ios.millergame.net/browserGame/json.do?";
			break;
		case "appstore":
			address = "http://login-appstore.millergame.net/browserGame/json.do?";
			break;
		default:
			break;
		}
		return address;
	}

	/**
	 * 初始化订单ID
	 * */
	private void initCDKID() {
		String clsName = SysConstants.CDKIDEntity;
		KeyGenerator keyGenerator = keyGeneratorDAO.getEntity(clsName);
		if (keyGenerator == null) {
			keyGenerator = new KeyGenerator();
			keyGenerator.setKey(clsName);
			keyGenerator.setNextId(1);
			keyGeneratorDAO.save(keyGenerator);
		}
	}

	/**
	 * 获取订单ID
	 * */
	public String CDKID() {
		String clsName = SysConstants.CDKIDEntity;
		long orderID = keyGeneratorDAO.updateInc(clsName);
		return orderID + "";
	}

	private static File[] getCdkFiles() {
		String path = TemplateManageHandler.class.getResource("/").getPath().split("WEB-INF")[0];
		File cdkDir = new File(path + "/cdk/");
		File[] files = cdkDir.listFiles();
		return files;
	}

	public void loadCDK() throws IOException {
		Map<String, String> rewardMap = new HashMap<String, String>();
		rewardMap.put("1", "1");
		rewardMap.put("2", "1");
		rewardMap.put("3", "3");
		rewardMap.put("4", "4");
		rewardMap.put("5", "5");
		rewardMap.put("6", "6");
		rewardMap.put("7", "7");
		rewardMap.put("8", "8");
		rewardMap.put("9", "9");
		rewardMap.put("10", "10");
		rewardMap.put("11", "13");
		rewardMap.put("12", "14");
		rewardMap.put("13", "11");
		rewardMap.put("14", "12");
		rewardMap.put("15", "15");
		rewardMap.put("16", "16");
		rewardMap.put("17", "12");
		rewardMap.put("18", "12");
		File[] files = getCdkFiles();
		for (File file : files) {
			logger.error("开始load  " + file.getName());
			long nowTime = System.currentTimeMillis();
			String[] temps = file.getName().split("_");
			String typeID = temps[1];
			String batch = temps[2].split("\\.")[0];
			String rewardID = rewardMap.get(batch);

			InputStreamReader in = new InputStreamReader(new FileInputStream(file));
			BufferedReader br = new BufferedReader(in);
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				CDKEntity entity = new CDKEntity();
				entity.setKey(CDKID());
				entity.setBatch(batch);
				entity.setCdk(line);
				entity.setTypeID(typeID);
				entity.setRewardID(rewardID);
				entity.setCreateTime(System.currentTimeMillis());
				saveCDKEntity(entity);
				i++;
			}
			br.close();
			logger.error("load  " + file.getName() + " , 完成，共计" + i + ",耗时" + (System.currentTimeMillis() - nowTime) + "ms");
		}
	}

}
