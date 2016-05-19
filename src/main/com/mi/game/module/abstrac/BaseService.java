package com.mi.game.module.abstrac;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;

public abstract class BaseService<T extends AbstractBaseEntity> {
	protected AbstractBaseDAO<T> dao;

	public abstract void initDAO();

	public BaseService() {
		initDAO();
	}

	/**
	 * 获取创建视图的newEntity信息
	 * 
	 * @param uid
	 * @return
	 */
	public T newEntity() {
		return null;
	}

	/**
	 * 创建一个与玩家uid相关的实体
	 * 
	 * @param uid
	 * @return
	 */
	public abstract T newEntity(long uid);

	public T newEntityWithParse(Object... parse) {
		return null;
	}

	public void save(T entity) {
		dao.save(entity);
	}

	public void del(T entity) {
		dao.del(entity);
	}

	public T query(QueryInfo query) {
		return dao.query(query);
	}

	public long queryCount(QueryInfo query) {
		return dao.queryCount(query);
	}

	public List<T> queryList(QueryInfo query) {
		return dao.queryList(query);
	}

	public List<T> queryPage(QueryInfo query) {
		return dao.queryPage(query);
	}

	public T getEntity(String entityId) {
		return this.dao.getEntity(entityId);
	}

	public boolean delEntity(String entityId) {
		return this.dao.getEntity(entityId) != null;
	}
}
