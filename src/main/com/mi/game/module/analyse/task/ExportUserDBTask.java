package com.mi.game.module.analyse.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.TimerTask;
import java.util.zip.GZIPOutputStream;

import com.mi.core.engine.ModuleManager;
import com.mi.core.util.ConfigUtil;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.analyse.pojo.AnalyEntity;
import com.mi.game.module.friend.FriendModule;
import com.mi.game.module.friend.pojo.FriendEntity;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.vip.VipModule;
import com.mi.game.module.vip.pojo.VipEntity;
import com.mi.game.module.wallet.WalletModule;
import com.mi.game.module.wallet.pojo.WalletEntity;
import com.mi.game.util.Logs;

public class ExportUserDBTask extends TimerTask {

	private static LoginModule loginModule;
	private static VipModule vipModule;
	private static HeroModule heroModule;
	private static WalletModule walletModule;
	private static FriendModule friendModule;
	private static String path = ConfigUtil.getString("analyse.export.path", "/data/stata/db/");
	private static String fileName = ConfigUtil.getString("analyse.export.user", "hgll_Android-1_user_");
	public static final int BUFFER = 1024;
	public static final String EXT = ".gz";

	@Override
	public void run() {
		try {
			initModule();
			Logs.logger.info("用户数据开始导出");
			long nowTime = System.currentTimeMillis();
			AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
			List<AnalyEntity> allAnaly = analyseModule.getAllAnalyEntity();
			String dateTime = DateTimeUtil.getStringDate("yyyy-MM-dd");
			File exportPath = new File(path);
			if (!exportPath.isDirectory()) {
				exportPath.mkdirs();
			}
			File outFile = new File(path + fileName + dateTime + ".sql");
			if (outFile.isFile()) {
				Logs.logger.info("文件已导出!");
				return;
			}
			FileWriter fw = new FileWriter(outFile);
			outFile.createNewFile();
			for (AnalyEntity analyEntity : allAnaly) {
				String playerID = analyEntity.getPlayer_id();
				Hero hero = heroModule.getLead(playerID, null);
				VipEntity vipEntity = vipModule.initVipEntity(playerID);
				PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
				WalletEntity walletEntity = walletModule.getwalletEntity(playerID);
				FriendEntity friendEntity = friendModule.getFriendEntity(playerID);
				//角色名
				analyEntity.setRole_name(playerEntity.getNickName());
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

				fw.write(analyEntity.toString() + "\n");
			}
			fw.close();
			// 开始压缩
			// compress(outFile, true);
			Logs.logger.info("导出用户数据:" + (System.currentTimeMillis() - nowTime) + "ms");
		} catch (Exception e) {
			e.printStackTrace();
			Logs.logger.info("db导出文件出错!");
		}

	}

	public static void compress(File file, boolean delete) throws Exception {
		FileInputStream fis = new FileInputStream(file);
		FileOutputStream fos = new FileOutputStream(file.getPath() + EXT);
		compress(fis, fos);
		fis.close();
		fos.flush();
		fos.close();
		if (delete) {
			file.delete();
		}
	}

	private static void compress(InputStream is, OutputStream os) throws Exception {
		GZIPOutputStream gos = new GZIPOutputStream(os);
		int count;
		byte data[] = new byte[BUFFER];
		while ((count = is.read(data, 0, BUFFER)) != -1) {
			gos.write(data, 0, count);
		}
		gos.finish();
		gos.flush();
		gos.close();
	}

	private void initModule() {
		loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		vipModule = ModuleManager.getModule(ModuleNames.VipModule, VipModule.class);
		heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		walletModule = ModuleManager.getModule(ModuleNames.WalletModule, WalletModule.class);
		friendModule = ModuleManager.getModule(ModuleNames.FriendMoudle, FriendModule.class);
	}
}
