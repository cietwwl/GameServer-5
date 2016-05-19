package com.mi.game.module.lead.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.lead.pojo.LeadDesitnyEntity;

public class DestinyEntityDAO extends AbstractBaseDAO<LeadDesitnyEntity>{
	
	private static final DestinyEntityDAO LEAD_ENTITY_DAO = new DestinyEntityDAO();
	private DestinyEntityDAO(){}
	
	public static DestinyEntityDAO getInstance(){
		return LEAD_ENTITY_DAO;
	}
}
