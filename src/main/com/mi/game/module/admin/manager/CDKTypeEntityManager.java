package com.mi.game.module.admin.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.cdk.CDKModule;
import com.mi.game.module.cdk.dao.CDKTypeEntityDao;
import com.mi.game.module.cdk.pojo.CDKTypeEntity;

public class CDKTypeEntityManager extends BaseEntityManager<CDKTypeEntity> {

	public CDKTypeEntityManager() {
		this.dao = CDKTypeEntityDao.getInstance();
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		CDKModule cdkModule = ModuleManager.getModule(ModuleNames.CDKModule, CDKModule.class);
		String typeID = (String) ioMessage.getInputParse("typeID");
		CDKTypeEntity entity = dao.getEntity(typeID);
		if (entity == null) {
			entity = new CDKTypeEntity();
			entity.setTypeID(cdkModule.cdkTypeID());
		}
		String num = (String) ioMessage.getInputParse("num");
		if (StringUtils.isNotEmpty(num)) {
			entity.setNum(Integer.parseInt(num));
		}
		String pnum = (String) ioMessage.getInputParse("pnum");
		if (StringUtils.isNotEmpty(pnum)) {
			entity.setPnum(Integer.parseInt(pnum));
		}
		String desc = (String) ioMessage.getInputParse("desc");
		if (StringUtils.isNotEmpty(desc)) {
			entity.setDesc(desc);
		}
		dao.save(entity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

	@Override
	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
		queryInfo.setOrder("-typeID");
		String rewardID = (String) ioMessage.getInputParse("typeID");
		if (StringUtils.isNotEmpty(rewardID)) {
			queryInfo.addQueryCondition("typeID", rewardID);
		}
		return dao.queryPage(queryInfo);
	}
}
