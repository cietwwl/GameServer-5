package com.mi.game.module.playerservice;

import java.util.List;

import com.mi.core.dao.KeyGeneratorDAO;
import com.mi.core.engine.annotation.Module;
import com.mi.core.pojo.KeyGenerator;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.playerservice.dao.SuggestEntityDAO;
import com.mi.game.module.playerservice.pojo.SuggestEntity;
@Module(name = ModuleNames.PlayerServiceModule,clazz = PlayerServiceModule.class)
public class PlayerServiceModule extends BaseModule{
	private final KeyGeneratorDAO keyGeneratorDAO = KeyGeneratorDAO.getInstance();
	private final SuggestEntityDAO suggestEntityDAO = SuggestEntityDAO.getInstance();
	@Override
	public void init(){
		initSuggestID();
	}
	
	/**初始化客服信息ID*/
	private  void initSuggestID(){
		String clsName = SysConstants.suggestIDEntity;
		KeyGenerator keyGenerator = keyGeneratorDAO.getEntity(clsName);
		if(keyGenerator == null){
			keyGenerator = new KeyGenerator();
			keyGenerator.setKey(clsName);
			keyGenerator.setNextId(SysConstants.suggestStartID);
			keyGeneratorDAO.save(keyGenerator);
		}
	}
	
	/**
	 * 获取唯一ID
	 * */
	private long getSuggestID(){
		String clsName = SysConstants.suggestIDEntity;
		long suggestID = keyGeneratorDAO.updateInc(clsName);
		return suggestID;
	}
	
	/**
	 * 保存实体
	 * */
	public void saveEntity(SuggestEntity suggestEntity){
		suggestEntityDAO.save(suggestEntity);
	}
	
	/**
	 * 获取未处理的实体
	 * */
	public List<SuggestEntity> getUnreadEntity(int page){
		return suggestEntityDAO.getUnreadSuggestList(page);
	}
	
	/**
	 * 保存信息
	 * */
	public void saveSuggestInfo(String playerID, String message, int suggestType){
		if(suggestType > 4 || suggestType < 1){
			logger.error("错误的客服信息类型");
			throw new IllegalArgumentException(ErrorIds.WrongSuggestType + "");
		}
		long suggestID = this.getSuggestID();
		SuggestEntity suggestEntity = new SuggestEntity();
		suggestEntity.setKey(suggestID + "");
		suggestEntity.setMessage(message);
		suggestEntity.setType(suggestType);
		suggestEntity.setPlayerID(playerID);
		suggestEntity.setTime(System.currentTimeMillis());
		this.saveEntity(suggestEntity);
	}
	
	/**
	 * 查询未读的客服信息
	 * */
	public List<SuggestEntity> getUnreadMessage(int page){
		//return this.getUnreadEntity();
		return null;
	}
	
}
