package com.mi.game.module.mailBox.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.mailBox.pojo.MailBoxEntity;

public class MailBoxEntityDAO extends AbstractBaseDAO<MailBoxEntity>{
	private static final MailBoxEntityDAO mailBoxEntityDAO = new MailBoxEntityDAO();
	private MailBoxEntityDAO (){}
	public static MailBoxEntityDAO getInstance(){
		return mailBoxEntityDAO;
	}
}
