package com.mi.game.module.analyse.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.analyse.protocol.AnalyProtocol;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.pay.define.PayConstants;
import com.mi.game.module.pay.define.PlatFromConstants;

@HandlerType(type = HandlerIds.ANALY_INFO)
public class AnalyHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		// 平台id
		String puid = (String) ioMessage.getInputParse("puid");
		// 平台性别
		int sex = (Integer) ioMessage.getInputParse("sex");
		// 设备唯一id
		String device_id = (String) ioMessage.getInputParse("device_id");
		// 操作系统版本
		String osversion = (String) ioMessage.getInputParse("osversion");
		// 设备型号
		String phonetype = (String) ioMessage.getInputParse("phonetype");
		// 设备imei
		String imei = (String) ioMessage.getInputParse("imei");
		// 设备mac地址
		String mac = (String) ioMessage.getInputParse("mac");
		// 渠道名称
		String store = (String) ioMessage.getInputParse("store");
		// 分服标示
		String server = (String) ioMessage.getInputParse("server");
		// 操作系统类型
		String os = (String) ioMessage.getInputParse("os");
		// 游戏标记
		String gameid = (String) ioMessage.getInputParse("gameid");
		// ip
		String ip = ioMessage.getTransmitter().getRequest().getRemoteAddr();
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		AnalyProtocol protocol = new AnalyProtocol();

		// 强制小写
		store = store.toLowerCase();
		switch (store) {
		case PayConstants.PLATFORM_AISI:
			store = PlatFromConstants.PLATFORM_AISI;
			break;
		case PayConstants.PLATFORM_91:
			store = PlatFromConstants.PLATFORM_91;
			break;
		case PayConstants.PLATFORM_HAIMA:
			store = PlatFromConstants.PLATFORM_HAIMA;
			break;
		case PayConstants.PLATFORM_IAPPLE:
			store = PlatFromConstants.PLATFORM_IAPPLE;
			break;
		case PayConstants.PLATFORM_ITOOLS:
			store = PlatFromConstants.PLATFORM_ITOOLS;
			break;
		case PayConstants.PLATFORM_KUAIYONG:
			store = PlatFromConstants.PLATFORM_KUAIYONG;
			break;
		case PayConstants.PLATFORM_PPZHUSHOU:
			store = PlatFromConstants.PLATFORM_PPZHUSHOU;
			break;
		case PayConstants.PLATFORM_TONGBUTUI:
			store = PlatFromConstants.PLATFORM_TONGBUTUI;
			break;
		case PayConstants.PLATFORM_XY:
			store = PlatFromConstants.PLATFORM_XY;
			break;
		case PayConstants.PLATFORM_BAIDU:
			store = PlatFromConstants.PLATFORM_BAIDU;
			break;
		case PayConstants.PLATFORM_COOLPAD:
			store = PlatFromConstants.PLATFORM_COOLPAD;
			break;
		case PayConstants.PLATFORM_DANGLE:
			store = PlatFromConstants.PLATFORM_DANGLE;
			break;
		case PayConstants.PLATFORM_DANGLE2:
			store = PlatFromConstants.PLATFORM_DANGLE;
			break;
		case PayConstants.PLATFORM_HUAWEI:
			store = PlatFromConstants.PLATFORM_HUAWEI;
			break;
		case PayConstants.PLATFORM_JINLI:
			store = PlatFromConstants.PLATFORM_JINLI;
			break;
		case PayConstants.PLATFORM_LENOVO:
			store = PlatFromConstants.PLATFORM_LENOVO;
			break;
		case PayConstants.PLATFORM_MUZHIWAN:
			store = PlatFromConstants.PLATFORM_MUZHIWAN;
			break;
		case PayConstants.PLATFORM_OPPO:
			store = PlatFromConstants.PLATFORM_OPPO;
			break;
		case PayConstants.PLATFORM_PPS:
			store = PlatFromConstants.PLATFORM_PPS;
			break;
		case PayConstants.PLATFORM_360:
			store = PlatFromConstants.PLATFORM_360;
			break;
		case PayConstants.PLATFORM_UC:
			store = PlatFromConstants.PLATFORM_UC;
			break;
		case PayConstants.PLATFORM_VIVO:
			store = PlatFromConstants.PLATFORM_VIVO;
			break;
		case PayConstants.PLATFORM_37WAN:
			store = PlatFromConstants.PLATFORM_37WAN;
			break;
		case PayConstants.PLATFORM_WDJ:
			store = PlatFromConstants.PLATFORM_WDJ;
			break;
		case PayConstants.PLATFORM_WDJ2:
			store = PlatFromConstants.PLATFORM_WDJ;
			break;
		case PayConstants.PLATFORM_XIAOMI:
			store = PlatFromConstants.PLATFORM_XIAOMI;
			break;
		case PayConstants.PLATFORM_YYH:
			store = PlatFromConstants.PLATFORM_YYH;
			break;
		case PayConstants.PLATFORM_YYH2:
			store = PlatFromConstants.PLATFORM_YYH;
			break;
		case PayConstants.PLATFORM_ANZHI:
			store = PlatFromConstants.PLATFORM_ANZHI;
			break;
		case PayConstants.PLATFORM_YOUAI:
			store = PlatFromConstants.PLATFORM_YOUAI;
			break;
		case PayConstants.PLATFORM_TENCENT:
			store = PlatFromConstants.PLATFORM_TENCENT;
			break;
		}
		analyseModule.analyInfo(playerID, puid, sex, device_id, osversion, phonetype, imei, mac, store, server, os, gameid, ip, protocol);
		ioMessage.setOutputResult(protocol);
	}

}
