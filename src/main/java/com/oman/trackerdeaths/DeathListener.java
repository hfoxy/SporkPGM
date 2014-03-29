package com.oman.trackerdeaths;

import io.sporkpgm.Spork;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import tc.oc.tracker.TrackerManager;
import tc.oc.tracker.Trackers;
import tc.oc.tracker.trackers.AnvilTracker;
import tc.oc.tracker.trackers.DispenserTracker;
import tc.oc.tracker.trackers.ExplosiveTracker;
import tc.oc.tracker.trackers.ProjectileDistanceTracker;
import tc.oc.tracker.trackers.base.gravity.Attack;
import tc.oc.tracker.trackers.base.gravity.SimpleGravityKillTracker;

public class DeathListener implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player victim = event.getEntity();
		Death death = new Death(victim);

		TrackerManager tm = Trackers.getManager();
		SimpleGravityKillTracker gravity = tm.getTracker(SimpleGravityKillTracker.class);
		AnvilTracker anv = tm.getTracker(AnvilTracker.class);
		ExplosiveTracker explosive = tm.getTracker(ExplosiveTracker.class);
		DispenserTracker dispenser = tm.getTracker(DispenserTracker.class);
		ProjectileDistanceTracker projectile = tm.getTracker(ProjectileDistanceTracker.class);

		makeDefaults(death, event);
		processGravity(gravity, death, event);
		processAnvil(anv, death);
		processExplosives(explosive, death);
		processItem(death);
		processDispensers(dispenser, projectile, death);
		processProjectiles(projectile, death);

		event.setDeathMessage(death.getDeathMessage());

		DeathEvent deathEvent = new DeathEvent(death);
		Spork.callEvent(deathEvent);
	}

	/**
	 * Put the default values into the death
	 * prior to any use of tracker
	 *
	 * @param death The fresh instance of Death
	 * @param event The PlayerDeathEvent in process
	 */
	public void makeDefaults(Death death, PlayerDeathEvent event) {

		death.setEvent(death.getVictim().getLastDamageCause());

		if(death.getEvent() instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) death.getEvent();
			Entity killerEntity = entityEvent.getDamager();

			if(killerEntity instanceof LivingEntity) {
				LivingEntity killer = (LivingEntity) killerEntity;
				if(killer != null) {
					death.setKiller(killer);
				}
			}
		}

		String attack = Util.getAction(death.getCause());
		death.setAction(attack);
	}

	/**
	 * Process the death for any
	 * Gravity kill data that
	 * may exist
	 *
	 * @param gravity the SimpleGravityTracker from the Tracker plugin
	 * @param death   The death being processed
	 * @param event   The death event being processed
	 */
	public void processGravity(SimpleGravityKillTracker gravity, Death death, PlayerDeathEvent event) {
		Player p = death.getVictim();

		if(gravity.attacks.containsKey(p)) {
			Attack attack = gravity.attacks.remove(p);
			if(gravity.wasAttackFatal(attack, death.getCause(), 200)) {
				DamageCause damageCause = death.getCause();

				death.setAction(Util.getCause(attack.cause));
				death.setFrom(Util.getFrom(attack.from));
				death.setTo(Util.getTo(attack.from, damageCause));
				death.setKiller(attack.attacker);
			}
		}
	}

	/**
	 * Process the death for anvil
	 * related kills
	 *
	 * @param anv   the AnvilTracker from the Tracker plugin
	 * @param death the Death in processing
	 */
	private void processAnvil(AnvilTracker anv, Death death) {
		if(death.getCause() == DamageCause.FALLING_BLOCK) {
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) death.getEvent();

			if(event.getDamager() instanceof FallingBlock) {
				if(((FallingBlock) event.getDamager()).getMaterial() == Material.ANVIL) {
					if(anv.getOwner((FallingBlock) event.getDamager()) != null) {
						if(anv.getOwner((FallingBlock) event.getDamager()) instanceof Player) {

							Player owner = (Player) anv.getOwner((FallingBlock) event.getDamager());

							if(death.getAction().equalsIgnoreCase("died")) {
								death.setAction("was crushed");
								death.setWeapon("falling anvil");
								death.setKiller(owner);
							} else {
								death.setTo(" into " + owner.getDisplayName() + "'s falling anvil");
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Process the deaths for explosives
	 *
	 * @param explosive the ExplosiveTracker from the Tracker plugin
	 * @param death     the Death being processed
	 */
	public void processExplosives(ExplosiveTracker explosive, Death death) {
		if(death.getCause() == DamageCause.BLOCK_EXPLOSION) {
			if(((EntityDamageByEntityEvent) death.getEvent()).getDamager() instanceof TNTPrimed) {
				TNTPrimed tnt = (TNTPrimed) ((EntityDamageByEntityEvent) death.getEvent()).getDamager();
				if(explosive.getOwner(tnt) != null) {
					Player killer = explosive.getOwner(tnt);
					death.setAction("was exploded");
					death.setWeapon("TNT");
					death.setKiller(killer);
				}
			}
		}
	}

	/**
	 * Update the item of the killer
	 * if applicable
	 *
	 * @param death The death being processed
	 */
	public void processItem(Death death) {
		if(death.getCause() == DamageCause.ENTITY_ATTACK) {
			if(death.getCredited() != null) {
				String weapon = "";
				ItemStack item = death.getCredited().getItemInHand();
				if(item.getType() == Material.AIR) {
					weapon = "fists";
				} else {
					if(Util.getName(item) != null) {
						weapon = Util.getName(item);
					}
				}

				death.setWeapon(weapon);
			}
		}
	}

	/**
	 * Process the dispenser tracker
	 *
	 * @param dispenser the dispenser tracker
	 * @param death     the death being processed
	 */
	public void processDispensers(DispenserTracker dispenser, ProjectileDistanceTracker projectile, Death death) {
		if(death.getEvent() instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) death.getEvent();
			if(dispenser.getOwner(event.getDamager()) != null) {
				if(player(dispenser.getOwner(event.getDamager())) != null) {
					Player p = player(dispenser.getOwner(event.getDamager()));
					death.setKiller(p);
					death.setWeapon("dispensed " + Util.getProjectileName(event.getDamager()));
				}
			}
		}
	}

	/**
	 * Process the projectile kills
	 * Adds misc information (distance)
	 *
	 * @param tracker the projectile tracker
	 * @param death   the death being processed
	 */
	public void processProjectiles(ProjectileDistanceTracker tracker, Death death) {
		if(death.getCause() == DamageCause.PROJECTILE) {
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) death.getEvent();
			Projectile projectile = (Projectile) event.getDamager();
			Entity shooter = (LivingEntity) projectile.getShooter();
			double distance = projectile.getLocation().distance(tracker.getLaunchLocation(projectile));

			if(shooter != null) {
				death.setKiller(shooter);
			}
			if(round(distance) == 0) {
				return;
			} else if(round(distance) == 1) {
				death.setMisc("(" + round(distance) + " block)");
			} else {
				death.setMisc("(" + round(distance) + " blocks)");
			}
		}
	}

	/**
	 * Get a player object from
	 * an OfflinePlayer object
	 *
	 * @param offlinePlayer the offline player
	 */
	public Player player(OfflinePlayer offlinePlayer) {
		if(Bukkit.getPlayer(offlinePlayer.getName()) != null) {
			return Bukkit.getPlayer(offlinePlayer.getName());
		}

		return null;
	}

	/**
	 * Get the integer from a double
	 *
	 * @param number the double being rounded
	 */
	public int round(double number) {
		return (int) Math.round(number);
	}
}
