package com.arzio.raidablebases;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.BaseProvider;
import com.arzio.arziolib.api.UpdateChecker;
import com.arzio.arziolib.api.UpdateChecker.CheckMethod;
import com.arzio.arziolib.api.UpdateChecker.UpdateState;
import com.arzio.arziolib.api.impl.GitHubUpdateChecker;
import com.arzio.arziolib.api.util.CountdownTimer;
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
public class RaidableBases extends JavaPlugin implements Listener {

	private static RaidableBases instance;

	private BaseDamageProvider basesDamage;
	private UpdateChecker updateChecker;

	@Override
	public void onEnable() {
		instance = this;
		BaseProvider baseProvider = ArzioLib.getInstance().getBaseProvider();

		basesDamage = new BaseDamageProviderImpl(this, baseProvider);

		this.saveDefaultConfig();
		this.reloadConfig();

		this.getServer().getPluginManager().registerEvents(new BaseDamageListener(baseProvider, basesDamage), this);
		this.getServer().getPluginManager().registerEvents(new C4FixListener(), this);
		this.getServer().getPluginManager().registerEvents(new BaseInteractListener(), this);

		this.getLogger().info(this.getName() + " loaded!");

		this.updateChecker = new GitHubUpdateChecker(this, "https://api.github.com/repos/Arzio/RaidableBases/releases",
		"https://github.com/Arzio/RaidableBases/releases");
		this.updateChecker.checkUpdates(CheckMethod.SYNC);

		// Automatically checks for updates in a async way every 10 minutes
		Bukkit.getScheduler().runTaskTimer(this, new Runnable() {

			@Override
			public void run() {
				getUpdateChecker().checkUpdates(CheckMethod.ASYNC);
			}

		}, 600L * 20L, 600L * 20L); // Checks for update every 10 minutes
	}

	@Override
	public void reloadConfig() {
		super.reloadConfig();
		RaidableConfig.INSTANCE.loadData(this.getConfig());
	}

	@Override
	public void onDisable() {
		basesDamage.saveDirty();
		this.getLogger().info(this.getName() + " disabled!");
		instance = null;
	}


	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onAdminJoin(PlayerJoinEvent event) {
		// As some servers does not uses OP, we check for any common permission
		if (event.getPlayer().isOp() || event.getPlayer().hasPermission("essentials.gamemode")) {
			final Player player = event.getPlayer();

			if (this.updateChecker.getState() == UpdateState.NEEDS_UPDATE) {
				CountdownTimer timer = new CountdownTimer(this, 5L,
						new CountdownTimer.TimeCallback() {

							@Override
							public void onStart(long durationMillis) {
							}

							@Override
							public void onEnd() {
								if (!player.isValid()) {
									return;
								}

								String latestVersion = getUpdateChecker().getLatestVersionTag();

								player.sendMessage(" ");
								player.sendMessage("§a["+getName()+"] §eA atualização " + latestVersion + " está pronta para ser baixada!");
								player.sendMessage("§a["+getName()+"] §e§oThe " + latestVersion + " update is ready to be downloaded!");
								player.sendMessage("§a["+getName()+"] §fDownload it here NOW: §b"+getUpdateChecker().getPluginPageURL());
								player.sendMessage(" ");

								player.playSound(player.getLocation(), Sound.NOTE_PLING, 2F, 2F);
							}
						});

				timer.start();
			}
		}
	}

	public UpdateChecker getUpdateChecker(){
		return this.updateChecker;
	}

	public BaseDamageProvider getBaseDamageProvider() {
		return this.basesDamage;
	}

	public static RaidableBases getInstance() {
		return instance;
	}
}
