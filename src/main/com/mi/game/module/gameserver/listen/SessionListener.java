package com.mi.game.module.gameserver.listen;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SessionListener implements HttpSessionListener{
	protected final static Logger logger = LoggerFactory.getLogger(SessionListener.class);
	  @Override
	  public void sessionCreated(HttpSessionEvent event) {      
//		  HttpSession ses = event.getSession();      
//		  String id = ses.getId()+ses.getCreationTime();      
		  
//		  SummerConstant.UserMap.put(id, Boolean.TRUE);     //添加用户      
	  }     
	  
	  @Override	         
	  public void sessionDestroyed(HttpSessionEvent event) {  
		  logger.debug("start destroy session");
		//   HttpSession session = event.getSession();      
		 //  String id = (String)session.getAttribute("id");
		   synchronized (this) {      
//				LoginModule module = (LoginModule)ModuleManager.getModule(ModuleNames.LoginModule);
//				logger.debug(id + "退出");
		   }      
	 }
}
