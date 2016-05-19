package com.mi.game.module.legion.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.mi.core.engine.ModuleManager;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.arena.ArenaModule;
import com.mi.game.module.friend.FriendModule;
import com.mi.game.module.legion.LegionModule;
import com.mi.game.module.legion.define.LegionConstans;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;

/**
 *
 */
@Entity(noClassnameStored = true)
public class LegionEntity extends BaseEntity {

	private static final long serialVersionUID = -7680273400911308098L;
	// 军团id
	@Indexed
	private String legionID;
	// 军团名称
	@Indexed
	private String name;
	// 军团宣言
	private String declaration;
	// 军团公告
	private String notice;
	// 军团长
	private String legatus;
	// 副军团长
	private List<String> legatusList = new ArrayList<String>();
	// 密码
	private String pwd;
	// 最大人数
	private int maxNum;
	// 军团贡献
	private long legionDevote;
	// 军团等级
	private int level;
	// 成员列表
	private List<String> members = new ArrayList<String>();
	// 军团大厅
	private LegionHall legionHall;
	// 军团商店
	private LegionShop legionShop;
	// 军团关公殿
	private LegionGG legiongg;
	// 申请列表
	private Map<String, String> applys = new HashMap<String, String>();
	// 受否解散
	private boolean isKill;

	public String getLegionID() {
		return legionID;
	}

