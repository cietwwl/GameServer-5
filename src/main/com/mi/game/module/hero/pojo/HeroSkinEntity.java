package com.mi.game.module.hero.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;

@Entity(noClassnameStored = true)
public class HeroSkinEntity extends BaseEntity {
	public HeroSkinEntity() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -554675721301267472L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String playerID;
	private Map<String, Integer> heroSkin = new HashMap<>();
	private Map<String, List<Integer>> allHeroSkin = new HashMap<>();

	public Map<String, List<Integer>> getAllHeroSkin() {
		if (allHeroSkin == null) {
			allHeroSkin = new HashMap<>();
		}
		return allHeroSkin;
	}

	public void setAllHeroSkin(Map<String, List<Integer>> allHeroSkin) {
		this.allHeroSkin = allHeroSkin;
	}

	public Map<String, Integer> getHeroSkin() {
		return heroSkin;
	}

	public void setHeroSkin(Map<String, Integer> heroSkin) {
		this.heroSkin = heroSkin;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("heroSkin", heroSkin);
		response.put("allHeroSkin", allHeroSkin);
		return response;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("playerID", playerID);
		response.put("heroSkinSize", heroSkin.size());
		response.put("allHeroSkinSize", allHeroSkin.size());
		return response;
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
