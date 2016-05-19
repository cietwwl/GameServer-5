package com.mi.game.module.drop;

import java.util.List;

import com.mi.core.engine.TemplateManager;
import com.mi.game.defines.ErrorIds;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.drop.data.DropData;
import com.mi.game.module.drop.data.PackageItem;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.util.Utilities;

public class DropModule extends BaseModule {
	/**
	 * 掉落指定模板的物品
	 * 
	 * @param templateID
	 * @return
	 */
	public static GoodsBean doDrop(int templateID) {
		DropData dropData = TemplateManager.getTemplateData(templateID, DropData.class);
		int max = dropData.getMax();
		int random = Utilities.getRandomInt(max);
		List<PackageItem> list = dropData.getPackageItem();
		int weight = 0;
		GoodsBean goodsBean = null;
		for (PackageItem packageItem : list) {
			weight = packageItem.getWeight();
			if (weight >= random) {
				int itemID = packageItem.getItemID();
				int amount = packageItem.getAmount();
				goodsBean = new GoodsBean();
				goodsBean.setPid(itemID);
				goodsBean.setNum(amount);
				break;
			}
		}
		if(goodsBean == null){
			throw new IllegalArgumentException(ErrorIds.DropDataError + "");
		}
		return goodsBean;
	}
}
