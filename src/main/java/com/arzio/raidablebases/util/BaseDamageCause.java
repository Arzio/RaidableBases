package com.arzio.raidablebases.util;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;

/**
 * Represents the damage cause of a damage event on a base.
 * @author Arzio
 *
 */
public enum BaseDamageCause {

	C4(4D, 2D), TNT(4D, 2D);

	private double size;
	private double damageAmount;

	BaseDamageCause(double size, double damageAmount) {
		this.size = size;
		this.damageAmount = damageAmount;
	}

	public double getSize() {
		return this.size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getDamageAmount() {
		return this.damageAmount;
	}
	
	public void setDamageAmount(double amount) {
		this.damageAmount = amount;
	}

	public void createExplosion(Entity entity) {
		CraftWorld craftWorld = (CraftWorld) entity.getWorld();
		Location location = entity.getLocation();
		craftWorld.getHandle().createExplosion(((CraftEntity) entity).getHandle(), location.getX(), location.getY(),
				location.getZ(), (float) this.getSize(), false, true);
	}
}
