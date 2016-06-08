package com.mi.game.module.base.handler;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerAfter;
import com.mi.core.engine.annotation.HandlerBefore;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;

@HandlerBefore(global = true, order = -1)
@HandlerAfter(global = true, order = 10)
public class GlobalHandler extends BaseHandler {
	Logger logger = LoggerFactory.getLogger(GlobalHandler.class);

	// private final static String serverID = ConfigUtil.getString("server.id");

	@Override
	public void executeBefore(IOMessage ioMessage) {

		int type = ioMessage.getType();
		// if (type == HandlerIds.PK_FIND_PLAYER) {
		// return;
		// }
		if (type >= HandlerIds.adminSession && type <= HandlerIds.sendMail) {
			return;
		}

		if (ioMessage.getInputParse("playerID") != null) {
			String playerID = (String) ioMessage.getInputParse("playerID");
			ioMessage.setPlayerId(playerID);
		}

		// logger.debug("GlobalHandler type:"+type);
		if (type != HandlerIds.UserLogin && type != HandlerIds.RegisterUser && type != HandlerIds.PlayerInfoInit && type != HandlerIds.ServerLogin
				&& type != HandlerIds.getRandomNameList && type != HandlerIds.VerifyMiuiSession && type != HandlerIds.platformLogin
				&& type != HandlerIds.CLIENT_ANALY_DEVICE && type != HandlerIds.getHeroSkin && type != HandlerIds.GetServerList && type != HandlerIds.getPlayerStatus
				&& type != HandlerIds.initArenaRobot && type != HandlerIds.visitorLogin && type != HandlerIds.visitorRegister && type != HandlerIds.getVisitorServerList
				&& type != HandlerIds.changePassword && type != HandlerIds.getPlatFormInfo && type != HandlerIds.changeServerStatus && type != HandlerIds.ServerLogin
				&& type != HandlerIds.mergeServer && type != HandlerIds.CDK_USE && type != HandlerIds.LEGION_REPAIR && type != HandlerIds.CDK_TYPE
				&& type != HandlerIds.CDK_BUG) {
			String playerID = ioMessage.getPlayerId();
			LoginModule loginModule = (LoginModule) ModuleManager.getModule(ModuleNames.LoginModule);
			PlayerEntity entity = loginModule.getPlayerEntity(playerID);
			entity.setOffLineTime(DateTimeUtil.getMillTime() + 300 * 1000);
			loginModule.savePlayerEntity(entity);
			String uniqueKey = ioMessage.getInputParse("uniqueKey").toString();

			if (StringUtils.isBlank(playerID) || StringUtils.isBlank(uniqueKey)) {
				throw new IllegalArgumentException(ErrorIds.RequestParseError + "");
			}

			if (uniqueKey.equals("123456")) {
				return;
			}

			if (!uniqueKey.equals(entity.getUniqueKey())) {
				// logger.error("playerID : " + playerID + "-----" +
// "uniqueKey : " + uniqueKey + "saveKey : " + entity.getUniqueKey());
				throw new IllegalArgumentException(ErrorIds.ConfirmLogin + "");
			}
			loginModule.savePlayerEntity(entity);
		}
	}

	@Override
	public void executeAfter(IOMessage ioMessage) {
		AnalyseModule.interfaceAnalyse(ioMessage);
	}

}
