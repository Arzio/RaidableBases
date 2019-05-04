package com.arzio.raidablebases.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.arzio.raidablebases.data.BaseDamage;
import com.arzio.raidablebases.data.BlockDamage;

/**
 * Event about a player repairing a base block.
 * @author Arzio
 *
 */
public class BaseBlockRepairEvent extends BaseBlockEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean shouldTakeFuel = true;
	private final Player player;
	private double healAmount;
	private boolean cancelled;

	public BaseBlockRepairEvent(BaseDamage baseDamage, BlockDamage blockDamage, Player player, double healAmount) {
		super(baseDamage, blockDamage);
		this.player = player;
		this.healAmount = healAmount;
	}

	public double getHealAmount() {
		return healAmount;
	}

	public void setHealAmount(double healAmount) {
		this.healAmount = healAmount;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean shouldTakeFuel() {
		return shouldTakeFuel;
	}

	public void setShouldTakeFuel(boolean shouldTakeFuel) {
		this.shouldTakeFuel = shouldTakeFuel;
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
