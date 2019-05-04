package com.arzio.raidablebases.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Almost a copy of a {@link Block} location.<br>
 * <br>
 * It is used to avoid using the same {@link Location} object on different
 * moments of the plugin lifetime.
 * 
 * @author Arzio
 *
 */
public class BlockStringLocation {
	private final String worldName;
	private final int x;
	private final int y;
	private final int z;

	public BlockStringLocation(String nonDecimalLocation) {
		String[] location = nonDecimalLocation.split("_");

		this.worldName = location[0];
		this.x = (int) Double.parseDouble(location[1]);
		this.y = (int) Double.parseDouble(location[2]);
		this.z = (int) Double.parseDouble(location[3]);
	}

	public BlockStringLocation(String worldName, int x, int y, int z) {
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String getWorldName() {
		return this.worldName;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}

	public Block asBlock() {
		return this.asLocation().getBlock();
	}

	public Location asLocation() {
		return new Location(Bukkit.getWorld(this.worldName), this.x, this.y, this.z);
	}

	public static BlockStringLocation fromBlock(Block location) {
		return new BlockStringLocation(location.getWorld().getName(), location.getX(), location.getY(),
				location.getZ());
	}

	public String toNonDecimalString() {
		return this.getWorldName() + "_" + this.getX() + "_" + this.getY() + "_" + this.getZ();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((worldName == null) ? 0 : worldName.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
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
		BlockStringLocation other = (BlockStringLocation) obj;
		if (worldName == null) {
			if (other.worldName != null)
				return false;
		} else if (!worldName.equals(other.worldName))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

}
