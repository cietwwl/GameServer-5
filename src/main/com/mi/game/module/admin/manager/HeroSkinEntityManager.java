package com.mi.game.module.admin.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.hero.dao.HeroSkinEntityDAO;
import com.mi.game.module.hero.pojo.HeroSkinEntity;

public class HeroSkinEntityManager extends BaseEntityManager<HeroSkinEntity> {
	public HeroSkinEntityManager() {
		this.dao = HeroSkinEntityDAO.getInstance();
	}

	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
		String playerID = (String) ioMessage.getInputParse("playerID");
		if (StringUtils.isNotBlank(playerID)) {
			queryInfo.addQueryCondition("playerID", playerID);
		}
		return dao.queryPage(queryInfo);
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String playerID = (String) ioMessage.getInputParse("playerID");
		HeroSkinEntity entity = dao.getEntity(playerID);
		if (entity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}
		String heroSkin = (String) ioMessage.getInputParse("heroSkin");
		if (StringUtils.isNotBlank(heroSkin)) {
			JSONObject json = JSON.parseObject(heroSkin);
			Map<String, Integer> temp = new HashMap<String, Integer>();
			Set<Entry<String, Object>> set = json.entrySet();
			for (Entry<String, Object> entry : set) {
				String key = entry.getKey();
				String value = entry.getValue().toString();
				temp.put(key, Integer.parseInt(value));
			}
			entity.setHeroSkin(temp);
		}
		String allHeroSkin = (String) ioMessage.getInputParse("allHeroSkin");
		if (StringUtils.isNotBlank(allHeroSkin)) {
			JSONObject json = JSON.parseObject(allHeroSkin);
			Map<String, List<Integer>> temp = new HashMap<String, List<Integer>>();
			Set<Entry<String, Object>> set = json.entrySet();
			for (Entry<String, Object> entry : set) {
				String key = entry.getKey();
				List<Object> temp2 = json.getJSONArray(key);
				List<Integer> temp3 = new ArrayList<Integer>();
				for (Object o : temp2) {
					temp3.add(Integer.parseInt(o.toString()));
				}
				temp.put(key, temp3);
			}
			entity.setAllHeroSkin(temp);
		}
		dao.save(entity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

}
