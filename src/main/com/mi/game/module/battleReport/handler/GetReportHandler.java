package com.mi.game.module.battleReport.handler;

import java.io.IOException;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.battleReport.BattleReportModule;
import com.mi.game.module.battleReport.pojo.BattleReportEntity;
import com.mi.game.module.battleReport.protocol.BattleReportProtocol;
import com.mi.game.util.Base64Coder;
import com.mi.game.util.GZIPUtil;

@HandlerType(type = HandlerIds.getBattleReportHandler)
public class GetReportHandler extends BaseHandler {
	@Override
	public void execute(IOMessage ioMessage) {
		long reportID = 0;
		if (ioMessage.getInputParse("reportID") != null) {
			reportID = Long.parseLong(ioMessage.getInputParse("reportID").toString());
		}
		BattleReportModule battleReportModule = ModuleManager.getModule(ModuleNames.BattleReportModule, BattleReportModule.class);
		BattleReportEntity entity = battleReportModule.getReportEntity(reportID + "");
		BattleReportProtocol protocol = new BattleReportProtocol();
		String battleString = "";
		// 解码
		byte[] base64 = Base64Coder.decode(entity.getBattleString());
		// 解压缩
		try {
			battleString = GZIPUtil.uncompress(base64);
		} catch (IOException e) {
			logger.error("战斗录像解压错误!");
		}
		protocol.setBattleString(battleString);
		ioMessage.setProtocol(protocol);
	}
}
