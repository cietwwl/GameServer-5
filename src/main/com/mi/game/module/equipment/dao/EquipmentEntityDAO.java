package com.mi.game.module.equipment.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.equipment.pojo.EquipmentMapEntity;

public class EquipmentEntityDAO extends AbstractBaseDAO<EquipmentMapEntity>{
	
	private static final EquipmentEntityDAO equipmentEntityDAO = new EquipmentEntityDAO();
	private EquipmentEntityDAO (){}
	public static EquipmentEntityDAO getInstance(){
		return equipmentEntityDAO;
	}
}
