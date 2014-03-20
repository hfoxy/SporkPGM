package io.sporkpgm.team.spawns.kits;

public enum KitArmorSlot {

	HELMET("helmet"),
	CHESTPLATE("chestplate"),
	LEGGINGS("leggings"),
	BOOTS("boots");

	private String name;

	KitArmorSlot(String name) {
		this.name = name;
	}

	String getName() {
		return name;
	}

	public static KitArmorSlot getSlot(String string) {
		KitArmorSlot match = null;
		for(KitArmorSlot slot : KitArmorSlot.values()) {
			if(slot.getName().equalsIgnoreCase(string)) {
				match = slot;
			}
		}
		return match;
	}

}
