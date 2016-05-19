package com.mi.game.module.analyse.util;

import com.mi.core.util.ConfigUtil;
import com.mi.game.util.Utilities;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class MongoUtils {

	private static MongoClient mongo = null;
	private static String dbName = ConfigUtil.getString("analyse.mongo.db.name");
	private static String collectionName = ConfigUtil.getString("analyse.mongo.collection.name");
	private static String host = ConfigUtil.getString("analyse.mongo.host");
	private static int port = ConfigUtil.getInt("analyse.mongo.port");

	private MongoUtils() {

	}

	public static void insert(BasicDBObject object) {
		DBCollection collection = getDBCollection();
		collection.insert(object);
	}

	public static DBCollection getDBCollection() {
		DB db = getDB(dbName);
		DBCollection collection = db.getCollection(collectionName + "_" + Utilities.getDateTime());
		if (!db.collectionExists(collectionName)) {
			BasicDBObject typeIndex = new BasicDBObject();
			typeIndex.put("type", 1);
			BasicDBObject codeIndex = new BasicDBObject();
			codeIndex.put("code", 1);
			BasicDBObject playerIndex = new BasicDBObject();
			playerIndex.put("playerID", 1);
			BasicDBObject timeIndex = new BasicDBObject();
			timeIndex.put("dateTime", 1);
			collection.createIndex(typeIndex);
			collection.createIndex(codeIndex);
			collection.createIndex(playerIndex);
			collection.createIndex(timeIndex);
		}
		return collection;
	}

	/**
	 * 根据dbName,collectionName 获取DBcollection
	 * 
	 * @param dname
	 * @param cname
	 * @return
	 */
	public static DBCollection getDBCollection(String dname, String cname) {
		DB db = getDB(dname);
		DBCollection collection = db.getCollection(cname);
		return collection;
	}

	public static DB getDB(String name) {
		if (mongo == null) {
			init();
		}
		return mongo.getDB(name);
	}

	private static void init() {
		try {
			ServerAddress serverAddress = new ServerAddress(host, port);
			mongo = new MongoClient(serverAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
