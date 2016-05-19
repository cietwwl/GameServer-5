package com.mi.game.module.relation;

import java.util.List;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.Module;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.mailBox.MailBoxModule;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.relation.dao.MasterEntityDao;
import com.mi.game.module.relation.dao.PupilEntityDao;
import com.mi.game.module.relation.define.RelationError;
import com.mi.game.module.relation.pojo.MasterEntity;
import com.mi.game.module.relation.pojo.PupilEntity;
import com.mi.game.module.relation.protocol.MasterProtocol;
import com.mi.game.module.relation.protocol.PupilProtocol;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.util.Utilities;

@Module(name = ModuleNames.RelationModule, clazz = RelationModule.class)
public class RelationModule extends BaseModule {

	private static RewardModule rewardModule;
	private static LoginModule loginModule;
	private static PayModule payModule;
	private static MailBoxModule mailBoxModule;
	private static final MasterEntityDao masterDao = MasterEntityDao.getInstance();
	private static final PupilEntityDao pupilDao = PupilEntityDao.getInstance();

	/**
	 * 收徒
	 * 
	 * @param playerID
	 * @param pupilID
	 * @param ioMessage
	 */
	public void recruit(String playerID, String pupilID, IOMessage ioMessage) {
		MasterProtocol protocol = new MasterProtocol();
		ioMessage.setProtocol(protocol);
		// 是否收自己
		if (playerID.equals(pupilID)) {
			protocol.setCode(RelationError.MASTER_NOT_MYSELF);
			return;
		}
		MasterEntity masterEntity = initMasterEntity(playerID);
		// 惩罚时间
		if (masterEntity.getCoolingTime() > 0) {
			protocol.setCode(RelationError.HAS_COOLINGTIME);
			return;
		}
		// 申请列表
		if (masterEntity.applyIsFull()) {
			protocol.setCode(RelationError.APPLY_NUM_ISFULL);
			return;
		}
		// 申请是否已存在
		if (masterEntity.isHasApply(pupilID)) {
			protocol.setCode(RelationError.APPLY_IS_HAS);
			return;
		}
		PupilEntity pupilEntity = initPupilEntity(pupilID);
		// 拜师开关
		if (!pupilEntity.isOpen()) {
			protocol.setCode(RelationError.PUPIL_NOT_OPEN);
			return;
		}
		// 是否已拜师
		if (pupilEntity.isHasMaster()) {
			protocol.setCode(RelationError.PUPIL_HAS_MASTER);
			return;
		}
		masterEntity.addApplys(pupilID);
		// 列表增加申请
		pupilEntity.addApplys(playerID);

		// 发送邮件通知
		// ///
		// ////////

		saveMasterEntity(masterEntity);
		savePupilEntity(pupilEntity);
	}

	/**
	 * 拜师
	 * 
	 * @param playerID
	 * @param masterID
	 * @param ioMessage
	 */
	public void apprentice(String playerID, String masterID, IOMessage ioMessage) {
		PupilProtocol protocol = new PupilProtocol();
		ioMessage.setProtocol(protocol);
		// 是否拜自己
		if (playerID.equals(masterID)) {
			protocol.setCode(RelationError.PUPIL_NOT_MYSELF);
			return;
		}
		PupilEntity pupilEntity = initPupilEntity(playerID);
		MasterEntity masterEntity = initMasterEntity(masterID);
		// 收徒开关
		if (!masterEntity.isOpen()) {
			protocol.setCode(RelationError.MASTER_NOT_OPEN);
			return;
		}
		// 徒弟数量
		if (masterEntity.pupilIsFull()) {
			protocol.setCode(RelationError.PUPILS_ISFULL);
			return;
		}
		// 惩罚时间
		if (pupilEntity.getCoolingTime() > 0) {
			protocol.setCode(RelationError.HAS_COOLINGTIME);
			return;
		}
		// 礼包是否领取
		if (!pupilEntity.isOpen()) {
			// 增加礼包物品
			// 发送邮件

		}

		// 设置师傅id
		pupilEntity.setMasterID(masterID);
		saveMasterEntity(masterEntity);
	}

	/**
	 * 初始化师父实体
	 * 
	 * @param masterID
	 * @return
	 */
	public MasterEntity initMasterEntity(String masterID) {
		MasterEntity entity = getMasterEntity(masterID);
		if (entity == null) {
			entity = new MasterEntity();
			entity.setPlayerID(masterID);
			entity.setDateTime(Utilities.getDateTime());
			entity.setOpen(true);
			saveMasterEntity(entity);
		}
		return entity;
	}

	/**
	 * 初始化徒弟实体
	 * 
	 * @param pupilID
	 * @return
	 */
	public PupilEntity initPupilEntity(String pupilID) {
		PupilEntity entity = getPupilEntity(pupilID);
		if (entity == null) {
			entity = new PupilEntity();
			entity.setPlayerID(pupilID);
			entity.setDateTime(Utilities.getDateTime());
			entity.setOpen(true);
			savePupilEntity(entity);
		}
		return entity;
	}

	@Override
	public void init() {
		rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		payModule = ModuleManager.getModule(ModuleNames.PayModule, PayModule.class);
		mailBoxModule = ModuleManager.getModule(ModuleNames.MailBoxModule, MailBoxModule.class);
	}

	public void saveMasterEntity(MasterEntity masterEntity) {
		masterDao.save(masterEntity);
	}

	public MasterEntity getMasterEntity(String masterID) {
		return masterDao.getMasterEntity(masterID);
	}

	public void savePupilEntity(PupilEntity pupilEntity) {
		pupilDao.save(pupilEntity);
	}

	public PupilEntity getPupilEntity(String pupilID) {
		return pupilDao.getPupilEntity(pupilID);
	}


	/**
	 * 拜师管理
	 * 
	 * @param playerID
	 * @param ioMessage
	 */
	public void pupilManage(String playerID, IOMessage ioMessage) {
		// TODO Auto-generated method stub

	}

	/**
	 * 收徒管理
	 * 
	 * @param playerID
	 * @param ioMessage
	 */
	public void masterManage(String playerID, IOMessage ioMessage) {

	}
	
	/**
	 * 获取徒弟列表
	 * 
	 * @param playerID
	 * @param ioMessage
	 */
	public void pupilList(String playerID, IOMessage ioMessage) {
		PupilProtocol protocol = new PupilProtocol();
		ioMessage.setProtocol(protocol);
		List<PlayerEntity> pupliList = loginModule.getaActivePlayerList(1, 30, false);
		protocol.setPupilList(pupliList);
	}

	/**
	 * 获取师傅列表
	 * 
	 * @param playerID
	 * @param ioMessage
	 */
	public void masterList(String playerID, IOMessage ioMessage) {
		MasterProtocol protocol = new MasterProtocol();
		ioMessage.setProtocol(protocol);
		List<PlayerEntity> masterList = loginModule.getaActivePlayerList(5, 30, true);
		protocol.setMasterList(masterList);
	}
}
