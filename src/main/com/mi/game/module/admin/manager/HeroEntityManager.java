package com.mi.game.module.admin.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.hero.dao.HeroDAO;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.hero.pojo.HeroEntity;
import com.mi.game.module.hero.pojo.HeroShard;

public class HeroEntityManager extends BaseEntityManager<HeroEntity> {
	public HeroEntityManager() {
		this.dao = HeroDAO.getInstance();
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
		HeroEntity entity = dao.getEntity(playerID);
		if (entity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}

		String maxHeroNum = (String) ioMessage.getInputParse("maxHeroNum");
		String teamList = (String) ioMessage.getInputParse("teamList");
		String heroMap = (String) ioMessage.getInputParse("heroMap");
		String shardMap = (String) ioMessage.getInputParse("shardMap");
		if (StringUtils.isNotBlank(maxHeroNum)) {
			entity.setMaxHeroNum(Integer.parseInt(maxHeroNum));
		}
		if (StringUtils.isNotBlank(teamList)) {
			JSONArray json = JSON.parseArray(teamList);
			List<Long> temp = new ArrayList<Long>();
			for (int i = 0; i < json.size(); i++) {
				temp.add(json.getLong(i));
			}
			entity.setTeamList(temp);
		}
		if (StringUtils.isNotBlank(heroMap)) {
			JSONObject json = JSON.parseObject(heroMap);
			Map<String, Hero> temp = new HashMap<String, Hero>();
			Set<Entry<String, Object>> set = json.entrySet();
			for (Entry<String, Object> entry : set) {
				String key = entry.getKey();
				String value = entry.getValue().toString();
				Hero hero = JSON.parseObject(value, Hero.class);
				temp.put(key, hero);
			}
			entity.setHeroMap(temp);
		}
		if (StringUtils.isNotBlank(shardMap)) {
			JSONObject json = JSON.parseObject(shardMap);
			Map<String, HeroShard> temp = new HashMap<String, HeroShard>();
			Set<Entry<String, Object>> set = json.entrySet();
			for (Entry<String, Object> entry : set) {
				String key = entry.getKey();
				String value = entry.getValue().toString();
				HeroShard heroShard = JSON.parseObject(value, HeroShard.class);
				temp.put(key, heroShard);
			}
			entity.setShardMap(temp);
		}
		dao.save(entity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

}
