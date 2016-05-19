package com.mi.game.module.analyse.handler;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.analyse.protocol.AnalyProtocol;
import com.mi.game.module.base.handler.BaseHandler;

@HandlerType(type = HandlerIds.CLIENT_ANALY_DEVICE)
public class DeviceAnalyHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		AnalyProtocol protocol = new AnalyProtocol();
		String device_id = (String) ioMessage.getInputParse("device_id");
		if (!StringUtils.isEmpty(device_id) && device_id.indexOf("unknown") != -1) {
			device_id = UUID.randomUUID().toString();
		}
		String phonetype = (String) ioMessage.getInputParse("phonetype");
		String phonemodel = (String) ioMessage.getInputParse("phonemodel");
		if (!StringUtils.isEmpty(phonetype) && device_id.indexOf("unknown") != -1) {
			phonetype = phonemodel;
		}
		String platform = (String) ioMessage.getInputParse("platform");
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.deviceAnaly(device_id, phonetype, platform,protocol);
		ioMessage.setOutputResult(protocol);
	}

}
