package com.mi.game.module.chat.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.job.BaseJob;
import com.mi.core.template.BaseTemplate;

@XmlTemplate(template = { "com/mi/template/jobs.xml" })
public class JobData extends BaseTemplate {
	/**
	 * 间隔
	 */
	private int interval = 0;
	/**
	 * 数量
	 */
	private int count = 0;
	/**
	 * 种类
	 */
	private String cls;
	private List<String> strParams = null;
	/**
	 * 开始时间
	 */
	private Date startTime;
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {

		Date oldDate = null;
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			oldDate = format.parse(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.startTime =oldDate;
	}
	public List<String> getList() {
		if(strParams == null){
			strParams = new ArrayList<String>();
		}
		return strParams;
	}
	public void setParams(List<ListContent> params) {
		strParams = new ArrayList<String>();
		for(ListContent element : params)
			strParams.add(element.toString());
	}
	public String getCls() {
		return cls;
	}
	public void setCls(String cls) {
		this.cls = cls;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseJob> T createObject() throws Exception {
		// TODO 自动生成的方法存根
		Class<?> classz = Class.forName(cls);
		if(classz==null) {
			throw new Exception("cls is null.");
		}
		BaseJob job = (BaseJob)classz.newInstance();
		job.setID(this.pid+"");
		job.setRepeatCount(this.count);
		job.setRepeatInterval(this.interval);
		Date date = new Date(System.currentTimeMillis() + 20000);
		job.setStartTime(date);			//指定时间开始运行
		job.addParam(BaseJob.USER_PARAMETER, this.strParams);
		return (T)job;
	}
}

class ListContent{
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		// TODO 自动生成的方法存根
		return content;
	}
}
