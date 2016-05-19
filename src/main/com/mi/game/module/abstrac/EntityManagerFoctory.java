package com.mi.game.module.abstrac;

import java.util.HashMap;
import java.util.Map;

public class EntityManagerFoctory {
	private static Map<String, BaseEntityManager<?>> entityManagerMap = new HashMap<String, BaseEntityManager<?>>();

	public static BaseEntityManager<?> getEntityManager(String entityName) {
		BaseEntityManager<?> entityManager = entityManagerMap.get(entityName);
		if (entityManager == null) {
			try {
				if (entityName.endsWith("RecordEntity")) {
					entityManager = (BaseEntityManager<?>) Class.forName("com.mi.game.module.admin.manager." + entityName + "Manager").newInstance();
				} else {
					entityManager = (BaseEntityManager<?>) Class.forName("com.mi.game.module.admin.manager." + entityName + "Manager").newInstance();
				}
				entityManagerMap.put(entityName, entityManager);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return entityManager;
	}

}
