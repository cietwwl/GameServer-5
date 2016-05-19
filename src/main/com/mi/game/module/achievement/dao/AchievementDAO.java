package com.mi.game.module.achievement.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.achievement.pojo.AchievementEntity;

public class AchievementDAO extends AbstractBaseDAO<AchievementEntity>{
	private final static AchievementDAO ACHIEVEMENT_DAO= new AchievementDAO();
	private AchievementDAO(){}
	public static AchievementDAO getInstance(){
		return ACHIEVEMENT_DAO;
	}
}
