package com.mi.game.module.event.pojo;

import com.mi.core.pojo.BaseEntity;

/**
 * vip折扣店购买历史
 * 
 * @author 赵鹏翔
 * @time Apr 7, 2015 2:45:19 PM
 */
public class EventVipDigoShopEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5041723066842656531L;
	private String buyID; // 主键
	private String playerID; // 玩家id
	private Integer goodsID; // 购买物品id
	private Integer buyNum; // 购买数量
	private String day; // 购买日期
	private Integer price; // 总价
	private Long updateTime; // 更新时间
	private Integer pid; // 模版ID

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getBuyID() {
		return buyID;
	}

	public void setBuyID(String buyID) {
		this.buyID = buyID;
	}

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public Integer getGoodsID() {
		return goodsID;
	}

	public void setGoodsID(Integer goodsID) {
		this.goodsID = goodsID;
	}

	public Integer getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(Integer buyNum) {
		this.buyNum = buyNum;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public Object getKey() {
		return buyID;
	}

	@Override
	public String getKeyName() {
		return "buyID";
	}

	@Override
	public void setKey(Object key) {
		this.buyID = key.toString();
	}

}
