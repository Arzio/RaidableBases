package com.arzio.raidablebases.data;

import java.util.List;

import org.bukkit.block.Block;

import com.arzio.arziolib.api.BaseProvider;
import com.arzio.arziolib.api.wrapper.Base;
import com.arzio.raidablebases.event.BaseBlockDamageEvent;

/**
 * Holds all the damage data a {@link Base} can carry. Every damage information
 * about a {@link Base} should be here.
 * 
 * @author Arzio
 *
 */
public interface BaseDamage {

	/**
	 * Gets the referred base.
	 * 
	 * @return {@link Base} instance. It is never null.
	 */
	public Base getBase();

	/**
	 * Repairs all the damaged base blocks.
	 */
	public void repairAll();

	/**
	 * Gets the damage information about a block.
	 * 
	 * @param block {@link Block} inside the base region.
	 * @return {@link BlockDamage} instance if the block is inside the base region.
	 *         <code>null</code> if otherwise.
	 */
	public BlockDamage getBlockDamage(Block block);

	/**
	 * Removes every damage information about a {@link BlockDamage}, repairing it.
	 * This is equal to using the <code>BlockDamage.repair()</code> method.
	 * 
	 * @param blockDamage
	 */
	public void removeDamage(BlockDamage blockDamage);

	/**
	 * Gets a {@link List} copy of all the current damaged blocks.<br>
	 * Blocks are only damaged if their health is not equal to its maximum.
	 * 
	 * @return A new {@link List} with all the current damaged blocks.
	 */
	public List<BlockDamage> getDamagedBlocks();
	
	/**
	 * Sets the list of currently damaged blocks.<br>
	 * Blocks are only damaged if their health is not equal to its maximum.
	 * 
	 * @param list The block damage list
	 */
	public void setDamagedBlocks(List<BlockDamage> list);

	/**
	 * Gets the max health of the base center block.
	 */
	public double getMaxHealth();

	/**
	 * Gets the current health of the base center block.
	 */
	public double getHealth();

	/**
	 * Shows the healthbar hologram of this base for a amount of time.<br>
	 * Can be used at any time since this base is not destroyed.
	 * 
	 * @param seconds Amount of seconds (not ticks) to show the hologram.
	 */
	public void showHologram(int seconds);

	/**
	 * Hides the healthbar hologram of this base until be shown again.<br>
	 * Can be used at any time since this base is not destroyed.
	 */
	public void hideHologram();

	/**
	 * Checks if the healthbar hologram is valid. In other words, checks if it is
	 * being shown.
	 * 
	 * @return <code>true</code> if the healthbar hologram is visible and not
	 *         deleted. <code>false</code> otherwise.
	 */
	public boolean isHologramValid();

	/**
	 * Checks if this base is waiting to be saved.
	 * 
	 * @return <code>true</code> if this base is waiting to be saved.
	 *         <code>false</code> if otherwise.
	 */
	public boolean isDirty();

	/**
	 * Sets the dirty status of this base. The <b>dirty</b> concept is used to check
	 * if a base is waiting to be saved.<br>
	 * <br>
	 * The {@link BaseProvider} implementation should take care of setting this to
	 * <code>false</code> after saving it.
	 * 
	 * @param isDirty current state
	 */
	public void setDirty(boolean isDirty);

	/**
	 * Gets the last successful damage event which happened against this base.
	 * @return The last base damage event.
	 */
	public BaseBlockDamageEvent getLastSuccessfulDamage();

	/**
	 * Sets the last successful damage event this base was taken.
	 */
	public void setLastSuccessfulDamage(BaseBlockDamageEvent event);
}
