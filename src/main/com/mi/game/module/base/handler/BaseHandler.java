package com.mi.game.module.base.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mi.core.engine.IHandlerAdapter;
import com.mi.core.engine.IOMessage;
import com.mi.core.pojo.BaseEntity;
import com.mi.core.protocol.BaseProtocol;

/**
 * 基础handler
 * 
 * @author Administrator
 *
 */
public class BaseHandler extends IHandlerAdapter {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public BaseHandler() {

	}

	protected void writeError(IOMessage iomessage, int errCode) {
		iomessage.setOutputResult(new BaseProtocol(errCode));
	}

	protected void writeOk(IOMessage iomessage) {
		iomessage.setOutputResult(new BaseProtocol());
	}

	protected void writeProtocol(BaseProtocol protocol, IOMessage iomessage) {
		iomessage.setOutputResult(protocol);
	}

	protected void writeProtocol(IOMessage iomessage, int code) {
		iomessage.setOutputResult(new BaseProtocol(code));
	}

	/**
	 * 获取protocol信息
	 * 
	 * @param iomessage
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <A extends BaseProtocol> A getProtocol(IOMessage iomessage, Class<A> clazz) {
		if (iomessage.getProtocol() == null) { // 如果还未设置protocol
			try {
				A protocol = clazz.newInstance();
				iomessage.setProtocol(protocol);
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}

		return (A) iomessage.getProtocol();
	}

	public static List<Map<String, Object>> getResponseEntityArray(List<? extends BaseEntity> entityList, int type) {
		List<Map<String, Object>> responseMapArr = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < entityList.size(); i++) {
			BaseEntity entity = entityList.get(i);
			responseMapArr.add(entity.responseMap(type));
		}
		return responseMapArr;
	}
}
