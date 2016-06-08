package com.mi.game.module.login.pojo;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class LoginInfoEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8701911249753791741L;
	@Indexed(value = IndexDirection.ASC, unique = true)	
	private String playerID;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String playerName;
	private String password;
	private String uniqueKey;
	@Indexed(value = IndexDirection.ASC)
	private String visitorPlayerName;
	private long visitorID;
	private boolean bind;
	@Indexed(value = IndexDirection.ASC)
	private String email;
	
	/**
	 * 注册时间
	 */
	private long registerTime;
	
	/**
	 * 最后登录时间
	 */
	private long lastLoginTime;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isBind() {
		return bind;
	}

	public void setBind(boolean bind) {
		this.bind = bind;
	}

	public long getVisitorID() {
		return visitorID;
	}
	
	public void setVisitorID(long visitorID) {
		this.visitorID = visitorID;
	}

	public String getVisitorPlayerName() {
		return visitorPlayerName;
	}

	public void setVisitorPlayerName(String visitorPlayerName) {
		this.visitorPlayerName = visitorPlayerName;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public long getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(long registerTime) {
		this.registerTime = registerTime;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	
	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return playerID;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		playerID = key.toString();
	}
	
}
