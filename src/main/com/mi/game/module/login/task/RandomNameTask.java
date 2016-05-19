package com.mi.game.module.login.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;

import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.data.RandomNamePrototype;
import com.mi.game.util.Logs;

public class RandomNameTask extends TimerTask {

	@Override
	public void run() {

		try {
			long startTime = System.currentTimeMillis();
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			List<String> maleRandomName = new ArrayList<String>();
			List<String> femaleRandomName = new ArrayList<String>();
			List<RandomNamePrototype> dataList = TemplateManager.getTemplateList(RandomNamePrototype.class);
			List<String> surnameList = new ArrayList<>();
			List<String> mNameList = new ArrayList<>();
			List<String> wNameList = new ArrayList<>();
			for (RandomNamePrototype data : dataList) {
				surnameList.add(data.getSurname());
				mNameList.add(data.getManName());
				wNameList.add(data.getWomanName());
			}
			String name = "";
			for (String surname : surnameList) {
				for (String mName : mNameList) {
					if (mName == null || mName.isEmpty()) {
						continue;
					}
					name = surname + mName;
					//if (loginModule.getPlayerEntityByName(name) == null)
						maleRandomName.add(name);
				}
			}
			
			for (String surname : surnameList) {
				for (String wName : wNameList) {
					if (wName == null || wName.isEmpty()) {
						continue;
					}
					name = surname + wName;
					//if (loginModule.getPlayerEntityByName(name) == null)
						femaleRandomName.add(name);
				}
			}
			
			Collections.shuffle(maleRandomName);
			Collections.shuffle(femaleRandomName);

			System.out.println("初始化昵称:" + (System.currentTimeMillis() - startTime) + "ms");
			loginModule.setMaleRandomName(maleRandomName);
			loginModule.setFeManleRandomName(femaleRandomName);
		} catch (Exception e) {
			e.printStackTrace();
			Logs.logger.error("随机名字生成异常!");
		}
	}

}
