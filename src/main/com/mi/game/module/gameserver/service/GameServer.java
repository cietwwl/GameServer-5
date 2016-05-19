package com.mi.game.module.gameserver.service;

import java.io.File;
import java.net.URL;

import com.mi.core.jetty.http.server.JettyHttpServer;
import com.mi.core.util.ConfigUtil;

public class GameServer {
	public final static int PORT = ConfigUtil.getInt("game.server.port", 8000);
	public final static int THREAD_NUM = ConfigUtil.getInt("game.server.threadNum", 2000);
	
	public  void start(){
		try{
			JettyHttpServer jettyHttpServer = new JettyHttpServer(PORT);
			JettyHttpServer.setThreadNum(THREAD_NUM);
			Thread.currentThread().getContextClassLoader();
			// 找到  WebRoot 目录，并设置resourceBase
			URL url = ClassLoader.getSystemResource(".");
			if(url == null){ // 如果不是直接用main函数启动
				return;
			}
			File f=new File(url.getFile());			
			String resourceBase = f.getParentFile().getParentFile().getAbsolutePath();			
			jettyHttpServer.setResourceBase(resourceBase);			
			jettyHttpServer.startServer();
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	
	public static void main(String[] args) {
		
	}
}
