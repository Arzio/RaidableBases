package com.arzio.raidablebases.util;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.arzio.arziolib.api.wrapper.Base;
import com.arzio.raidablebases.data.BaseDamage;
import com.arzio.raidablebases.data.BlockDamage;

/**
 * External utilities for bases.
 * @author Arzio
 */
public class BaseDamageUtil {

	private static final int MAX_BASE_UPDATE_DISTANCE = 100;
	
	public static File getBaseFile(File folder, Base base, String fileExtension) {
		Location location = base.getLocation();
		return new File(folder,
				"base_" + location.getX() + "_" + location.getY() + "_" + location.getZ() + "." + fileExtension);
	}

	public static void broadcastBaseDamage(BaseDamage base) {
		for (Player player : base.getBase().getLocation().getWorld().getPlayers()) {
			showBaseDamage(base, player);
		}
	}

	public static void broadcastBlockDamage(BlockDamage blockDamage) {
		for (Player player : blockDamage.getLocation().getWorld().getPlayers()) {
			if (BaseDamageUtil.isPlayerCloseEnoughToSeeDamage(player, blockDamage.getBaseDamage())) {
				PacketUtil.sendBlockCrackAnimationPacket(player, blockDamage);
			}
		}
	}

	public static void showBaseDamage(BaseDamage damage, Player player) {
		if (BaseDamageUtil.isPlayerCloseEnoughToSeeDamage(player, damage)) {
			for (BlockDamage block : damage.getDamagedBlocks()) {
				PacketUtil.sendBlockCrackAnimationPacket(player, block);
			}
		}
	}

	private static boolean isPlayerCloseEnoughToSeeDamage(Player player, BaseDamage base) {
		return base.getBase().getLocation().getWorld().equals(player.getWorld())
				&& base.getBase().getLocation().distance(player.getLocation()) < MAX_BASE_UPDATE_DISTANCE;
	}

	public static String getHealthbar(double currentHealth, double maxHealth, int healthBarScale) {
		StringBuilder sb = new StringBuilder();
		sb.append("§a");
		
		double healthPercent = currentHealth / maxHealth;
		boolean alreadyHasColor = false;

		for (int i = 0; i < healthBarScale; i++) {
			double barPercent = (double) (i) / (double) healthBarScale;

			if (barPercent >= healthPercent && !alreadyHasColor) {
				sb.append("§8");
				alreadyHasColor = true;
			}
			sb.append("||");
		}
		return sb.toString();
	}
}
