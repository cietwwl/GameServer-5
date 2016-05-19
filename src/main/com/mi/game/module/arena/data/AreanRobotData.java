package com.mi.game.module.arena.data;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = "com/mi/template/ArenaRobotPrototype.xml")
public class AreanRobotData extends BaseTemplate{
	private int level;
	private String name;
	private List<Integer> hero;
	private int photoID;
	
	public int getPhotoID() {
		return photoID;
	}
	public void setPhotoID(int photoID) {
		this.photoID = photoID;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Integer> getHero() {
		return hero;
	}
	public void setHero(String hero) {
		if(!hero.isEmpty() && hero != null){
			this.hero = new ArrayList<>();	
			String[] strList = hero.split(",");
			for(String str : strList){
				this.hero.add(Integer.parseInt(str));
			}
		}
		
	}
	
	
}
