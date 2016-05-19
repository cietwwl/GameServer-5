package com.mi.game.module.vitatly;

import com.mi.core.engine.annotation.Module;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.vitatly.dao.VitatlyDAO;
import com.mi.game.module.vitatly.pojo.VitatlyEntity;

/**
 * @author 刘凯旋
 *
 *         2014年6月6日 下午5:56:13
 */
@Module(name = ModuleNames.VitatlyModule,clazz = VitatlyModule.class)
public class VitatlyModule extends BaseModule{
	private VitatlyDAO vitatlyDAO = VitatlyDAO.getInstance();
	
	/**
	 * 初始化体力耐力
	 * 
	 * @return
	 */
	public VitatlyEntity initVitatlyEntity(String playerID){
		VitatlyEntity entity = new VitatlyEntity();
		long nowTime = System.currentTimeMillis();
		entity.setVitatly(SysConstants.maxVitatly);
		entity.setEnergy(SysConstants.initEnergy);
		entity.setMaxEnergy(SysConstants.initEnergy);
		entity.setMaxVitatly(SysConstants.maxVitatly);
		entity.setLastUpdateEnergyTime(nowTime);
		entity.setLastUpdateVitatlyTime(nowTime);
		entity.setKey(playerID);
		return entity;
	}
	
	/**
	 * 获取体力实体
	 * */	
	
	public VitatlyEntity getVitatlyEntity(String playerID){
		VitatlyEntity vitatlyEntity= vitatlyDAO.getEntity(playerID);
		if(vitatlyEntity != null){
			this.recoverVitatly(vitatlyEntity);
			this.revoverEnergy(vitatlyEntity);
		}else{
			logger.error("体力实体为空");
			throw new IllegalArgumentException(ErrorIds.NoEntity + "");
		}
		return vitatlyEntity;
	}
	
	/**
	 * 保存体力实体
	 * */
	
	public void saveVitatlyEntity(VitatlyEntity entity){
		vitatlyDAO.save(entity);
	}
	
	/**
	 * 恢复体力
	 * */
	
	public VitatlyEntity  recoverVitatly(VitatlyEntity entity){
		long nowTime = System.currentTimeMillis();
		long vitatly = entity.getVitatly();
		if(vitatly > entity.getMaxVitatly()){
			entity.setLastUpdateVitatlyTime(nowTime);
			return entity;
		}
		long lastUpdateTime = entity.getLastUpdateVitatlyTime();
	
		long diffTime = nowTime - lastUpdateTime;
		long times = 0;
		if(diffTime > SysConstants.recoverVitatlyTime){
			times = (diffTime/SysConstants.recoverVitatlyTime);
		}
		
		vitatly += times;
		if(vitatly > entity.getMaxVitatly()){
			entity.setVitatly(entity.getMaxVitatly());
			entity.setLastUpdateVitatlyTime(nowTime);
		}else{
			entity.setLastUpdateVitatlyTime(lastUpdateTime + times * SysConstants.recoverVitatlyTime);
			entity.setVitatly(vitatly);
		}
		this.saveVitatlyEntity(entity); // 保存到数据库
		return entity;
	}
	
	/**
	 * 恢复耐力
	 * */
	
	public VitatlyEntity revoverEnergy(VitatlyEntity entity){
		long nowTime = System.currentTimeMillis();
		long energy = entity.getEnergy();
		if(energy > entity.getMaxEnergy()){
			entity.setLastUpdateEnergyTime(nowTime);
			return entity;
		}
		long lastUpdateTime = entity.getLastUpdateEnergyTime();
		long diffTime = nowTime - lastUpdateTime;
		long times = 0;
		if(diffTime > SysConstants.recoverEnergyTime){
			times = diffTime/SysConstants.recoverEnergyTime;
		}
	
		long nowEnergy = energy  + times;
		if(nowEnergy > entity.getMaxEnergy()){
			nowEnergy = entity.getMaxEnergy();
			entity.setLastUpdateEnergyTime(nowTime);
		}else{
			entity.setLastUpdateEnergyTime(lastUpdateTime + times * SysConstants.recoverEnergyTime);
		}
		entity.setEnergy(nowEnergy);
		this.saveVitatlyEntity(entity); // 保存到数据库
		return entity;
	}
	
	
	/**
	 * 增加耐力
	 */
	public VitatlyEntity addEnergy (String playerID,int num){
		VitatlyEntity entity = this.getVitatlyEntity(playerID);
		long energy = entity.getEnergy();
		energy += num;
		entity.setEnergy(energy);
		this.saveVitatlyEntity(entity);
		return entity;
	}
	
	/**
	 * 增加体力
	 * */
	public VitatlyEntity addVitatly(String playerID,int num){
		VitatlyEntity entity = this.getVitatlyEntity(playerID);
		long vitatly = entity.getVitatly();
		vitatly += num;
		entity.setVitatly(vitatly);
		this.saveVitatlyEntity(entity);
		return entity;
	}
	
	/**
	 * 扣减体力
	 * */
	public synchronized VitatlyEntity consumeVitatly(String playerID,boolean isSave,long delNum){
		VitatlyEntity entity = this.getVitatlyEntity(playerID);
		long vitatly = entity.getVitatly();
		long nowVitatly = vitatly - delNum;
		if(nowVitatly < 0){
			throw new IllegalArgumentException(ErrorIds.NotEnoughVitatly + "") ;
		}
		entity.setVitatly(nowVitatly);
		if(isSave ){
			this.saveVitatlyEntity(entity);	
		}
		return entity;
	}
	
	/**
	 * 扣减耐力
	 * */
	public synchronized VitatlyEntity consumeEnergy(String playerID, long delNum){
		VitatlyEntity entity = this.getVitatlyEntity(playerID);
		long energy = entity.getEnergy();
		long nowEnergy = energy - delNum;
		if(nowEnergy < 0){
			throw new IllegalArgumentException(ErrorIds.NotEnoughEnergy + "");
		}
		entity.setEnergy(nowEnergy);
		this.saveVitatlyEntity(entity);
		return entity;
	}
}
