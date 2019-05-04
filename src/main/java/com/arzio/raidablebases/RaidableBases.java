package com.arzio.raidablebases;

import org.bukkit.plugin.java.JavaPlugin;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.BaseProvider;
import com.arzio.raidablebases.data.BaseDamageProvider;
import com.arzio.raidablebases.data.impl.BaseDamageProviderImpl;
import com.arzio.raidablebases.listener.BaseDamageListener;
import com.arzio.raidablebases.listener.BaseInteractListener;
import com.arzio.raidablebases.listener.C4FixListener;

/**
 * Main instance of this plugin.<br>
 * <br>
 * I would like to thank all the current CD Team for the opportunity to help the
 * CD community.<br>
 * I hope this plugin will be useful for everyone who needs it.
 * 
 * @author Arzio
 *
 */
public class RaidableBases extends JavaPlugin {

	private static RaidableBases instance;

	private BaseDamageProvider damageProvider;

	@Override
	public void onEnable() {
		instance = this;
		BaseProvider baseProvider = ArzioLib.getInstance().getBaseProvider();

		damageProvider = new BaseDamageProviderImpl(this, baseProvider);

		this.saveDefaultConfig();
		this.reloadConfig();

		this.getServer().getPluginManager().registerEvents(new BaseDamageListener(baseProvider, damageProvider), this);
		this.getServer().getPluginManager().registerEvents(new C4FixListener(), this);
		this.getServer().getPluginManager().registerEvents(new BaseInteractListener(), this);

		this.getLogger().info(this.getName() + " loaded!");
	}

	@Override
	public void reloadConfig() {
		super.reloadConfig();
		RaidableConfig.INSTANCE.loadData(this.getConfig());
	}

	@Override
	public void onDisable() {
		damageProvider.saveDirty();
		this.getLogger().info(this.getName() + " disabled!");
		instance = null;
	}

	public BaseDamageProvider getBaseDamageProvider() {
		return this.damageProvider;
	}

	public static RaidableBases getInstance() {
		return instance;
	}
}
