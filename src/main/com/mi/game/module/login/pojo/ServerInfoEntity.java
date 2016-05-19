package com.mi.game.module.login.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;

/**
 * 服务器信息
 * 
 */

@Entity(noClassnameStored = true)
public class ServerInfoEntity extends BaseEntity {
	private static final long serialVersionUID = 6168285275515237725L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private Long serverID; // 服务器ID
	private String serverName; // 服务器名称
	private String url; // 服务器地址
	private int status; // 服务器状态 0 新 ,1 热, 2满 ,3停
	private String message;

	public ServerInfoEntity() {

	}

	public ServerInfoEntity(Long serverID, String serverName, String url, int status) {
		this.serverID = serverID;
		this.serverName = serverName;
		this.url = url;
		this.status = status;
	}

	public ServerInfoEntity(Long serverID, String serverName, String url, int status, int recommend, String message) {
		this.serverID = serverID;
		this.serverName = serverName;
		this.url = url;
		this.status = status;
		this.message = message;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("serverID", serverID);
		data.put("serverName", serverName);
		data.put("url", url);
		data.put("status", status);
		data.put("message", message);
		return data;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("serverID", serverID);
		data.put("serverName", serverName);
		data.put("url", url);
		data.put("status", status);
		data.put("message", message);
		return data;
	}

	/*
	 * (non-Javadoc)
	 * @see com.mi.core.pojo.BaseEntity#getKey()
	 */
	@Override
	public Object getKey() {
		return serverID;
	}

	/*
	 * (non-Javadoc)
	 * @see com.mi.core.pojo.BaseEntity#getKeyName()
	 */
	@Override
	public String getKeyName() {
		return "serverID";
	}

	@Override
	public void setKey(Object key) {
		if (key != null) {
			serverID = Long.parseLong(key.toString());
		}
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getServerID() {
		return serverID;
	}

	public void setServerID(Long serverID) {
		this.serverID = serverID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return this.responseMap().toString();
	}

}
