package com.oman.trackerdeaths;

import io.sporkpgm.player.SporkPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import tc.oc.tracker.trackers.base.gravity.Attack;

public class Util {

	public static String getEntityString(Entity entity) {
		String name = "";
		switch(entity.getType()) {
			case BAT:
				name = "a bat";
				break;
			case BLAZE:
				name = "a blaze";
				break;
			case CAVE_SPIDER:
				name = "a cave spider";
				break;
			case CHICKEN:
				name = "a chicken";
				break;
			case COW:
				name = "a cow";
				break;
			case CREEPER:
				name = "a creeper";
				break;
			case ENDER_DRAGON:
				name = "an Ender Dragon";
				break;
			case ENDERMAN:
				name = "an enderman";
				break;
			case FALLING_BLOCK:
				name = "a falling block";
				break;
			case FIREBALL:
				name = "a fireball";
				break;
			case GHAST:
				name = "a ghast";
				break;
			case GIANT:
				name = "a giant";
				break;
			case IRON_GOLEM:
				name = "an iron golem";
				break;
			case MAGMA_CUBE:
				name = "a magma cube";
				break;
			case MUSHROOM_COW:
				name = "a mooshroom";
				break;
			case OCELOT:
				name = "an ocelot";
				break;
			case PIG:
				name = "a pig";
				break;
			case PIG_ZOMBIE:
				name = "a pig zombie";
				break;
			case PLAYER:
				name = SporkPlayer.getPlayer(((Player) entity)) != null ? SporkPlayer.getPlayer(((Player) entity))
						.getTeam().getColor() + SporkPlayer.getPlayer(((Player) entity)).getPlayer().getName() : (
						(Player) entity).getDisplayName();
				break;
			case SHEEP:
				name = "a sheep";
				break;
			case SILVERFISH:
				name = "a silverfish";
				break;
			case SKELETON:
				name = "a skeleton";
				break;
			case SLIME:
				name = "a slime";
				break;
			case SMALL_FIREBALL:
				name = "a fireball";
				break;
			case SNOWMAN:
				name = "a snowman";
				break;
			case SPIDER:
				name = "a spider";
				break;
			case SQUID:
				name = "a squid";
				break;
			case VILLAGER:
				name = "a villager";
				break;
			case WITCH:
				name = "a witch";
				break;
			case WOLF:
				name = "a wolf";
				break;
			case ZOMBIE:
				name = "a zombie";
				break;
			default:
				name = "an unknown entity";
				break;
		}

		return name;
	}

	public static String getProjectileName(Entity entity) {
		String name = "";
		switch(entity.getType()) {
			case PRIMED_TNT:
				name = "TNT";
				break;
			case ARROW:
				name = "arrow";
				break;
			case EGG:
				name = "egg";
				break;
			case ENDER_PEARL:
				name = "ender pearl";
				break;
			case FIREBALL:
				name = "fireball";
				break;
			case SNOWBALL:
				name = "snowball";
				break;
			case WITHER_SKULL:
				name = "wither skull";
				break;
			default:
				name = "";
				break;
		}

		return name;
	}

	public static String getName(ItemStack item) {
		Material material = item.getType();
		String[] wordsSplit = material.name().split("_");
		String sReturn = "";
		int count = 1;
		for(String w : wordsSplit) {
			String word = w.toUpperCase().replace(w.substring(1), w.substring(1).toLowerCase());
			sReturn += word;
			if(count < wordsSplit.length) {
				sReturn += " ";
			}
		}
		return (item.getEnchantments().size() > 0 ? "enchanted " : "") + sReturn;
	}

	public static String getFrom(Attack.From from) {
		String siteText;
		switch(from) {
			case LADDER:
				siteText = " off a ladder";
				break;

			case WATER:
				siteText = " out of the water";
				break;

			default:
				siteText = "";
				break;
		}

		return siteText;
	}

	public static String getCause(Attack.Cause cause) {
		String attackText;
		switch(cause) {
			case HIT:
				attackText = "was knocked";
				break;

			case SHOOT:
				attackText = "was shot";
				break;

			case SPLEEF:
				attackText = "was spleefed";
				break;

			default:
				attackText = "";
				break;
		}

		return attackText;
	}

	public static String getTo(Attack.From from, EntityDamageEvent.DamageCause damageCause) {
		String damageText;
		if(from == Attack.From.FLOOR) {
			switch(damageCause) {
				case VOID:
					damageText = " out of the world";
					break;

				case FALL:
					damageText = " off a high place";
					break;

				case LAVA:
				case FIRE_TICK:
					damageText = " into lava";
					break;

				case SUICIDE:
					damageText = " to their death (suicide/battle log)";
					break;

				default:
					damageText = " to their death";
			}
		} else {
			switch(damageCause) {
				case VOID:
					damageText = " and into the void";
					break;

				case FALL:
					damageText = "";
					break;

				case LAVA:
				case FIRE_TICK:
					damageText = " and into lava";
					break;

				case SUICIDE:
					damageText = " to their death";
					break;

				default:
					damageText = " to their death";
			}
		}

		return damageText;
	}

	public static String getAction(EntityDamageEvent.DamageCause damageCause) {
		String attack = "";
		switch(damageCause) {
			case ENTITY_ATTACK:
				attack = "was slain";
				break;
			case PROJECTILE:
				attack = "was shot";
				break;
			case BLOCK_EXPLOSION:
				attack = "was blown up";
				break;
			case CONTACT:
				attack = "was pricked to death";
				break;
			case DROWNING:
				attack = "drowned";
				break;
			case FALL:
				attack = "hit the ground too hard";
				break;
			case FIRE:
				attack = "burned to death";
				break;
			case FIRE_TICK:
			case LAVA:
				attack = "died in lava";
				break;
			case LIGHTNING:
				attack = "was struck by lightning";
				break;
			case POISON:
				attack = "was poisoned";
				break;
			case STARVATION:
				attack = "starved to death";
				break;
			case SUFFOCATION:
				attack = "suffocated in a wall";
				break;
			case VOID:
				attack = "fell out of the world";
				break;
			default:
				attack = "died";
				break;
		}
		return attack;
	}

}
