package com.mi.game.module.gameserver.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * web容器方式启动的servlet
 * 
 * @author 李强 <br/>
 *
 * 创建时间：2014-3-11 上午10:16:25
 */
public class StartupServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 975875338926943385L;
	private static Logger logger = LoggerFactory.getLogger(StartupServlet.class);

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		if(ClassLoader.getSystemResource(".")==null){
			logger.info("tomcat方式启动");
			com.mi.core.startup.Main.start();
		}
	}

}
