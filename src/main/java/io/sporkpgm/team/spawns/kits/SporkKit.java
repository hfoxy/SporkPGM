package io.sporkpgm.team.spawns.kits;

import com.google.common.collect.Lists;
import io.sporkpgm.player.SporkPlayer;
import io.sporkpgm.team.SporkTeam;
import io.sporkpgm.util.Log;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class SporkKit {

	private String name;
	private List<KitItem> kitItems;
	private List<PotionEffect> potions;
	private List<KitArmor> kitArmor;
	private List<SporkKit> parents;

	public SporkKit(String name, List<KitItem> kitItems) {
		this(name, kitItems, null, null, null);
	}

	public SporkKit(String name, List<KitItem> kitItems, List<PotionEffect> potions) {
		this(name, kitItems, potions, null, null);
	}

	public SporkKit(String name, List<KitItem> kitItems, List<PotionEffect> potions, List<KitArmor> armor) {
		this(name, kitItems, potions, null, armor);
	}

	public SporkKit(String name, List<KitItem> kitItems, List<PotionEffect> potions, List<SporkKit> parents, List<KitArmor> armor) {
		this.name = name;
		this.kitItems = kitItems;
		this.potions = potions;
		this.parents = parents;
		this.kitArmor = armor;
		this.parents = Lists.newArrayList();
	}

	public void apply(SporkPlayer player) {
		apply(player.getPlayer());
	}

	public void apply(Player player) {
		PlayerInventory inv = player.getInventory();
		Log.info("Actually applying kit..." + name);

		if(kitItems != null) {
			Log.info(kitItems.size() + " items in '" + name + "'");
		}

		for(KitItem item : kitItems) {
			inv.setItem(item.getSlot(), item.getItem());
		}

		if(potions != null) {
			Log.info(potions.size() + " potions in '" + name + "'");
			// player.addPotionEffects(potions);
		}

		if(kitArmor != null) {
			Log.info(kitArmor.size() + " armor slots used in '" + name + "'");
			for(KitArmor armor : kitArmor) {
				switch(armor.getSlot()) {
					case HELMET:
						inv.setHelmet(armor.getItem());
					case CHESTPLATE:
						inv.setChestplate(armor.getItem());
					case LEGGINGS:
						inv.setLeggings(armor.getItem());
					case BOOTS:
						inv.setBoots(armor.getItem());
				}
			}
		}
		if(parents != null) {
			Log.info(parents.size() + " parents applying in " + name);
			for(SporkKit kit : parents) {
				kit.apply(player);
			}
		}
	}

	public void apply(SporkTeam team) {
		for(SporkPlayer p : team.getPlayers()) {
			apply(p);
		}
	}

	public List<PotionEffect> getPotions() {
		return potions;
	}

	public List<KitItem> getKitItems() {
		return kitItems;
	}

	public String getName() {
		return name;
	}

	public List<SporkKit> getParents() {
		return parents;
	}

	public boolean hasParents() {
		return parents.isEmpty();
	}

	public void setParents(List<SporkKit> parents) {
		this.parents = parents;
	}

	public void addParent(SporkKit parent) {
		parents.add(parent);
	}

	public void removeParent(SporkKit parent) {
		parents.remove(parent);
	}

	public void removeParentById(int id) {
		parents.remove(id);
	}

}
