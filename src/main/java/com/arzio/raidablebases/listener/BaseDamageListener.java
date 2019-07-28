package com.arzio.raidablebases.listener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import com.arzio.arziolib.api.BaseProvider;
import com.arzio.arziolib.api.util.CDBaseMaterial;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.api.util.EntityUtil;
import com.arzio.arziolib.api.wrapper.Base;
import com.arzio.raidablebases.RaidableConfig;
import com.arzio.raidablebases.data.BaseDamage;
import com.arzio.raidablebases.data.BaseDamageProvider;
import com.arzio.raidablebases.data.BlockDamage;
import com.arzio.raidablebases.event.BaseBlockDamageEvent;
import com.arzio.raidablebases.util.BaseDamageCause;

/**
 * Listens for explosions and applies the damage to bases.
 * @author Arzio
 */
public class BaseDamageListener implements Listener {

	private final BaseProvider baseProvider;
	private final BaseDamageProvider damageProvider;

	public BaseDamageListener(BaseProvider baseProvider, BaseDamageProvider damageProvider) {
		this.baseProvider = baseProvider;
		this.damageProvider = damageProvider;
	}

	@EventHandler
	public void onBaseBlockDamage(BaseBlockDamageEvent event) {
		if (event.getBlockDamage().isBaseCenter() && RaidableConfig.INSTANCE.shouldDisableBaseCenterDamage()) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onExplode(EntityExplodeEvent event) {
		
		List<Block> unmodifiedExplodedBlocksList = new ArrayList<>(event.blockList());
		
		// Disables explosion damage for all blocks except TNT
		if (RaidableConfig.INSTANCE.shouldDisableTNTdestroyingNonBaseBlocks()) {
			Iterator<Block> blockIt = event.blockList().iterator();
			while (blockIt.hasNext()) {
				Block it = blockIt.next();

				if (it.getType() != Material.TNT) {
					blockIt.remove();
				}
			}
		}

		Player attacker = null;
		BaseDamageCause type = null;
		
		if (event.getEntity() != null) {
			if (event.getEntity().getType() == EntityType.PRIMED_TNT) {
				type = BaseDamageCause.TNT;
				attacker = (Player) ((TNTPrimed) event.getEntity()).getSource();
			}
			if (CDEntityType.C4.isTypeOf(event.getEntity())) {
				type = BaseDamageCause.C4;
				attacker = Bukkit.getPlayerExact(EntityUtil.getC4OwnerName(event.getEntity()));
			}
		}

		if (type == null || attacker == null) {
			return;
		}

		for (Block explodedBlock : unmodifiedExplodedBlocksList) {
			Base base = baseProvider.getBaseFromPart(explodedBlock);
			if (base != null) {
				BlockDamage blockDamage = damageProvider.getBaseDamage(base).getBlockDamage(explodedBlock);
				double amount = type.getDamageAmount()
						/ (1D + (blockDamage.getLocation().distance(event.getLocation()) * 1.5D));
				blockDamage.causeDamage(amount, type, attacker);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBreak(BlockBreakEvent event) {
		if (!CDBaseMaterial.isCenter(event.getBlock())){
			Base base = baseProvider.getBaseFromPart(event.getBlock());

			if (base != null && base.isOwner(event.getPlayer())){
				BaseDamage baseDamage = this.damageProvider.getBaseDamage(base);
				BlockDamage block = baseDamage.getBlockDamage(event.getBlock());
				
				block.destroy();
			}
		}
	}

	@EventHandler
	public void updateTNTExplosionSize(ExplosionPrimeEvent event) {
		if (event.getEntityType() == EntityType.PRIMED_TNT) {
			event.setRadius((float) BaseDamageCause.TNT.getSize());
		}
	}

}
