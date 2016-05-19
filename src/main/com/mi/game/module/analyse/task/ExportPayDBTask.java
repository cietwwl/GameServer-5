package com.mi.game.module.analyse.task;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.TimerTask;

import com.mi.core.engine.ModuleManager;
import com.mi.core.util.ConfigUtil;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.analyse.pojo.AnalyPayEntity;
import com.mi.game.util.Logs;

public class ExportPayDBTask extends TimerTask {

	private static String path = ConfigUtil.getString("analyse.export.path", "/data/stata/db/");
	private static String fileName = ConfigUtil.getString("analyse.export.pay", "hgll_Android-1_user_");

	@Override
	public void run() {
		try {
			Logs.logger.info("订单数据开始导出");
			long nowTime = System.currentTimeMillis();
			AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
			List<AnalyPayEntity> allAnalyPay = analyseModule.getAllAnalyPayEntity();
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
			for (AnalyPayEntity entity : allAnalyPay) {
				fw.write(entity.toString() + "\n");
			}
			fw.close();
			Logs.logger.info("导出订单数据:" + (System.currentTimeMillis() - nowTime) + "ms");
		} catch (Exception e) {
			e.printStackTrace();
			Logs.logger.info("db导出文件出错!");
		}
	}
}