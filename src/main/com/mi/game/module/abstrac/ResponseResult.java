package com.mi.game.module.abstrac;

public final class ResponseResult {
	public final static int OK = 1;
	/**权限不足**/
	public final static int PERMISSION = 2;
	/**用户不存在**/
	public final static int USERNULL = 3;
	/**登录超时**/
	public final static int TIMEOUT = 4;

	public final static int NULL = 0;
	public final static int ERROR = -1;

}