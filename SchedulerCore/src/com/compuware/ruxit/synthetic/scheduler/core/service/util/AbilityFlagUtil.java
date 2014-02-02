package com.compuware.ruxit.synthetic.scheduler.core.service.util;

import java.util.LinkedList;
import java.util.List;

import com.compuware.ruxit.synthetic.scheduler.core.dao.AbilityFlagDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.AbilityFlag;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIAbilityFlag;

public class AbilityFlagUtil {

	public static List<UIAbilityFlag> decodeAbilityFlags(AbilityFlagDao abilityFlagDao, long abilityF) {
		List<AbilityFlag> daoRequiresFlags = abilityFlagDao.getByBitMapField(abilityF);
		List<UIAbilityFlag> uiRequiresFlags = new LinkedList<>();
		for (AbilityFlag daoRequiresFlag : daoRequiresFlags) {
			uiRequiresFlags.add(UIAbilityFlag.create()
					.withAbilityFlag(daoRequiresFlag)
					.build());
		}
		return uiRequiresFlags;
	}

	public static long encodeAbilityFlags(AbilityFlagDao abilityFlagDao, List<String> requiresFlags) {
		long requiresF = 0L;
		for (String requiresFlag : requiresFlags) {
			AbilityFlag abilityFlag = abilityFlagDao.getByDescription(requiresFlag);
			if (abilityFlag == null) {
				throw new IllegalArgumentException(String.format("The specified ability flag '%s' does not exist", requiresFlag));
			}
			requiresF = abilityFlag.setFlag(requiresF);
		}
		return requiresF;
	}
	
	private AbilityFlagUtil () {}
	
}
