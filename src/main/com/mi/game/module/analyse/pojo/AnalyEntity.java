package com.mi.game.module.analyse.pojo;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;

/**
 * 统计需求数据
 *
 */
public class AnalyEntity extends BaseEntity {

	private static final long serialVersionUID = -6179564091955868617L;
	// 平台id
	private String uid;
	// 设备唯一id
	private String device_id;
	// 首次注册时间
	private String add_time;
	// 平台性别
	private int sex;
	// 首次注册ip
	private String first_login_ip;
	// 最后登录时间
	private String last_login_time;
	// 最后登录ip
	private String last_login_ip;
	// 总登陆天数(活跃天数)
	private int total_num;
	// 连续登录天数
	private int login_num;
	// 角色id
	@Indexed
	private String player_id;
	// 角色名称
	private String role_name;
	// 角色职业
	private String occupational;
	// 角色性别
	private int role_sex;
	// 角色创建时间
	private String create_time;
	// 角色等级
	private int level;
	// vip等级
	private int vip_level;
	// 角色经验
	private int exp;
	// 虚拟货币
	private int goldingot;
	// 好友数
	private int friends_num;
	// 操作系统版本
	private String osversion;
	// 设备型号
	private String phonetype;
	// 设备imei
	private String imei;
	// 设备mac地址
	private String mac;
	// 渠道名称
	private String store;
	// 分服标示
	private String server;

	// ///////////////
	// 操作系统类型
	private String os;
	// 游戏标记
	private String gameid;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getFirst_login_ip() {
		return first_login_ip;
	}

	public void setFirst_login_ip(String first_login_ip) {
		this.first_login_ip = first_login_ip;
	}

	public String getLast_login_time() {
		return last_login_time;
	}

	public void setLast_login_time(String last_login_time) {
		this.last_login_time = last_login_time;
	}

	public String getLast_login_ip() {
		return last_login_ip;
	}

	public void setLast_login_ip(String last_login_ip) {
		this.last_login_ip = last_login_ip;
	}

	public int getTotal_num() {
		return total_num;
	}

	public void setTotal_num(int total_num) {
		this.total_num = total_num;
	}

	public int getLogin_num() {
		return login_num;
	}

	public void setLogin_num(int login_num) {
		this.login_num = login_num;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getOccupational() {
		return occupational;
	}

	public void setOccupational(String occupational) {
		this.occupational = occupational;
	}

	public int getRole_sex() {
		return role_sex;
	}

	public void setRole_sex(int role_sex) {
		this.role_sex = role_sex;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getVip_level() {
		return vip_level;
	}

	public void setVip_level(int vip_level) {
		this.vip_level = vip_level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getGoldingot() {
		return goldingot;
	}

	public void setGoldingot(int goldingot) {
		this.goldingot = goldingot;
	}

	public int getFriends_num() {
		return friends_num;
	}

	public void setFriends_num(int friends_num) {
		this.friends_num = friends_num;
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

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getGameid() {
		return gameid;
	}

	public void setGameid(String gameid) {
		this.gameid = gameid;
	}

	public String getPlayer_id() {
		return player_id;
	}

	public void setPlayer_id(String player_id) {
		this.player_id = player_id;
	}

	@Override
	public Object getKey() {
		return player_id;
	}

	@Override
	public String getKeyName() {
		return "player_id";
	}

	@Override
	public void setKey(Object key) {
		player_id = key.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append("\t");
		sb.append(device_id).append("\t");
		sb.append(add_time).append("\t");
		sb.append(sex).append("\t");
		sb.append(first_login_ip).append("\t");
		sb.append(last_login_time).append("\t");
		sb.append(last_login_ip).append("\t");
		sb.append(total_num).append("\t");
		sb.append(login_num).append("\t");
		sb.append(player_id).append("\t");
		sb.append(role_name).append("\t");
		sb.append(occupational).append("\t");
		sb.append(role_sex).append("\t");
		sb.append(create_time).append("\t");
		sb.append(level).append("\t");
		sb.append(vip_level).append("\t");
		sb.append(exp).append("\t");
		sb.append(goldingot).append("\t");
		sb.append(friends_num).append("\t");
		sb.append(osversion).append("\t");
		sb.append(phonetype).append("\t");
		sb.append(imei).append("\t");
		sb.append(mac).append("\t");
		sb.append(store).append("\t");
		sb.append(server).append("\t");
		return sb.toString();
	}

}
