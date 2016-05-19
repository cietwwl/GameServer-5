package com.mi.game.module.analyse.pojo;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;

/**
 * 订单统计数据
 *
 */
public class AnalyPayEntity extends BaseEntity {

	private static final long serialVersionUID = -8777647531577782670L;
	// 订单id
	@Indexed
	private String payno;
	// 充值时间
	private String paytime;
	// 平台id
	private String uid;
	// 角色id
	private String player_id;
	// 充值金额
	private int pay_money;
	// 兑换的虚拟货币
	private int add_coin;
	// 充值类型
	private String payfrom;
	// 充值时等级
	private int level;
	// 操作系统版本
	private String osversion;
	// 设备型号
	private String phonetype;
	// imei
	private String imei;
	// mac
	private String mac;
	// 渠道名称
	private String store;
	// 分服标识
	private String server;
	// 首次充值
	private int is_first;
	// 设备id
	private String device_id;

	public String getPayno() {
		return payno;
	}

	public void setPayno(String payno) {
		this.payno = payno;
	}

	public String getPaytime() {
		return paytime;
	}

	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPlayer_id() {
		return player_id;
	}

	public void setPlayer_id(String player_id) {
		this.player_id = player_id;
	}

	public String getOsversion() {
		return osversion;
	}

	public void setOsversion(String osversion) {
		this.osversion = osversion;
	}

	public String getPhonetype() {
		return phonetype;
	}

	public void setPhonetype(String phonetype) {
		this.phonetype = phonetype;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public int getPay_money() {
		return pay_money;
	}

	public void setPay_money(int pay_money) {
		this.pay_money = pay_money;
	}

	public int getAdd_coin() {
		return add_coin;
	}

	public void setAdd_coin(int add_coin) {
		this.add_coin = add_coin;
	}

	public String getPayfrom() {
		return payfrom;
	}

	public void setPayfrom(String payfrom) {
		this.payfrom = payfrom;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getIs_first() {
		return is_first;
	}

	public void setIs_first(int is_first) {
		this.is_first = is_first;
	}

	@Override
	public Object getKey() {
		return payno;
	}

	@Override
	public String getKeyName() {
		return "payno";
	}

	@Override
	public void setKey(Object key) {
		payno = key.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(payno).append("\t");
		sb.append(paytime).append("\t");
		sb.append(uid).append("\t");
		sb.append(player_id).append("\t");
		sb.append(pay_money).append("\t");
		sb.append(add_coin).append("\t");
		sb.append(payfrom).append("\t");
		sb.append(level).append("\t");
		sb.append(osversion).append("\t");
		sb.append(phonetype).append("\t");
		sb.append(imei).append("\t");
		sb.append(mac).append("\t");
		sb.append(store).append("\t");
		sb.append(server).append("\t");
		sb.append(is_first).append("\t");
		sb.append(device_id).append("\t");
		return sb.toString();
	}

}
