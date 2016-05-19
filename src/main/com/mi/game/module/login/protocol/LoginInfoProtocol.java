package com.mi.game.module.login.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.game.defines.HandlerIds;
import com.mi.game.module.arena.pojo.ArenaShopEntity;
import com.mi.game.module.arena.pojo.LuckRankMapEntity;
import com.mi.game.module.bag.pojo.BagEntity;
import com.mi.game.module.dungeon.pojo.ActLimitRewardMapEntity;
import com.mi.game.module.dungeon.pojo.DungeonActMapEntity;
import com.mi.game.module.dungeon.pojo.DungeonActiveEntity;
import com.mi.game.module.dungeon.pojo.DungeonEliteEntity;
import com.mi.game.module.dungeon.pojo.DungeonMapEntity;
import com.mi.game.module.effect.pojo.PlayerEffectEntity;
import com.mi.game.module.equipment.pojo.EquipmentMapEntity;
import com.mi.game.module.farm.pojo.FarmEntity;
import com.mi.game.module.hero.pojo.HeroEntity;
import com.mi.game.module.hero.pojo.HeroTroopsEntity;
import com.mi.game.module.heroDraw.pojo.HeroDrawEntity;
import com.mi.game.module.lead.pojo.LeadDesitnyEntity;
import com.mi.game.module.login.pojo.NewPlayerEntity;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.login.pojo.PlayerStatusEntity;
import com.mi.game.module.login.pojo.ServerInfoEntity;
import com.mi.game.module.manual.pojo.HeroManualsEntity;
import com.mi.game.module.pet.pojo.PetEntity;
import com.mi.game.module.talisman.pojo.TalismanMapEntity;
import com.mi.game.module.talisman.pojo.TalismanShard;
import com.mi.game.module.tower.pojo.TowerEntity;
import com.mi.game.module.vitatly.pojo.VitatlyEntity;
import com.mi.game.module.wallet.pojo.WalletEntity;
import com.mi.game.util.CommonMethod;
import com.mi.core.protocol.BaseProtocol;

