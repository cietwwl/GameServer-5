package com.mi.game.module.pet.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.pet.pojo.PetEntity;

public class PetDAO extends AbstractBaseDAO<PetEntity>{
	private final static PetDAO petDAO = new PetDAO();
	private PetDAO(){}
	
	public static PetDAO getInstance(){
		return petDAO;
	}

}
