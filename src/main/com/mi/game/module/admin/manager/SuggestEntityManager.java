package com.mi.game.module.admin.manager;

import java.util.ArrayList;
import java.util.List;





import java.util.Map;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.cache.bean.QueryBean;
import com.mi.core.engine.IOMessage;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.SuggestStatus;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.playerservice.dao.SuggestEntityDAO;
import com.mi.game.module.playerservice.pojo.SuggestEntity;


public class SuggestEntityManager extends BaseEntityManager<SuggestEntity>{
	public SuggestEntityManager() {
		this.dao = SuggestEntityDAO.getInstance();
	}
	
	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
		int status = 0;
		if(ioMessage.getInputParse("status") != null){
			status = Integer.parseInt(ioMessage.getInputParse("status").toString());
		}
		
		if(status != 9){
			QueryBean queryBean = new QueryBean("status", QueryType.EQUAL, status);
			queryInfo.addQueryBean(queryBean);
		}else{
			QueryBean queryBean = new QueryBean("status", QueryType.NOT_EQUAL, SuggestStatus.del);
			queryInfo.addQueryBean(queryBean);
		}
		
		queryInfo.setOrder("-time");
		List<SuggestEntity> list = dao.queryPage(queryInfo);
		for(SuggestEntity suggestEntity : list ){
			suggestEntity.setShowStatus(status);
		}
		return list;
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		// TODO 自动生成的方法存根
		int changeType = 0 ;
		int result = ResponseResult.OK;
		String suggestID = ioMessage.getInputParse("suggestID").toString();
		if(suggestID != null){
			SuggestEntity suggestEntity = this.dao.getEntity(suggestID);
			if(ioMessage.getInputParse("changeType") != null ){
				changeType = Integer.parseInt(ioMessage.getInputParse("changeType").toString());
			}
			if(changeType == 1){
				suggestEntity.setStatus(SuggestStatus.inHand);
			}else
			if(changeType == 2){
				suggestEntity.setStatus(SuggestStatus.solve);
			}else
			if(changeType == 3){
				suggestEntity.setStatus(SuggestStatus.del);
			}else{
				result = ResponseResult.ERROR;
			}
			this.dao.save(suggestEntity);
			BaseAdminProtocol protocol = new BaseAdminProtocol();
			protocol.put("result", result);
			protocol.put("code", 1);
			QueryInfo queryInfo = new QueryInfo();
			List<? extends BaseEntity> list = this.doQueryList(queryInfo, ioMessage);
			if(list != null){
				protocol.put("entityList", getResponseEntityArray(list, 11111));
			}
			ioMessage.setOutputResult(protocol);
		}else {
			this.writeErrorResult(ioMessage, ErrorIds.update_entity_error);
			return;
		}
	}
	
	public static List<Map<String, Object>> getResponseEntityArray(List<? extends BaseEntity> entityList, int type) {
		List<Map<String, Object>> responseMapArr = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < entityList.size(); i++) {
			BaseEntity entity = entityList.get(i);
			responseMapArr.add(entity.responseMap(type));
		}
		return responseMapArr;
	}
}
