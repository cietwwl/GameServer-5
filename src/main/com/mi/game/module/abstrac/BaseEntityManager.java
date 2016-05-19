package com.mi.game.module.abstrac;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.core.engine.IOMessage;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.defines.ErrorIds;

public abstract class BaseEntityManager<T extends BaseEntity> {
	/**
	 * 返回此Dao所管理的Entity类型
	 */
	protected Class<T> poClass;
	protected String idFieldName;
	protected AbstractBaseDAO<T> dao;

	@SuppressWarnings("unchecked")
	public BaseEntityManager() {
		poClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		idFieldName = com.mi.core.pojo.BaseEntityManager.getEntityKeyName(poClass);
	}

	/**
	 * 执行查询列表
	 * 
	 * @param queryInfo
	 * @param ioMessage
	 * @return
	 */
	public abstract List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage);

	/**
	 * 查询单个实体
	 * 
	 * @param ioMessage
	 * @return
	 */
	public BaseEntity queryEntity(IOMessage ioMessage) {
		String entityId = (String) ioMessage.getInputParse(idFieldName);
		if (StringUtils.isNotBlank(entityId)) {
			return dao.getEntity(entityId);
		}
		return null;
	}

	public T queryLongEntity(IOMessage ioMessage) {
		String entityId = (String) ioMessage.getInputParse(idFieldName);
		if (StringUtils.isNotBlank(entityId)) {
			return dao.getEntity(Long.valueOf(entityId));
		}
		return null;
	}

	/**
	 * 更新实体
	 * 
	 * @param ioMessage
	 * @return
	 */
	public abstract void updateEntity(IOMessage ioMessage);

	/**
	 * 删除实体
	 * 
	 * @param ioMessage
	 * @return
	 */
	public void deleteEntity(IOMessage ioMessage) {
		String entityId = (String) ioMessage.getInputParse(idFieldName);
		if (StringUtils.isNotBlank(entityId)) {
			dao.delEntity(entityId);
		} else {
			this.writeErrorResult(ioMessage, ErrorIds.delete_entity_error);
		}
	}

	public void deleteLongEntity(IOMessage ioMessage) {
		String entityId = (String) ioMessage.getInputParse(idFieldName);
		if (StringUtils.isNotBlank(entityId)) {
			dao.delEntity(Long.valueOf(entityId));
			this.writeOkResult(ioMessage);
		} else {
			this.writeErrorResult(ioMessage, ErrorIds.delete_entity_error);
		}
	}

	public void writeOkResult(IOMessage ioMessage) {
	}

	public void writeOkResult(IOMessage ioMessage, int code) {
	}

	public void writeErrorResult(IOMessage ioMessage, int code) {
	}
}
