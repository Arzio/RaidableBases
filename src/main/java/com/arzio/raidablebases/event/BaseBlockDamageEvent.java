package com.arzio.raidablebases.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.arzio.raidablebases.data.BaseDamage;
import com.arzio.raidablebases.data.BlockDamage;
import com.arzio.raidablebases.util.BaseDamageCause;

/**
 * Event about a damage against a base block.
 * @author Arzio
 */
public class BaseBlockDamageEvent extends BaseBlockEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private double amount;
	private final BaseDamageCause damageCause;
	private final Player attacker;
	private boolean cancelled;

	public BaseBlockDamageEvent(BaseDamage baseDamage, BlockDamage blockDamage, double amount,
			BaseDamageCause damageCause, Player attacker) {
		super(baseDamage, blockDamage);
		this.amount = amount;
		this.attacker = attacker;
		this.damageCause = damageCause;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public BaseDamageCause getDamageCause() {
		return damageCause;
	}

	public Player getAttacker() {
		return attacker;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
