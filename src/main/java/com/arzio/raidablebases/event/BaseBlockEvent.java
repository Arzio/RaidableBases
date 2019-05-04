package com.arzio.raidablebases.event;

import com.arzio.raidablebases.data.BaseDamage;
import com.arzio.raidablebases.data.BlockDamage;

/**
 * Base class to be used in any event about a block of a base.
 * @author Arzio
 */
public abstract class BaseBlockEvent extends RaidableBaseEvent {

	private final BlockDamage blockDamage;

	public BaseBlockEvent(BaseDamage baseDamage, BlockDamage blockDamage) {
		super(baseDamage);
		this.blockDamage = blockDamage;
	}

	public BlockDamage getBlockDamage() {
		return blockDamage;
	}

}
