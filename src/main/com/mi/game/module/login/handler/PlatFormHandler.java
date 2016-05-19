package com.mi.game.module.login.handler;

import org.apache.commons.lang.StringUtils;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.MinuEntity;
import com.mi.game.module.login.protocol.LoginInfoProtocol;
import com.mi.game.util.Logs;

/**
 * 第三方平台用户登录
 *
 */
@HandlerType(type = HandlerIds.platformLogin)
public class PlatFormHandler extends BaseHandler {
	@Override
	public void execute(IOMessage ioMessage) {
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);

		LoginInfoProtocol protocol = new LoginInfoProtocol();
		ioMessage.setProtocol(protocol);
		String uid = (String) ioMessage.getInputParse("uid");
		String session = (String) ioMessage.getInputParse("session");
		String platform = (String) ioMessage.getInputParse("platform");

		if (StringUtils.isBlank(uid) || StringUtils.isBlank(session) || StringUtils.isBlank(platform)) {
			Logs.logger.error("uid=" + uid + ",session=" + session + ",platform=" + platform);
			protocol.setCode(ErrorIds.UIDEmpty);
			return;
		}
		// 防uid重复 处理
		uid = platform + uid;
		uid = uid.toLowerCase();
		if (!loginModule.chechkUserPasswd(uid, uid, protocol,true)) { // 如果用户不存在
			loginModule.registerUser(uid, uid, null, true);
			// 删档测试记录第三方平台用户id
			MinuEntity minuEntity = loginModule.getMinuEntity(uid);
			if (minuEntity == null) {
				minuEntity = new MinuEntity();
				minuEntity.setMinu_uid(uid);
				minuEntity.setDateTime(System.currentTimeMillis());
				loginModule.saveMinuEntity(minuEntity);
			}
		}
	}
}
