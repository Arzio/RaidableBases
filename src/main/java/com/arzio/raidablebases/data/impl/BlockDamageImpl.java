package com.arzio.raidablebases.data.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.arzio.arziolib.api.util.CDBaseMaterial;
import com.arzio.raidablebases.RaidableConfig;
import com.arzio.raidablebases.data.BaseDamage;
import com.arzio.raidablebases.data.BlockDamage;
import com.arzio.raidablebases.event.BaseBlockDamageEvent;
import com.arzio.raidablebases.event.BaseBlockDestroyEvent;
import com.arzio.raidablebases.util.BaseDamageCause;
import com.arzio.raidablebases.util.BaseDamageUtil;
import com.arzio.raidablebases.util.BlockStringLocation;
import com.arzio.raidablebases.util.PacketUtil;

public class BlockDamageImpl implements BlockDamage {

	private boolean isDestroyed = false;
	private final BaseDamage baseDamage;
	
	private BlockStringLocation blockLocation;
	private double damage;

	public BlockDamageImpl(BaseDamage baseDamage, BlockStringLocation bruteLocation, int damage) {
		this.blockLocation = bruteLocation;
		this.damage = damage;
		this.baseDamage = baseDamage;
	}

	public BlockStringLocation getBruteLocation() {
		return this.blockLocation;
	}

	@Override
	public Block getBlock() {
		return this.getBruteLocation().asBlock();
	}

	@Override
	public Location getLocation() {
		return this.getBruteLocation().asLocation();
	}

	@Override
	public double getMaxHealth() {
		return RaidableConfig.INSTANCE.getMaterialResistance(this.getBlock());
	}

	@Override
	public void setDamage(double amount) {
		double damageBefore = this.damage;
		this.damage = amount;

		// Updates the health hologram, showing it for some time
		if (this.isBaseCenter() && !RaidableConfig.INSTANCE.shouldDisableBaseCenterHologram()) {
			this.getBaseDamage().showHologram(RaidableConfig.INSTANCE.getHologramSecondsShownAfterDamage());
		}

		if (this.getHealth() <= 0) {
			this.destroy();

			if (this.getMaxHealth() > 0D) {
				this.getLocation().getWorld().playSound(this.getLocation(), Sound.DIG_STONE, 1.2F, 1.2F);
			}
		} else {
			if (!this.isDamaged()) {
				baseDamage.removeDamage(this);
			}
			BaseDamageUtil.broadcastBlockDamage(this);
		}

		// The block has taken damage, so we broadcast some particle packets
		if (damageBefore < amount) {
			for (Player player : this.getLocation().getWorld().getPlayers()) {
				PacketUtil.sendBlockBreakParticlePacket(player, this.getBlock());
				PacketUtil.sendBlockBreakParticlePacket(player, this.getBlock());
			}
		}

		baseDamage.setDirty(true);
	}

	@Override
	public void repair() {
		this.setDamage(0D);
	}

	@Override
	public void destroy() {
		if (this.isDestroyed) {
			return;
		}

		BaseBlockDestroyEvent destroyEvent = new BaseBlockDestroyEvent(getBaseDamage(), this);
		Bukkit.getPluginManager().callEvent(destroyEvent);

		if (!destroyEvent.isCancelled()) {
			this.isDestroyed = true;
			this.getBaseDamage().removeDamage(this);

			if (CDBaseMaterial.isCenter(this.getBlock())) {
				this.getBaseDamage().getBase().destroy();
			} else {
				this.getBlock().setType(Material.AIR);
			}
		}
	}

	@Override
	public boolean isDestroyed() {
		return this.isDestroyed;
	}

	@Override
	public void causeDamage(double amount, BaseDamageCause damageCause, Player attacker) {
		BaseBlockDamageEvent damageEvent = new BaseBlockDamageEvent(this.getBaseDamage(), this, amount, damageCause, attacker);
		Bukkit.getPluginManager().callEvent(damageEvent);

		if (!damageEvent.isCancelled()) {
			this.getBaseDamage().setLastSuccessfulDamage(damageEvent);
			this.setDamage(this.damage + amount);
		}
	}

	@Override
	public BaseDamage getBaseDamage() {
		return this.baseDamage;
	}

	@Override
	public boolean isBaseCenter() {
		return CDBaseMaterial.isCenter(this.getBlock());
	}

	@Override
	public double getHealth() {
		return getMaxHealth() - this.getDamage();
	}

	@Override
	public double getDamage() {
		return this.damage;
	}

	@Override
	public void setHealth(double amount) {
		this.setDamage(this.getMaxHealth() - amount);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blockLocation == null) ? 0 : blockLocation.hashCode());
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
		BlockDamageImpl other = (BlockDamageImpl) obj;
		if (blockLocation == null) {
			if (other.blockLocation != null)
				return false;
		} else if (!blockLocation.equals(other.blockLocation))
			return false;
		return true;
	}

	@Override
	public boolean isDamaged() {
		return this.getHealth() < this.getMaxHealth();
	}
	
}
