package com.mi.game.module.admin.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.legion.dao.LegionEntityDAO;
import com.mi.game.module.legion.pojo.LegionEntity;
import com.mi.game.module.legion.pojo.LegionGG;
import com.mi.game.module.legion.pojo.LegionHall;
import com.mi.game.module.legion.pojo.LegionShop;

public class LegionEntityManager extends BaseEntityManager<LegionEntity> {
	public LegionEntityManager() {
		this.dao = LegionEntityDAO.getInstance();
	}

	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
		String legionID = (String) ioMessage.getInputParse("legionID");
		String name = (String) ioMessage.getInputParse("name");
		String level = (String) ioMessage.getInputParse("level");
		if (StringUtils.isNotBlank(legionID)) {
			queryInfo.addQueryCondition("legionID", legionID);
		}
		if (StringUtils.isNotBlank(name)) {
			queryInfo.addQueryCondition("name", name);
		}
		if (StringUtils.isNotBlank(level)) {
			queryInfo.addQueryCondition("level", level);
		}
		return dao.queryPage(queryInfo);
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String legionID = (String) ioMessage.getInputParse("legionID");
		LegionEntity legionEntity = dao.getEntity(legionID);
		if (legionEntity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}
		String name = (String) ioMessage.getInputParse("name");
		String declaration = (String) ioMessage.getInputParse("declaration");
		String kill = (String) ioMessage.getInputParse("kill");
		String legatus = (String) ioMessage.getInputParse("legatus");
		String legatusList = (String) ioMessage.getInputParse("legatusList");
		String pwd = (String) ioMessage.getInputParse("pwd");
		String maxNum = (String) ioMessage.getInputParse("maxNum");
		String legionDevote = (String) ioMessage.getInputParse("legionDevote");
		String level = (String) ioMessage.getInputParse("level");
		String notice = (String) ioMessage.getInputParse("notice");
		String legionHall = (String) ioMessage.getInputParse("legionHall");
		String legionShop = (String) ioMessage.getInputParse("legionShop");
		String legiongg = (String) ioMessage.getInputParse("legiongg");
		String members = (String) ioMessage.getInputParse("members");
		String applys = (String) ioMessage.getInputParse("applys");
		if (StringUtils.isNotBlank(name)) {
			legionEntity.setName(name);
		}
		if (StringUtils.isNotBlank(declaration)) {
			legionEntity.setDeclaration(declaration);
		}
		if (StringUtils.isNotBlank(kill)) {
			legionEntity.setKill(Boolean.parseBoolean(kill));
		}
		if (StringUtils.isNotBlank(legatus)) {
			legionEntity.setLegatus(legatus);
		}
		if (StringUtils.isNotBlank(legatusList)) {
			JSONArray array = JSON.parseArray(legatusList);
			List<String> temp = new ArrayList<String>();
			for (int i = 0; i < array.size(); i++) {
				temp.add(array.getString(i));
			}
			legionEntity.setLegatusList(temp);
		}
		if (StringUtils.isNotBlank(pwd)) {
			legionEntity.setPwd(pwd);
		}
		if (StringUtils.isNotBlank(maxNum)) {
			legionEntity.setMaxNum(Integer.parseInt(maxNum));
		}
		if (StringUtils.isNotBlank(legionDevote)) {
			legionEntity.setLegionDevote(Long.parseLong(legionDevote));
		}
		if (StringUtils.isNotBlank(level)) {
			legionEntity.setLevel(Integer.parseInt(level));
		}
		if (StringUtils.isNotBlank(notice)) {
			legionEntity.setNotice(notice);
		}
		if (StringUtils.isNotBlank(legionHall)) {
			LegionHall hall = JSON.parseObject(legionHall, LegionHall.class);
			legionEntity.setLegionHall(hall);
		}
		if (StringUtils.isNotBlank(legionShop)) {
			LegionShop shop = JSON.parseObject(legionShop, LegionShop.class);
			legionEntity.setLegionShop(shop);
		}
		if (StringUtils.isNotBlank(legiongg)) {
			LegionGG gg = JSON.parseObject(legiongg, LegionGG.class);
			legionEntity.setLegiongg(gg);
		}
		if (StringUtils.isNotBlank(members)) {
			JSONArray array = JSON.parseArray(members);
			List<String> temp = new ArrayList<String>();
			for (int i = 0; i < array.size(); i++) {
				temp.add(array.getString(i));
			}
			legionEntity.setMembers(temp);
		}
		if (StringUtils.isNotBlank(applys)) {
			@SuppressWarnings("unchecked")
			Map<String,String> array =JSON.parseObject(applys,HashMap.class);
			legionEntity.setApplys(array);
		}
		dao.save(legionEntity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

}
