package com.arzio.raidablebases.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.arzio.raidablebases.data.BaseDamage;
import com.arzio.raidablebases.data.BlockDamage;

/**
 * Event about the destruction of a base block, after its health getting to zero.
 * @author Arzio
 *
 */
public class BaseBlockDestroyEvent extends BaseBlockEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;

	public BaseBlockDestroyEvent(BaseDamage baseDamage, BlockDamage blockDamage) {
		super(baseDamage, blockDamage);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
