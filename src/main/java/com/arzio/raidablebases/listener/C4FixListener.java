package com.arzio.raidablebases.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.arzio.arziolib.api.event.packet.CDBulletHitEvent;
import com.arzio.arziolib.api.event.packet.CDBulletHitEvent.HitType;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.api.util.EntityUtil;
import com.arzio.raidablebases.util.BaseDamageCause;

/**
 * Listener which fixes some simple issues in the C4 entity.
 * @author Arzio
 */
public class C4FixListener implements Listener {

	private static final int DETONATOR_ID = 9711;

	@EventHandler
	public void onBulletHit(CDBulletHitEvent event) {
		if (event.getHitType() != HitType.ENTITY) {
			return;
		}

		Entity entityHit = event.getEntityHit();
		if (CDEntityType.C4.isTypeOf(entityHit)) {
			if (entityHit.isValid()) {
				entityHit.remove();
				BaseDamageCause.C4.createExplosion(entityHit);
			}
		}
	}

	@EventHandler
	public void onDropDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() == EntityType.DROPPED_ITEM) {
			event.setCancelled(true);
		}
		if (CDEntityType.C4.isTypeOf(event.getEntity())) {
			Entity entity = event.getEntity();
			event.setCancelled(true);

			if (!entity.isDead() && entity.isValid()) {
				entity.remove();
				BaseDamageCause.C4.createExplosion(entity);
			}
		}
	}

	@EventHandler
	public void onC4Damage(EntityDamageByBlockEvent event) {
		Entity entity = event.getEntity();

		if (CDEntityType.C4.isTypeOf(entity)) {
			event.setCancelled(true);

			if (!entity.isDead() && entity.isValid()) {
				entity.remove();
				BaseDamageCause.C4.createExplosion(entity);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getPlayer().getItemInHand().getTypeId() == DETONATOR_ID) {
				event.setCancelled(true);

				event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.CLICK, 2F, 2F);

				for (Entity entity : event.getPlayer().getWorld().getEntities()) {
					if (CDEntityType.C4.isTypeOf(entity)
							&& event.getPlayer().getName().equalsIgnoreCase(EntityUtil.getC4OwnerName(entity))) {
						if (entity.getLocation().distance(event.getPlayer().getLocation()) <= 50D) {
							entity.remove();
							BaseDamageCause.C4.createExplosion(entity);
						}
					}
				}
			}
		}
	}
}
