package com.arzio.raidablebases.data.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.arzio.arziolib.api.wrapper.Base;
import com.arzio.raidablebases.data.BaseDamage;
import com.arzio.raidablebases.data.BlockDamage;
import com.arzio.raidablebases.data.impl.BaseDamageImpl;
import com.arzio.raidablebases.data.impl.BlockDamageImpl;
import com.arzio.raidablebases.util.BaseDamageUtil;
import com.arzio.raidablebases.util.BlockStringLocation;

/**
 * YML implementation of a {@link BaseIOLoader}.
 * @author Arzio
 */
public class BaseYMLLoader implements BaseIOLoader {

	@Override
	public void save(File baseFile, BaseDamage baseDamage) throws IOException {
		FileConfiguration config = YamlConfiguration.loadConfiguration(baseFile);
		
		config.set("damage", null);
		
		for (BlockDamage part : baseDamage.getDamagedBlocks()) {
			config.set("damage."+BlockStringLocation.fromBlock(part.getBlock()).toNonDecimalString(), part.getDamage());
		}
		
		config.save(baseFile);
	}

	@Override
	public BaseDamage load(File baseFile, Base base) throws IOException {
		FileConfiguration config = YamlConfiguration.loadConfiguration(baseFile);
		BaseDamage baseDamage = new BaseDamageImpl(base);
		
		List<BlockDamage> blocksList = new ArrayList<BlockDamage>();
		
		if (config.contains("damage")) {
			for (String bruteLocation : config.getConfigurationSection("damage").getKeys(false)) {
				BlockStringLocation location = new BlockStringLocation(bruteLocation);
				if (base.isPartOfBase(location.asBlock())) {
					blocksList.add(new BlockDamageImpl(baseDamage, location, config.getInt("damage."+bruteLocation)));
				}
			}
		}
		
		baseDamage.setDamagedBlocks(blocksList);
		
		return baseDamage;
	}

	@Override
	public File getBaseFile(File folder, Base base) {
		return BaseDamageUtil.getBaseFile(folder, base, "yml");
	}

}
