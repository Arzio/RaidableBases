package com.arzio.raidablebases.data.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.arzio.arziolib.api.BaseProvider;
import com.arzio.arziolib.api.event.CDBaseDestroyEvent;
import com.arzio.arziolib.api.wrapper.Base;
import com.arzio.raidablebases.RaidableBases;
import com.arzio.raidablebases.data.BaseDamage;
import com.arzio.raidablebases.data.BaseDamageProvider;
import com.arzio.raidablebases.data.loader.BaseIOLoader;
import com.arzio.raidablebases.data.loader.BaseYMLLoader;
import com.arzio.raidablebases.util.BaseDamageUtil;

public class BaseDamageProviderImpl implements BaseDamageProvider {

	private static final long BASE_CRACK_UPDATE_SECONDS = 5L * 20L;

	private final File baseDataFolder;
	private final BaseProvider baseProvider;
	private BaseIOLoader saveHandler;
	private final Map<Base, BaseDamage> damageCache = new HashMap<Base, BaseDamage>();

	public BaseDamageProviderImpl(Plugin plugin, BaseProvider baseProvider) {
		this.saveHandler = new BaseYMLLoader();
		this.baseProvider = baseProvider;

		// Creates the basedata folder if it does not exists
		this.baseDataFolder = new File(RaidableBases.getInstance().getDataFolder(), "basedata");
		if (!baseDataFolder.exists()) {
			baseDataFolder.mkdirs();
		}

		// Registers this provider as a listener
		Bukkit.getPluginManager().registerEvents(this, plugin);

		// Updates the visual damamge to the near players
		Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {

			@Override
			public void run() {
				for (World world : Bukkit.getWorlds()) {
					for (Base base : BaseDamageProviderImpl.this.baseProvider.getLoadedBasesFrom(world)) {
						BaseDamage baseDamage = getBaseDamage(base);
						BaseDamageUtil.broadcastBaseDamage(baseDamage);
					}
				}
			}

		}, BASE_CRACK_UPDATE_SECONDS, BASE_CRACK_UPDATE_SECONDS);

		// Saves the dirty bases every second
		Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {

			@Override
			public void run() {
				saveDirty();
			}

		}, 20L, 20L);
	}

	@Override
	public void saveDirty() {
		for (BaseDamage damage : damageCache.values()) {
			if (damage.isDirty()) {
				try {
					this.getLoader().save(this.getLoader().getBaseFile(this.baseDataFolder, damage.getBase()), damage);
					damage.setDirty(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public BaseDamage getBaseDamage(Base base) {
		BaseDamage data = damageCache.get(base);

		if (data == null) {

			try {
				data = this.getLoader().load(this.getLoader().getBaseFile(this.baseDataFolder, base), base);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (data == null) {
				data = new BaseDamageImpl(base);
			}

			damageCache.put(base, data);
		}

		return data;
	}

	@EventHandler
	public void onBaseDestroy(CDBaseDestroyEvent event) {
		BaseDamage data = this.getBaseDamage(event.getBase());

		data.repairAll();
		data.hideHologram();
		damageCache.remove(data.getBase());
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		for (Base base : baseProvider.getLoadedBasesFrom(player.getWorld())) {
			BaseDamageUtil.showBaseDamage(this.getBaseDamage(base), player);
		}
	}

	@Override
	public BaseIOLoader getLoader() {
		return this.saveHandler;
	}

	@Override
	public void setLoader(BaseIOLoader handler) {
		this.saveHandler = handler;
	}

}
