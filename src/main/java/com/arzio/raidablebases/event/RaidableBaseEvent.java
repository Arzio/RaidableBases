package com.arzio.raidablebases.event;

import org.bukkit.event.Event;

import com.arzio.raidablebases.data.BaseDamage;

/**
 * Base class to be used in any event about any bases around this plugin.
 * @author Arzio
 */
public abstract class RaidableBaseEvent extends Event {

	private final BaseDamage baseDamage;

	public RaidableBaseEvent(BaseDamage baseDamage) {
		this.baseDamage = baseDamage;
	}

	public BaseDamage getBaseDamage() {
		return this.baseDamage;
	}
}
