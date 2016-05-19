package com.mi.game.module.legion.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mi.core.protocol.BaseProtocol;
import com.mi.core.util.StringUtil;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.legion.pojo.LegionEntity;
import com.mi.game.module.legion.pojo.LegionHistoryEntity;
import com.mi.game.module.legion.pojo.LegionMemberEntity;
import com.mi.game.module.legion.pojo.LegionShop;
import com.mi.game.module.vip.pojo.VipEntity;
import com.mi.game.module.vitatly.pojo.VitatlyEntity;
import com.mi.game.module.wallet.pojo.WalletEntity;

public class LegionProtocol extends BaseProtocol {
	// 钱包信息
	private WalletEntity walletEntity;
	// 体力耐力
	private VitatlyEntity vitatlyEntity;
	// 军团信息
	private LegionEntity legionEntity;
	// 成员信息
	private LegionMemberEntity memberEntity;
	// 军团列表
	private List<LegionEntity> legionList;
	// 申请列表
	private List<String> joinList;
	// 军团动态
	private List<LegionHistoryEntity> historyList;
	// 军团id
	private String legionID;
	// 冷却时间
	private String coolingTime;
	// 物品信息
	private Map<String, Object> itemMap;
	// vip信息
	private VipEntity vipEntity;
	
	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		return response;
	}

	@Override
	public Map<String, Object> responseMap(int y) {
		Map<String, Object> response = new HashMap<String, Object>();
		switch (y) {
		case HandlerIds.LEGION_CREATE:
			if (walletEntity != null) {
				response.put("itemMap", getWalletEntityResponse());
			}
			if (legionEntity != null) {
				response.put("legionEntity", legionEntity.getLegionInfoMap(true));
				response.put("members", legionEntity.getMembersMap(true,(String)getPlayerID()));
				response.put("legionHall", legionEntity.getLegionHall().responseMap());
				response.put("legionShop", legionEntity.getLegionShop().responseMap());
				response.put("legionGG", legionEntity.getLegiongg().responseMap());
				response.put("legionHistorys", getLegionHistoryResponse());
			}
			break;
		case HandlerIds.LEGION_GETLIST:
			if (legionList != null) {
				response.put("legionList", getLegionListResponse());
			}
			if (joinList != null && joinList.size() > 0) {
				response.put("joinList", joinList);
			}
			if (coolingTime != null) {
				response.put("collingTime", coolingTime);
			}
			break;
		case HandlerIds.LEGION_JOINOUT:
			if (legionID != null) {
				response.put("legionID", legionID);
			}
			break;
		case HandlerIds.LEGION_APPLYS:
			if (legionEntity != null) {
				response.put("applys", legionEntity.getApplysMap(true));
			}
			break;
		case HandlerIds.LEGION_MEMBERS:
			if (legionEntity != null) {
				response.put("members", legionEntity.getMembersMap(true,(String)getPlayerID()));
				response.put("applys", legionEntity.getApplysMap(true));
			}
			break;
		case HandlerIds.LEGION_MANAGE: {
			if (legionEntity != null) {
				response.put("legionEntity", legionEntity.getLegionInfoMap(true));
				response.put("members", legionEntity.getMembersMap(true,(String)getPlayerID()));
				response.put("applys", legionEntity.getApplysMap(true));
				response.put("legionHistorys", getLegionHistoryResponse());
			}
			break;
		}
		case HandlerIds.LEGION_INFO:
			if (legionEntity != null) {
				response.put("legionEntity", legionEntity.getLegionInfoMap(true));
				response.put("members", legionEntity.getMembersMap(true,(String)getPlayerID()));
				response.put("legionHall", legionEntity.getLegionHall().responseMap());
				response.put("legionShop", getLegionShopResponse(legionEntity));
				response.put("legionGG", legionEntity.getLegiongg().responseMap());
				response.put("legionHistorys", getLegionHistoryResponse());
			}

			break;
		case HandlerIds.LEGION_HALL:
			if (walletEntity != null) {
				response.put("itemMap", getWalletEntityResponse());
			}
			if (legionEntity != null) {
				response.put("legionEntity", legionEntity.getLegionInfoMap(true));
				response.put("members", legionEntity.getMembersMap(true,(String)getPlayerID()));
				response.put("legionHall", legionEntity.getLegionHall().responseMap());
			}
			response.put("legionHistorys", getLegionHistoryResponse());
			break;
		case HandlerIds.LEGION_SHOP:
			if (legionEntity != null) {
				response.put("legionEntity", legionEntity.getLegionInfoMap(true));
				response.put("members", legionEntity.getMembersMap(true,(String)getPlayerID()));
				response.put("legionShop", legionEntity.getLegionShop().responseMap());
				response.put("legionHistorys", getLegionHistoryResponse());
			}
			if (itemMap != null) {
				response.put("itemMap", itemMap);
			}
			break;
		case HandlerIds.LEGION_GG:
			if (legionEntity != null) {
				response.put("legionEntity", legionEntity.getLegionInfoMap(true));
				response.put("members", legionEntity.getMembersMap(true,(String)getPlayerID()));
				response.put("legionGG", legionEntity.getLegiongg().responseMap());
			}
			if (walletEntity != null) {
				response.put("itemMap", getWalletAndVitatlyResponse());
			}
			if (vipEntity != null) {
				response.put("vipInfo", vipEntity.responseMap());
			}
			response.put("legionHistorys", getLegionHistoryResponse());
			break;
		case HandlerIds.LEGION_HISTORY:
			response.put("legionHistorys", getLegionHistoryResponse());
			break;
		case HandlerIds.LEGION_ISJOIN:
			if(StringUtils.isBlank(legionID)){
				legionID = "0";
			}
			response.put("legionID", legionID);
			break;
		}

		return response;
	}

	// 根据军团获取军团商店数据
	private Map<String, Object> getLegionShopResponse(LegionEntity legionEntity) {
		Map<String, Object> result = new HashMap<String, Object>();
		LegionShop legionShop = legionEntity.getLegionShop();
		result.put("currentPid", legionShop.getCurrentPid());
		result.put("collingTime", legionShop.getDiffTime());
		result.put("goods", legionShop.goodsResponseMap());
		return result;
	}

	// 返回创建军团后的金币,银币数值
	private Map<String, Object> getWalletEntityResponse() {
		Map<String, Object> walletMap = new HashMap<String, Object>();
		walletMap.put("WalletEntity", walletEntity.responseMap());
		return walletMap;
	}

	// 返回参拜关公殿之后数值变化
	private Map<String, Object> getWalletAndVitatlyResponse() {
		Map<String, Object> walletMap = new HashMap<String, Object>();
		walletMap.put("WalletEntity", walletEntity.responseMap());
		if (vitatlyEntity != null) {
			walletMap.put("vitatlyEntity", vitatlyEntity.responseMap());
		}
		return walletMap;
	}

	// 获取军团列表信息
	private List<Map<String, Object>> getLegionListResponse() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (LegionEntity legion : legionList) {
			result.add(legion.getLegionMap(true));
		}
		return result;
	}

	public List<Map<String, Object>> getLegionHistoryResponse() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if (historyList != null && historyList.size() > 0) {
			for (LegionHistoryEntity history : historyList) {
				result.add(history.respsoneMap());
			}
		}
		return result;
	}

	public WalletEntity getWalletEntity() {
		return walletEntity;
	}

	public void setWalletEntity(WalletEntity walletEntity) {
		this.walletEntity = walletEntity;
	}

	public VitatlyEntity getVitatlyEntity() {
		return vitatlyEntity;
	}

	public void setVitatlyEntity(VitatlyEntity vitatlyEntity) {
		this.vitatlyEntity = vitatlyEntity;
	}

	public LegionEntity getLegionEntity() {
		return legionEntity;
	}

	public void setLegionEntity(LegionEntity legionEntity) {
		this.legionEntity = legionEntity;
	}

	public LegionMemberEntity getMemberEntity() {
		return memberEntity;
	}

	public void setMemberEntity(LegionMemberEntity memberEntity) {
		this.memberEntity = memberEntity;
	}

	public List<LegionEntity> getLegionList() {
		return legionList;
	}

	public void setLegionList(List<LegionEntity> legionList) {
		this.legionList = legionList;
	}

	public String getLegionID() {
		return legionID;
	}

	public void setLegionID(String legionID) {
		this.legionID = legionID;
	}

	public String getCoolingTime() {
		return coolingTime;
	}

	public void setCoolingTime(String coolingTime) {
		this.coolingTime = coolingTime;
	}

	public List<String> getJoinList() {
		return joinList;
	}

	public void setJoinList(List<String> joinList) {
		this.joinList = joinList;
	}

	public List<LegionHistoryEntity> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<LegionHistoryEntity> historyList) {
		this.historyList = historyList;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public VipEntity getVipEntity() {
		return vipEntity;
	}

	public void setVipEntity(VipEntity vipEntity) {
		this.vipEntity = vipEntity;
	}

}
