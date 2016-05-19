package com.mi.game.module.mailBox.pojo;


public class MailInfo {
	private int mailType;
	private String name;
	private long createTime;
	private String message;
	private String sendPlayerID;
	private int status;
	private int index;
	private String title;
	private long reportID;

	public MailInfo(){}
	
	public MailInfo(String sendPlayerID,String name, String msg, int mailType,int status){
		this.sendPlayerID = sendPlayerID;
		this.name = name;
		this.message = msg;
		this.mailType = mailType;
		this.status = status;
	}
	
	public long getReportID() {
		return reportID;
	}
	public void setReportID(long reportID) {
		this.reportID = reportID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getSendPlayerID() {
		return sendPlayerID;
	}
	public void setSendPlayerID(String sendPlayerID) {
		this.sendPlayerID = sendPlayerID;
	}
	public int getMailType() {
		return mailType;
	}
	public void setMailType(int mailType) {
		this.mailType = mailType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
