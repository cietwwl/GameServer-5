package com.mi.game.module.cdk.task;

import java.util.TimerTask;

import com.mi.core.engine.ModuleManager;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.cdk.CDKModule;
import com.mi.game.module.cdk.pojo.CDKEntity;
import com.mi.game.util.Logs;

public class CDKTask extends TimerTask {

	int num;
	String batch;
	String type;
	String platFrom;
	String rewardID;
	String startTime;
	String endTime;

	public CDKTask(int num, String batch, String type, String platFrom, String rewardID, String startTime, String endTime) {
		this.num = num;
		this.batch = batch;
		this.type = type;
		this.platFrom = platFrom;
		this.rewardID = rewardID;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	private String CDK(String batch, String type, String platFrom, String num) {
		String intTemp = batch + type + num;
		String temp = Integer.toHexString(Integer.parseInt(intTemp));
		return (platFrom + temp).toLowerCase();
	}

	@Override
	public void run() {
		Logs.logger.error(batch + "cdk入库中");
		CDKModule cdkModule = ModuleManager.getModule(ModuleNames.CDKModule, CDKModule.class);
		long time = System.currentTimeMillis();
		for (int i = 0; i < num; i++) {
			CDKEntity cdk = new CDKEntity();
			cdk.setKey(cdkModule.CDKID());
			cdk.setBatch(batch);
			cdk.setTypeID(type);
			cdk.setPlatFrom(platFrom);
			cdk.setRewardID(rewardID);
			cdk.setCreateTime(System.currentTimeMillis());
			cdk.setUsed(0);
			if (startTime != null && !startTime.isEmpty()) {
				cdk.setStartTime(DateTimeUtil.getDate(startTime).getTime());
			}
			if (endTime != null && !endTime.isEmpty()) {
				cdk.setEndTime(DateTimeUtil.getDate(endTime).getTime());
			}
			String cdkString = CDK(batch, type, platFrom, i + "");
			cdk.setCdk(cdkString);
			cdkModule.saveCDKEntity(cdk);
		}
		Logs.logger.error("cdk入库完成!" + (System.currentTimeMillis() - time) + "ms");
	}

}
