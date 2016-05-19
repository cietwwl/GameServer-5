package com.mi.game.module.pay.pojo;

import com.mi.core.pojo.BaseEntity;

public class AppstoreReceiptEntity extends BaseEntity {

	private static final long serialVersionUID = 1697676438565756836L;

	private String receiptMD5;

	public String getReceiptMD5() {
		return receiptMD5;
	}

	public void setReceiptMD5(String receiptMD5) {
		this.receiptMD5 = receiptMD5;
	}

	@Override
	public Object getKey() {
		return receiptMD5;
	}

	@Override
	public String getKeyName() {
		return "receiptMD5";
	}

	@Override
	public void setKey(Object key) {
		this.receiptMD5 = key.toString();
	}

}