public class LoginInfoProtocol extends BaseProtocol {
	private List<ServerInfoEntity> serverList;
	private HeroTroopsEntity heroTroopsEntity;
	private HeroEntity heroEntity;
	private PlayerEntity playerEntity;
	private DungeonMapEntity dungeonMapEntity;
	private VitatlyEntity vitatlyEntity;
	private BagEntity bagEntity;
	private EquipmentMapEntity equipmentMapEntity;
	private WalletEntity walletEntity;
	private TalismanMapEntity talismanMapEntity;
	private PetEntity petEntity;
	private HeroManualsEntity heroManualsEntity;
	private int newPlayer;
	private List<String> maleNameList;
	private List<String> femaleNameList;
	private DungeonActMapEntity dungeonActMapEntity;
	private DungeonEliteEntity dungeonEliteEntity;
	private DungeonActiveEntity dungeonActiveEntity;
	private TowerEntity towerEntity;
	private PlayerStatusEntity playerStatusEntity;
	private LeadDesitnyEntity leadDesitnyEntity;
	private LuckRankMapEntity luckRankMapEntity;
	private ArenaShopEntity arenaShopEntity;
	private NewPlayerEntity newPlayerEntity;
	private HeroDrawEntity heroDrawEntity;
	private PlayerEffectEntity playerEffectEntity;
	private FarmEntity farmEntity;
	private Map<String, Object> itemMap;
	private List<TalismanShard> talismanShardList;
	private String uniqueKey;
	private int status;
	private ActLimitRewardMapEntity actLimitRewardMapEntity;
	private long visitorID;
	private boolean bind;
	private String playerName;
	private String uid;
	private String stopServerMessage;
	private String[] openIDList;
	@Override
	public Map<String, Object> responseMap(int y) {
		Map<String, Object> response = new HashMap<String, Object>();
		switch (y) {
		case HandlerIds.UserLogin:
			break;
		case HandlerIds.VerifyMiuiSession:
			break;
		case HandlerIds.platformLogin:
			break;
		case HandlerIds.getBaseInfo:
			if (playerEffectEntity != null)
				response.put("playerEffectEntity", playerEffectEntity.responseMap());
			if (heroDrawEntity != null)
				response.put("heroDrawEntity", heroDrawEntity.responseMap());
			if (newPlayerEntity != null)
				response.put("newPlayerEntity", newPlayerEntity.responseMap());
			if (arenaShopEntity != null)
				response.put("arenaShop", arenaShopEntity.responseMap());
			if (luckRankMapEntity != null)
				response.put("luckRankList", luckRankMapEntity.responseMap());
			if (playerStatusEntity != null)
				response.put("playerStatusEntity", playerStatusEntity.responseMap());
			if (walletEntity != null)
				response.put("walletEntity", walletEntity.responseMap());
			if (bagEntity != null)
				response.put("bagEntity", bagEntity.responseMap());
			if (vitatlyEntity != null)
				response.put("vitatlyEntity", vitatlyEntity.responseMap());
			response.put("serverTime", System.currentTimeMillis());
			break;
		case HandlerIds.getBattleInfo:
			if (playerEntity != null)
				response.put("playerEntity", playerEntity.responseMap());
			if (leadDesitnyEntity != null)
				response.put("leadDesitnyEntity", leadDesitnyEntity.responseMap());
			if (heroTroopsEntity != null)
				response.put("heroTroopsEntity", heroTroopsEntity.responseMap());
			if (heroManualsEntity != null)
				response.put("heroManualsEntity", heroManualsEntity.responseMap());
			if (heroEntity != null)
				response.put("heroEntity", heroEntity.responseMap());
			if (talismanMapEntity != null)
				response.put("talismanMapEntity", talismanMapEntity.responseMap());
			if (talismanShardList != null)
				response.put("talismanShardMapEntity", CommonMethod.getResponseListMap(talismanShardList));
			if (equipmentMapEntity != null)
				response.put("equipMapEntity", equipmentMapEntity.responseMap());
			response.put("uniqueKey", uniqueKey);
			break;
		case HandlerIds.getDungeonInfo:
			if (dungeonActiveEntity != null)
				response.put("dungeonActiveEntity", dungeonActiveEntity.responseMap());
			if (dungeonMapEntity != null)
				response.put("dungeonMapEntity", dungeonMapEntity.responseMap());
			if (dungeonEliteEntity != null)
				response.put("dungeonEliteEntity", dungeonEliteEntity.responseMap());
			if (dungeonActMapEntity != null)
				response.put("dungeonActMapEntity", dungeonActMapEntity.responseMap());
			if (actLimitRewardMapEntity != null)
				response.put("actLimitRewardMapEntity", actLimitRewardMapEntity.responseMap());
			if (farmEntity != null)
				response.put("farmEntity", farmEntity.responseMap());
			break;
		case HandlerIds.ServerLogin:
			if (heroTroopsEntity != null)
				response.put("heroTroopsEntity", heroTroopsEntity.responseMap());
			if (heroEntity != null)
				response.put("heroEntity", heroEntity.responseMap());
			if (playerEntity != null)
				response.put("playerEntity", playerEntity.responseMap());
			if (dungeonMapEntity != null)
				response.put("dungeonMapEntity", dungeonMapEntity.responseMap());
			if (vitatlyEntity != null)
				response.put("vitatlyEntity", vitatlyEntity.responseMap());
			if (bagEntity != null)
				response.put("bagEntity", bagEntity.responseMap());
			if (equipmentMapEntity != null)
				response.put("equipMapEntity", equipmentMapEntity.responseMap());
			if (walletEntity != null)
				response.put("walletEntity", walletEntity.responseMap());
			if (talismanMapEntity != null)
				response.put("talismanMapEntity", talismanMapEntity.responseMap());
			if (petEntity != null)
				response.put("petEntity", petEntity.responseMap());
			if (heroManualsEntity != null)
				response.put("heroManualsEntity", heroManualsEntity.responseMap());
			if (dungeonActMapEntity != null)
				response.put("dungeonActMapEntity", dungeonActMapEntity.responseMap());
			if (dungeonEliteEntity != null)
				response.put("dungeonEliteEntity", dungeonEliteEntity.responseMap());
			if (dungeonActiveEntity != null)
				response.put("dungeonActiveEntity", dungeonActiveEntity.responseMap());
			if (playerStatusEntity != null)
				response.put("playerStatusEntity", playerStatusEntity.responseMap());
			if (leadDesitnyEntity != null)
				response.put("leadDesitnyEntity", leadDesitnyEntity.responseMap());
			if (talismanShardList != null)
				response.put("talismanShardMapEntity", CommonMethod.getResponseListMap(talismanShardList));
			if (luckRankMapEntity != null)
				response.put("luckRankList", luckRankMapEntity.responseMap());
			response.put("serverTime", System.currentTimeMillis());
			if (arenaShopEntity != null)
				response.put("arenaShop", arenaShopEntity.responseMap());
			if (newPlayerEntity != null)
				response.put("newPlayerEntity", newPlayerEntity.responseMap());
			if (heroDrawEntity != null)
				response.put("heroDrawEntity", heroDrawEntity.responseMap());
			if (playerEffectEntity != null)
				response.put("playerEffectEntity", playerEffectEntity.responseMap());
			if (farmEntity != null)
				response.put("farmEntity", farmEntity.responseMap());
			if (actLimitRewardMapEntity != null)
				response.put("actLimitRewardMapEntity", actLimitRewardMapEntity.responseMap());
			response.put("uniqueKey", uniqueKey);
			break;
		case HandlerIds.getRandomNameList:
			if (maleNameList != null)
				response.put("maleNameList", maleNameList);
			if (femaleNameList != null)
				response.put("femaleNameList", femaleNameList);
			break;
		case HandlerIds.changePlayerName:
			response.put("itemMap", itemMap);
			break;
		case HandlerIds.getPlayerStatus:
			response.put("newPlayer", newPlayer);
			break;
		case HandlerIds.GetServerList:
			response.put("status", status);
			response.put("serverList", CommonMethod.getResponseListMap(serverList));
			response.put("playerID", this.getPlayerID());
			response.put("stopServerMessage", stopServerMessage);
			response.put("openPlayerIDList", openIDList);
			break;
		case HandlerIds.visitorLogin:
			response.put("playerID", this.getPlayerID());
			response.put("visitorID", visitorID);
			response.put("bind", bind);
			response.put("playerName", playerName);
			break;
		case HandlerIds.getVisitorServerList:
			response.put("status", status);
			response.put("serverList", CommonMethod.getResponseListMap(serverList));
			response.put("playerID", this.getPlayerID());
			response.put("bind", bind);
			response.put("playerName", playerName);
			break;
		case HandlerIds.getPlatFormInfo:
			response.put("uid", uid);
			break;
		case HandlerIds.getVitatlyEntity:
			response.put("vitatlyEntity", vitatlyEntity.responseMap());
			break;
		}
		// 返回服务器当前时间
		response.put("serverTime", System.currentTimeMillis());

		return response;
	}

	
	
