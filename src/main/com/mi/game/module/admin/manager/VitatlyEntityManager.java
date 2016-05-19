package com.mi.game.module.admin.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.vitatly.dao.VitatlyDAO;
import com.mi.game.module.vitatly.pojo.VitatlyEntity;

public class VitatlyEntityManager extends BaseEntityManager<VitatlyEntity> {

	public VitatlyEntityManager() {
		this.dao = VitatlyDAO.getInstance();
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String playerID = (String) ioMessage.getInputParse("playerID");
		VitatlyEntity entity = dao.getEntity(playerID);
		if (entity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}
		String vitatly = (String) ioMessage.getInputParse("vitatly");
		String energy = (String) ioMessage.getInputParse("energy");
		String maxEnergy = (String) ioMessage.getInputParse("maxEnergy");
		String maxVitatly = (String) ioMessage.getInputParse("maxVitatly");
		String lastUpdateVitatlyTime = (String) ioMessage.getInputParse("lastUpdateVitatlyTime");
		String lastUpdateEnergyTime = (String) ioMessage.getInputParse("lastUpdateEnergyTime");
		if(StringUtils.isNotBlank(vitatly)){
			entity.setVitatly(Long.parseLong(vitatly));
		}
		if(StringUtils.isNotBlank(energy)){
			entity.setEnergy(Long.parseLong(energy));
		}
		if(StringUtils.isNotBlank(maxEnergy)){
			entity.setMaxEnergy(Long.parseLong(maxEnergy));
		}
		if(StringUtils.isNotBlank(maxVitatly)){
			entity.setMaxVitatly(Long.parseLong(maxVitatly));
		}
		if(StringUtils.isNotBlank(lastUpdateVitatlyTime)){
			entity.setLastUpdateVitatlyTime(Long.parseLong(lastUpdateVitatlyTime));
		}
		if(StringUtils.isNotBlank(lastUpdateEnergyTime)){
			entity.setLastUpdateEnergyTime(Long.parseLong(lastUpdateEnergyTime));
		}
		dao.save(entity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

	@Override
	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
		String playerID = (String) ioMessage.getInputParse("playerID");
		if (StringUtils.isNotBlank(playerID)) {
			queryInfo.addQueryCondition("playerID", playerID);
		}
		return dao.queryPage(queryInfo);
	}
}
