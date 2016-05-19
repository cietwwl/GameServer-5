package com.mi.game.module.chat.classes;

/**
 * 频道消息块
 * @author Administrator
 */
public class MessageBlock {
	private long timestamp;
	private Message content;
	
	public MessageBlock(long timestamp,Message content){
		this.timestamp = timestamp;
		this.content = content;
	}
	
	public MessageBlock(){}
	
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public Message getContent() {
		return content;
	}
	public void setContent(Message content) {
		this.content = content;
	}
}
