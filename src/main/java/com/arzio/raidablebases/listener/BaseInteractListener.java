package com.arzio.raidablebases.listener;

import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.YELLOW;

import org.bukkit.Bukkit;

import static org.bukkit.ChatColor.RED;

import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.wrapper.Base;
import com.arzio.raidablebases.RaidableBases;
import com.arzio.raidablebases.RaidableConfig;
import com.arzio.raidablebases.data.BaseDamage;
import com.arzio.raidablebases.data.BlockDamage;
import com.arzio.raidablebases.event.BaseBlockRepairEvent;
import com.arzio.raidablebases.util.BaseDamageUtil;

/**
 * Listener which listens for interaction with bases, like clicking on them.
 * 
 * @author Arzio
 *
 */
public class BaseInteractListener implements Listener {

	public static final int SCRAP_METAL_ID = 9731;

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBaseBlockClick(PlayerInteractEvent event) {

		if (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		Block clickedBlock = event.getClickedBlock();
		Player player = event.getPlayer();
		Base base = ArzioLib.getInstance().getBaseProvider().getBaseFromPart(clickedBlock);

		if (base == null) {
			return;
		}

		BaseDamage baseDamage = RaidableBases.getInstance().getBaseDamageProvider().getBaseDamage(base);
		BlockDamage blockDamage = baseDamage.getBlockDamage(clickedBlock);

		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			String healthBar = BaseDamageUtil.getHealthbar(blockDamage.getHealth(), blockDamage.getMaxHealth(), 30);

			player.sendMessage(" ");
			player.sendMessage(GREEN + " Block Health: " + YELLOW + healthBar);
			if (base.hasPermission(player) && RaidableConfig.INSTANCE.isRepairAllowed()) {
				player.sendMessage(
						GRAY + " You can restore its health by clicking it with a " + BOLD + "Wrench" + GRAY + ".");
				player.sendMessage(GRAY + " You also need a " + BOLD + "Scrap Metal" + GRAY + " in your inventory.");
			}
			player.sendMessage(" ");
		} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

			if (!blockDamage.isDamaged()) {
				return;
			}

			if (!RaidableConfig.INSTANCE.isRepairAllowed()) {
				return;
			}

			ItemStack repairTool = new ItemStack(RaidableConfig.INSTANCE.getRepairItemId());

			if (player.getInventory().getItemInHand().getType() != repairTool.getType()) {
				return;
			}

			ItemStack fuel = new ItemStack(SCRAP_METAL_ID);

			if (player.getInventory().containsAtLeast(fuel, 1)) {
				BaseBlockRepairEvent repairEvent = new BaseBlockRepairEvent(baseDamage, blockDamage, player,
						RaidableConfig.INSTANCE.getRepairHealthAmount());
				Bukkit.getPluginManager().callEvent(repairEvent);

				if (!repairEvent.isCancelled()) {
					if (repairEvent.shouldTakeFuel()) {
						player.getInventory().removeItem(fuel);
					}
					clickedBlock.getWorld().playSound(clickedBlock.getLocation(), Sound.ANVIL_USE, 1.1F, 1.1F);
					blockDamage.setHealth(blockDamage.getHealth() + repairEvent.getHealAmount());
				}
			} else {
				player.sendMessage(RED + " You need at least 1 " + BOLD + "Scrap Metal" + RED + " in your inventory.");
			}
		}
	}
}
