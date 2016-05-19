package com.mi.game.util;

import java.io.File;
import java.net.URL;

public class ShowAllFile {
	public static void main(String[] args) {
		StringBuffer liunx = new StringBuffer();
		Thread.currentThread().getContextClassLoader();
		URL url = ClassLoader.getSystemResource(".");
		File f=new File(url.getFile());	
		String path = f.getParent() + "/lib";
		System.out.println(path);
		File dirPath = new File(path);
		
		if(dirPath.exists() && dirPath.isDirectory()){
			String[] subFile = dirPath.list();
			
			if(subFile != null){
				System.out.println("windos下配置文件：");
				System.out.print("set TMP_LIB_APTH=");
				for(String item:subFile){
					if(item.endsWith(".jar")){
						System.out.print("%LIB_PATH%\\"+item+";");
						liunx.append(":$GODS_LIB_PATH/"+item);
					}
				}
				
				System.out.println("");
				System.out.println("liunx下配置文件：");
				System.out.println("TMP_LIB_APTH="+liunx.substring(1));
			}
		}
	}
}