	public void setLegionID(String legionID) {
		this.legionID = legionID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getLegatus() {
		return legatus;
	}

	public void setLegatus(String legatus) {
		this.legatus = legatus;
	}

	public List<String> getLegatusList() {
		return legatusList;
	}

	public void setLegatusList(List<String> legatusList) {
		this.legatusList = legatusList;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public long getLegionDevote() {
		return legionDevote;
	}

	public void setLegionDevote(long legionDevote) {
		this.legionDevote = legionDevote;
	}

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public LegionHall getLegionHall() {
		return legionHall;
	}

	public void setLegionHall(LegionHall legionHall) {
		this.legionHall = legionHall;
	}

	public LegionShop getLegionShop() {
		return legionShop;
	}

	public void setLegionShop(LegionShop legionShop) {
		this.legionShop = legionShop;
	}

	public LegionGG getLegiongg() {
		return legiongg;
	}

	public void setLegiongg(LegionGG legiongg) {
		this.legiongg = legiongg;
	}

	public Map<String, String> getApplys() {
		return applys;
	}

	public void setApplys(Map<String, String> applys) {
		this.applys = applys;
	}

	public void addLegatus(String playerID) {
		legatusList.add(playerID);
	}

	public void delLegatus(String playerID) {
		if (legatusList.contains(playerID)) {
			legatusList.remove(playerID);
		}
	}

	public void addApplys(String playerID, String dateTime) {
		applys.put(playerID, dateTime);
	}

	public void delApplys(String playerID) {
		if (applys.containsKey(playerID)) {
			applys.remove(playerID);
		}
	}

	public void delApplyAll() {
		applys.clear();
	}

	public void addMember(String member) {
		members.add(member);
	}

	public void delMember(String member) {
		if (members.contains(member)) {
			members.remove(member);
		}
	}

	public boolean isKill() {
		return isKill;
	}

	public void setKill(boolean isKill) {
		this.isKill = isKill;
	}

	public void addLegionDevote(int currency) {
		legionDevote += currency;
	}

	public void consumeLegionDevote(int currency) {
		legionDevote -= currency;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("legionID", legionID);
		responseMap.put("legionName", name);
		responseMap.put("legionDeclaration", declaration);
		responseMap.put("legionNotice", notice);
		responseMap.put("legatus", legatus);
		responseMap.put("legatus2", legatusList);
		responseMap.put("password", pwd);
		responseMap.put("maxNum", maxNum);
		responseMap.put("legionDevote", legionDevote);
		responseMap.put("level", level);
		responseMap.put("members", members);
		responseMap.put("legionHall", legionHall);
		responseMap.put("legionShop", legionShop);
		responseMap.put("legiongg", legiongg);
		responseMap.put("applys", applys);
		responseMap.put("isKill", isKill);
		return responseMap;
	}

	public Map<String, Object> getLegionInfoMap(boolean sign) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		if (sign) {
			// 军团信息
			responseMap.put("legionID", legionID);
			responseMap.put("legionName", name);
			responseMap.put("legionDeclaration", declaration);
			responseMap.put("legionNotice", notice);
			responseMap.put("level", level);
			responseMap.put("legatus", legatus);
			responseMap.put("legatus2", legatusList);
			responseMap.put("currentNum", members.size());
			responseMap.put("maxNum", maxNum);
			responseMap.put("legionDevote", legionDevote);
			responseMap.put("legionfightValue", getLegionfightValue(sign));
		}
		return responseMap;
	}

	/**
	 * 军团信息
	 * 
	 * @return
	 */
	public Map<String, Object> getLegionMap(boolean sign) {
		// 军团长信息
		Map<String, Object> responseMap = new HashMap<String, Object>();
		if (sign) {
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			ArenaModule arenaModule = ModuleManager.getModule(ModuleNames.ArenaModule, ArenaModule.class);
			PlayerEntity playerEntity = loginModule.getPlayerEntity(legatus);
			responseMap.put("legatus", legatus);
			responseMap.put("legatusName", playerEntity.getNickName());
			responseMap.put("legatusSex", playerEntity.getSex());
			responseMap.put("photoID", playerEntity.getPhotoID());
			responseMap.put("chatPhotoID", playerEntity.getChatPhotoID());
			responseMap.put("legatusLevel", playerEntity.getLevel());
			responseMap.put("legionID", legionID);
			responseMap.put("legionName", name);
			responseMap.put("level", level);
			// 军团长竞技场排名
			responseMap.put("legatusArenaRank", arenaModule.getArenaRank(legatus));
			responseMap.put("legionDeclaration", declaration);
			responseMap.put("currentNum", members.size());
			responseMap.put("maxNum", maxNum);
			responseMap.put("legionfightValue", getLegionfightValue(sign));
		}
		return responseMap;
	}

	/**
	 * 获取军团成员列表
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getMembersMap(boolean sign, String playerID) {
		List<Map<String, Object>> memberList = new ArrayList<Map<String, Object>>();
		if (sign) {
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			LegionModule legionModule = ModuleManager.getModule(ModuleNames.LegionModule, LegionModule.class);
			ArenaModule arenaModule = ModuleManager.getModule(ModuleNames.ArenaModule, ArenaModule.class);
			FriendModule friendModule = ModuleManager.getModule(ModuleNames.FriendMoudle, FriendModule.class);
			for (String member : members) {
				Map<String, Object> memberMap = new HashMap<String, Object>();
				PlayerEntity playerEntity = loginModule.getPlayerEntity(member);
				LegionMemberEntity memberEntity = legionModule.getLegionMemberEntity(member);
				if (playerEntity != null) {
					// 拼装玩家信息
					memberMap.put("playerID", member);
					memberMap.put("nickName", playerEntity.getNickName());
					memberMap.put("level", playerEntity.getLevel());
					memberMap.put("sex", playerEntity.getSex());
					memberMap.put("photoID", playerEntity.getPhotoID());
					memberMap.put("fightValue", playerEntity.getFightValue());
					memberMap.put("arenaRank", arenaModule.getArenaRank(member));
					memberMap.put("chatPhotoID", playerEntity.getChatPhotoID());
					memberMap.put("loginTime", playerEntity.getLoginTime());
					// 是否好友关系
					memberMap.put("isFriend", friendModule.isFriend(playerID, member));
					// 拼装玩家军团信息
					if (memberEntity != null) {
						memberMap.put("devote", memberEntity.getDevote());
						memberMap.put("maxDevote", memberEntity.getMaxDevote());
						// 最后一次建设时间,0为未建设过
						memberMap.put("buildDay", legionModule.getLegionHistoryBuildDay(member));
						if (memberEntity.isVisitRefresh(true)) {
							memberMap.put("visit", memberEntity.getVisit());
						} else {
							memberMap.put("visit", LegionConstans.LEGION_VISITMAX);
						}
						if (memberEntity.isCompareRefresh(true)) {
							memberMap.put("compare", memberEntity.getCompare());
						} else {
							memberMap.put("compare", LegionConstans.LEGION_COMPAREMAX);
						}
						if (memberEntity.isBuildRefresh(true)) {
							memberMap.put("build", memberEntity.getBuild());
						}
						if (memberEntity.isBuyRefresh(true)) {
							memberMap.put("buyItem", memberEntity.getBuyItem());
						}
						if (memberEntity.isGemRefresh(true)) {
							memberMap.put("gemItem", memberEntity.getGemItem());
						}
					}
				}
				memberList.add(memberMap);
			}
		}
		return memberList;
	}

	/**
	 * 获取军团申请加入列表
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getApplysMap(boolean sign) {
		List<Map<String, Object>> applyList = new ArrayList<Map<String, Object>>();
		if (sign) {
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			ArenaModule arenaModule = ModuleManager.getModule(ModuleNames.ArenaModule, ArenaModule.class);
			Set<Entry<String, String>> playerSet = applys.entrySet();
			for (Entry<String, String> entry : playerSet) {
				Map<String, Object> memberMap = new HashMap<String, Object>();
				PlayerEntity playerEntity = loginModule.getPlayerEntity(entry.getKey());
				if (playerEntity != null) {
					// 拼装成员列表
					memberMap.put("playerID", entry.getKey());
					memberMap.put("nickName", playerEntity.getNickName());
					memberMap.put("level", playerEntity.getLevel());
					memberMap.put("sex", playerEntity.getSex());
					memberMap.put("photoID", playerEntity.getPhotoID());
					memberMap.put("fightValue", playerEntity.getFightValue());
					memberMap.put("arenaRank", arenaModule.getArenaRank(entry.getKey()));
					memberMap.put("chatPhotoID", playerEntity.getChatPhotoID());
					// 申请时间
					memberMap.put("applyTime", applys.get(entry.getKey()));
				}
				applyList.add(memberMap);
			}
		}
		return applyList;
	}

	/**
	 * 获取军团战斗力
	 * 
	 * @return
	 */
	public long getLegionfightValue(boolean sign) {
		long legionfightValue = 0;
		if (sign) {
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			for (String member : members) {
				PlayerEntity playerEntity = loginModule.getPlayerEntity(member);
				if (playerEntity != null) {
					// 计算军团战斗力
					legionfightValue += playerEntity.getFightValue();
				}
			}
		}
		return legionfightValue;
	}

	/**
	 * 判断是否军团长
	 * 
	 * @param playerID
	 * @return
	 */
	public boolean isLegatus(String playerID) {
		if (legatus != null && legatus.equals(playerID)) {
			return true;
		}
		if (legatusList != null) {
			for (String legatus : legatusList) {
				if (legatus.equals(playerID)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 检查用户是否副军团长
	 * 
	 * @param playerID
	 * @return
	 */
	public boolean isLegatus2(String playerID) {
		if (legatusList != null) {
			for (String legatus : legatusList) {
				if (legatus.equals(playerID)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Object getKey() {
		return legionID;
	}

	@Override
	public String getKeyName() {
		return "legionID";
	}

	@Override
	public void setKey(Object key) {
		legionID = key.toString();
	}
}
