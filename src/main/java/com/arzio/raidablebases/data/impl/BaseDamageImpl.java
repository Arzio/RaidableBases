package com.arzio.raidablebases.data.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

import com.arzio.arziolib.api.wrapper.Base;
import com.arzio.raidablebases.RaidableBases;
import com.arzio.raidablebases.data.BaseDamage;
import com.arzio.raidablebases.data.BlockDamage;
import com.arzio.raidablebases.event.BaseBlockDamageEvent;
import com.arzio.raidablebases.util.BaseDamageUtil;
import com.arzio.raidablebases.util.BlockStringLocation;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

public class BaseDamageImpl implements BaseDamage {

	private Base base;
	private Hologram hologram;
	private BukkitTask taskHideHologram;
	private boolean isDirty;
	private BaseBlockDamageEvent lastDamage;

	private List<BlockDamage> damagedBlocks;

	public BaseDamageImpl(Base base) {
		this.damagedBlocks = new ArrayList<BlockDamage>();
		this.base = base;
	}

	@Override
	public void repairAll() {
		for (BlockDamage block : this.getDamagedBlocks()) {
			block.repair();
		}
	}

	@Override
	public Base getBase() {
		return this.base;
	}

	@Override
	public List<BlockDamage> getDamagedBlocks() {
		return Collections.unmodifiableList(new ArrayList<BlockDamage>(this.damagedBlocks));
	}

	@Override
	public double getMaxHealth() {
		return this.getBlockDamage(this.getBase().getBlock()).getMaxHealth();
	}

	@Override
	public double getHealth() {
		return this.getBlockDamage(this.getBase().getBlock()).getHealth();
	}

	@Override
	public BlockDamage getBlockDamage(Block block) {
		if (!this.getBase().isPartOfBase(block)) {
			return null;
		}

		BlockDamage blockDamage = null;

		// Search for the already existing base block
		for (BlockDamage damage : damagedBlocks) {
			if (damage.getBlock().equals(block)) {
				blockDamage = damage;
				break;
			}
		}

		// Haven't found
		if (blockDamage == null) {
			blockDamage = new BlockDamageImpl(this, BlockStringLocation.fromBlock(block), 0);
			damagedBlocks.add(blockDamage);
		}

		return blockDamage;
	}

	@Override
	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

	@Override
	public boolean isDirty() {
		return this.isDirty;
	}

	@Override
	public void showHologram(int seconds) {
		double maxHealth = getMaxHealth();
		double currentHealth = getHealth();

		if (this.getBase().isValid() && currentHealth > 0D) {

			if (!isHologramValid()) {
				this.hologram = HologramsAPI.createHologram(RaidableBases.getInstance(),
						this.getBase().getLocation().add(0.5D, 2D, 0.5D));
			}

			this.hologram.clearLines();
			this.hologram.appendTextLine(ChatColor.RED + "❤ " + BaseDamageUtil.getHealthbar(currentHealth, maxHealth, 20));

			if (taskHideHologram != null) {
				taskHideHologram.cancel();
			}

			taskHideHologram = Bukkit.getScheduler().runTaskLater(RaidableBases.getInstance(), new Runnable() {
				@Override
				public void run() {
					if (isHologramValid()) {
						hideHologram();
					}
				}
			}, seconds * 20L);

		} else {
			this.hologram.delete();
		}
	}

	@Override
	public void hideHologram() {
		this.hologram.delete();
	}

	@Override
	public boolean isHologramValid() {
		return this.hologram != null && !this.hologram.isDeleted();
	}

	@Override
	public void removeDamage(BlockDamage blockDamage) {
		this.damagedBlocks.remove(blockDamage);
		if (blockDamage.isDamaged()) {
			blockDamage.repair();
		}
	}

	@Override
	public BaseBlockDamageEvent getLastSuccessfulDamage() {
		return lastDamage;
	}

	@Override
	public void setLastSuccessfulDamage(BaseBlockDamageEvent event) {
		this.lastDamage = event;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getBase() == null) ? 0 : this.getBase().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseDamageImpl other = (BaseDamageImpl) obj;
		if (this.getBase() == null) {
			if (other.getBase() != null)
				return false;
		} else if (!this.getBase().equals(other.getBase()))
			return false;
		return true;
	}

	@Override
	public void setDamagedBlocks(List<BlockDamage> list) {
		this.damagedBlocks = list;
	}

}
