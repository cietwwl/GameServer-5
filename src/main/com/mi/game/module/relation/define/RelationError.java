package com.mi.game.module.relation.define;

public class RelationError {

	/** 不能收自己为徒 **/
	public static final int MASTER_NOT_MYSELF = 60000;
	/** 不能拜自己为师 **/
	public static final int PUPIL_NOT_MYSELF = 60001;
	/** 收徒开关没有打开 **/
	public static final int MASTER_NOT_OPEN = 60002;
	/** 拜师开关没有打开 **/
	public static final int PUPIL_NOT_OPEN = 60003;
	/** 申请列表已满 **/
	public static final int APPLY_NUM_ISFULL = 60004;
	/** 申请列表已存在 **/
	public static final int APPLY_IS_HAS = 60005;
	/** 已经有师傅 **/
	public static final int PUPIL_HAS_MASTER = 60006;
	/** 惩罚时间中 **/
	public static final int HAS_COOLINGTIME = 60007;
	/** 徒弟列表已满 **/
	public static final int PUPILS_ISFULL = 60008;

}
