package com.arzio.raidablebases.data;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.arzio.arziolib.api.wrapper.Base;
import com.arzio.raidablebases.util.BaseDamageCause;

/**
 * Holds all the data about the damage status of a {@link Block}.<br>
 * This class has direct connection with a {@link Base} through a
 * {@link BaseDamage} object.
 * 
 * @author Arzio
 */
public interface BlockDamage {

	/**
	 * Gets the referenced {@link BaseDamage} instance.
	 */
	public BaseDamage getBaseDamage();

	/**
	 * Gets the referenced {@link Block} instance.
	 */
	public Block getBlock();

	/**
	 * Gets the {@link Location} of this block.
	 */
	public Location getLocation();

	/**
	 * Causes a player damage to this block.
	 * 
	 * @param amount      Amount of damage
	 * @param damageCause Damage cause
	 * @param attacker    Player who is attacking
	 */
	public void causeDamage(double amount, BaseDamageCause damageCause, Player attacker);

	/**
	 * Gets the current damage this block has received until then. The
	 * <code>getHealth()</code> method gets the inverse.
	 */
	public double getDamage();

	/**
	 * Sets the current damage amount this block has received until then. The
	 * <code>setHealth()</code> method sets the inverse.
	 */
	public void setDamage(double amount);

	/**
	 * Gets the current health amount of this block.
	 */
	public double getHealth();

	/**
	 * Sets the current health amount of this block.
	 */
	public void setHealth(double amount);

	/**
	 * Gets the maximum health this block type can have.
	 */
	public double getMaxHealth();

	/**
	 * Checks if this block is damaged. In other words, checks if its health is less
	 * than the maximum.
	 */
	public boolean isDamaged();

	/**
	 * Repairs this block.
	 */
	public void repair();

	/**
	 * Destroys this block, setting it to air.
	 */
	public void destroy();

	/**
	 * Checks if this block is already destroyed.
	 */
	public boolean isDestroyed();

	/**
	 * Check if this block is a base center.
	 */
	public boolean isBaseCenter();
}
