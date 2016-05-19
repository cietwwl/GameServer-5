package com.mi.game.module.base.bean.init;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

@XmlTemplate(template = {
	"com/mi/template/ConfigVersionPrototype.xml"
})
public class ConfigVersionData extends BaseTemplate {
	private int readType;
	private String type;
	private String url;
	private int version;
	private int fileSize;
	private int serverType;

	public int getReadType() {
		return readType;
	}

	public void setReadType(int readType) {
		this.readType = readType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public int getServerType() {
		return serverType;
	}

	public void setServerType(int serverType) {
		this.serverType = serverType;
	}
}
