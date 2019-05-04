package com.arzio.raidablebases;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import com.arzio.arziolib.api.util.CDBaseMaterial;
import com.arzio.raidablebases.util.BaseDamageCause;

/**
 * Holds all the configuration data about the plugin.
 * @author Arzio
 */
public enum RaidableConfig {

	INSTANCE;

	private static final double DEFAULT_MATERIAL_RESISTANCE = 1D;
	private Map<CDBaseMaterial, Double> materialHealthMap = new HashMap<CDBaseMaterial, Double>();
	private boolean disableTNTdestroyingNonBaseBlocks;
	private boolean disableBaseCenterDamage;
	private boolean disableBaseCenterHologram;
	private int hologramSecondsShownAfterDamage;
	private int repairItemId;
	private double repairHealthAmount;

	public void loadData(FileConfiguration config) {

		this.hologramSecondsShownAfterDamage = config.getInt("hologram-seconds-shown-after-damage");
		this.disableTNTdestroyingNonBaseBlocks = config.getBoolean("disable-tnt-destroying-non-base-blocks");
		this.disableBaseCenterDamage = config.getBoolean("disable-base-center-damage");
		this.disableBaseCenterHologram = config.getBoolean("disable-base-center-hologram");
		this.repairItemId = config.getInt("repair-item-id");
		this.repairHealthAmount = config.getDouble("repair-health-amount");

		// ======================================

		for (CDBaseMaterial material : CDBaseMaterial.MATERIAL_SET) {
			if (!config.contains("material." + material.getName())) {
				config.set("material." + material.getName() + ".resistance", 2D);
			}
		}

		materialHealthMap.clear();
		for (CDBaseMaterial material : CDBaseMaterial.MATERIAL_SET) {
			materialHealthMap.put(material, config.getDouble("material." + material.getName() + ".resistance"));
		}

		// =====================================

		for (BaseDamageCause type : BaseDamageCause.values()) {
			if (!config.contains("explosion_type." + type.toString() + ".damage")) {
				config.set("explosion_type." + type.toString() + ".size", type.getSize());
				config.set("explosion_type." + type.toString() + ".damage", 1);
			}
		}

		for (BaseDamageCause type : BaseDamageCause.values()) {
			type.setDamageAmount(config.getDouble("explosion_type." + type.toString() + ".damage"));
			type.setSize(config.getDouble("explosion_type." + type.toString() + ".size"));
		}

		// ======================

		RaidableBases.getInstance().saveConfig();
	}

	public double getMaterialResistance(Block block) {
		CDBaseMaterial material = CDBaseMaterial.getMaterial(block);

		if (material == null) {
			return DEFAULT_MATERIAL_RESISTANCE;
		} else {
			return getMaterialResistance(material);
		}
	}

	public double getMaterialResistance(CDBaseMaterial material) {
		Double result = materialHealthMap.get(material);

		if (result == null) {
			result = DEFAULT_MATERIAL_RESISTANCE;
		}

		return result;
	}
	
	public boolean isRepairAllowed() {
		return this.getRepairItemId() >= 0;
	}

	public int getRepairItemId() {
		return repairItemId;
	}

	public double getRepairHealthAmount() {
		return repairHealthAmount;
	}

	public int getHologramSecondsShownAfterDamage() {
		return hologramSecondsShownAfterDamage;
	}

	public boolean shouldDisableTNTdestroyingNonBaseBlocks() {
		return disableTNTdestroyingNonBaseBlocks;
	}

	public boolean shouldDisableBaseCenterHologram() {
		return disableBaseCenterHologram;
	}

	public boolean shouldDisableBaseCenterDamage() {
		return disableBaseCenterDamage;
	}
}
