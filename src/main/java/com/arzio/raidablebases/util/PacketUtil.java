package com.arzio.raidablebases.util;

import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.arzio.raidablebases.data.BlockDamage;

import net.minecraft.server.v1_6_R3.Packet55BlockBreakAnimation;

/**
 * Packet utilities.
 * @author Arzio
 */
public class PacketUtil {

	@SuppressWarnings("deprecation")
	public static void sendBlockBreakParticlePacket(Player player, Block block) {
		player.playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
	}

	public static void sendBlockCrackAnimationPacket(Player player, BlockDamage blockDamage) {
		CraftPlayer cPlayer = (CraftPlayer) player;

		Block block = blockDamage.getBlock();

		// We need to fake the X position to hide the block damage
		// if the damage is below 0
		int posX = blockDamage.getDamage() > 0 ? block.getX() : 10000000;

		Packet55BlockBreakAnimation animation = new Packet55BlockBreakAnimation(
				generateFakePlayerIdFor(block.getX(), block.getY(), block.getZ()), // Fake player ID
				posX, block.getY(), block.getZ(),
				(int) (9D * (Math.min(blockDamage.getDamage(), blockDamage.getMaxHealth())
						/ blockDamage.getMaxHealth())));

		cPlayer.getHandle().playerConnection.sendPacket(animation);
	}

	public static int generateFakePlayerIdFor(int x, int y, int z) {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

}