	public String[] getOpenIDList() {
		return openIDList;
	}

	public void setOpenIDList(String[] openIDList) {
		this.openIDList = openIDList;
	}

	public String getStopServerMessage() {
		return stopServerMessage;
	}
	
	public void setStopServerMessage(String stopServerMessage) {
		this.stopServerMessage = stopServerMessage;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
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

	public ActLimitRewardMapEntity getActLimitRewardMapEntity() {
		return actLimitRewardMapEntity;
	}

	public void setActLimitRewardMapEntity(ActLimitRewardMapEntity actLimitRewardMapEntity) {
		this.actLimitRewardMapEntity = actLimitRewardMapEntity;
	}

	public FarmEntity getFarmEntity() {
		return farmEntity;
	}

	public void setFarmEntity(FarmEntity farmEntity) {
		this.farmEntity = farmEntity;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public List<TalismanShard> getTalismanShardList() {
		return talismanShardList;
	}

	public void setTalismanShardList(List<TalismanShard> talismanShardList) {
		this.talismanShardList = talismanShardList;
	}

	public PlayerEffectEntity getPlayerEffectEntity() {
		return playerEffectEntity;
	}

	public void setPlayerEffectEntity(PlayerEffectEntity playerEffectEntity) {
		this.playerEffectEntity = playerEffectEntity;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public HeroDrawEntity getHeroDrawEntity() {
		return heroDrawEntity;
	}

	public void setHeroDrawEntity(HeroDrawEntity heroDrawEntity) {
		this.heroDrawEntity = heroDrawEntity;
	}

	public NewPlayerEntity getNewPlayerEntity() {
		return newPlayerEntity;
	}

	public void setNewPlayerEntity(NewPlayerEntity newPlayerEntity) {
		this.newPlayerEntity = newPlayerEntity;
	}

	public ArenaShopEntity getArenaShopEntity() {
		return arenaShopEntity;
	}

	public void setArenaShopEntity(ArenaShopEntity arenaShopEntity) {
		this.arenaShopEntity = arenaShopEntity;
	}

	public LuckRankMapEntity getLuckRankMapEntity() {
		return luckRankMapEntity;
	}

	public void setLuckRankMapEntity(LuckRankMapEntity luckRankMapEntity) {
		this.luckRankMapEntity = luckRankMapEntity;
	}

	public LeadDesitnyEntity getLeadDesitnyEntity() {
		return leadDesitnyEntity;
	}

	public void setLeadDesitnyEntity(LeadDesitnyEntity leadDesitnyEntity) {
		this.leadDesitnyEntity = leadDesitnyEntity;
	}

	public PlayerStatusEntity getPlayerStatusEntity() {
		return playerStatusEntity;
	}

	public void setPlayerStatusEntity(PlayerStatusEntity playerStatusEntity) {
		this.playerStatusEntity = playerStatusEntity;
	}

	public TowerEntity getTowerEntity() {
		return towerEntity;
	}

	public void setTowerEntity(TowerEntity towerEntity) {
		this.towerEntity = towerEntity;
	}

	public DungeonActiveEntity getDungeonActiveEntity() {
		return dungeonActiveEntity;
	}

	public void setDungeonActiveEntity(DungeonActiveEntity dungeonActiveEntity) {
		this.dungeonActiveEntity = dungeonActiveEntity;
	}

	public DungeonEliteEntity getDungeonEliteEntity() {
		return dungeonEliteEntity;
	}

	public void setDungeonEliteEntity(DungeonEliteEntity dungeonEliteEntity) {
		this.dungeonEliteEntity = dungeonEliteEntity;
	}

	public DungeonActMapEntity getDungeonActMapEntity() {
		return dungeonActMapEntity;
	}

	public void setDungeonActMapEntity(DungeonActMapEntity dungeonActMapEntity) {
		this.dungeonActMapEntity = dungeonActMapEntity;
	}

	public List<String> getMaleNameList() {
		return maleNameList;
	}

	public void setMaleNameList(List<String> maleNameList) {
		this.maleNameList = maleNameList;
	}

	public List<String> getFemaleNameList() {
		return femaleNameList;
	}

	public void setFemaleNameList(List<String> femaleNameList) {
		this.femaleNameList = femaleNameList;
	}

	public int getNewPlayer() {
		return newPlayer;
	}

	public void setNewPlayer(int newPlayer) {
		this.newPlayer = newPlayer;
	}

	public HeroManualsEntity getHeroManualsEntity() {
		return heroManualsEntity;
	}

	public void setHeroManualsEntity(HeroManualsEntity heroManualsEntity) {
		this.heroManualsEntity = heroManualsEntity;
	}

	public PetEntity getPetEntity() {
		return petEntity;
	}

	public void setPetEntity(PetEntity petEntity) {
		this.petEntity = petEntity;
	}

	public TalismanMapEntity getTalismanMapEntity() {
		return talismanMapEntity;
	}

	public void setTalismanMapEntity(TalismanMapEntity talismanMapEntity) {
		this.talismanMapEntity = talismanMapEntity;
	}

	public WalletEntity getWalletEntity() {
		return walletEntity;
	}

	public void setWalletEntity(WalletEntity walletEntity) {
		this.walletEntity = walletEntity;
	}

	public EquipmentMapEntity getEquipmentMapEntity() {
		return equipmentMapEntity;
	}

	public void setEquipmentMapEntity(EquipmentMapEntity equipmentMapEntity) {
		this.equipmentMapEntity = equipmentMapEntity;
	}

	public BagEntity getBagEntity() {
		return bagEntity;
	}

	public void setBagEntity(BagEntity bagEntity) {
		this.bagEntity = bagEntity;
	}

	public VitatlyEntity getVitatlyEntity() {
		return vitatlyEntity;
	}

	public void setVitatlyEntity(VitatlyEntity vitatlyEntity) {
		this.vitatlyEntity = vitatlyEntity;
	}

	public DungeonMapEntity getDungeonMapEntity() {
		return dungeonMapEntity;
	}

	public void setDungeonMapEntity(DungeonMapEntity dungeonMapEntity) {
		this.dungeonMapEntity = dungeonMapEntity;
	}

	public PlayerEntity getPlayerEntity() {
		return playerEntity;
	}

	public void setPlayerEntity(PlayerEntity playerEntity) {
		this.playerEntity = playerEntity;
	}

	public HeroTroopsEntity getHeroTroopsEntity() {
		return heroTroopsEntity;
	}

	public void setHeroTroopsEntity(HeroTroopsEntity heroTroopsEntity) {
		this.heroTroopsEntity = heroTroopsEntity;
	}

	public HeroEntity getHeroEntity() {
		return heroEntity;
	}

	public void setHeroEntity(HeroEntity heroEntity) {
		this.heroEntity = heroEntity;
	}

	public List<ServerInfoEntity> getServerList() {
		return serverList;
	}

	public void setServerList(List<ServerInfoEntity> serverList) {
		this.serverList = serverList;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
