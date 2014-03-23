package io.sporkpgm.filter;

public enum Modifier {

	NOT,
	ONE,
	ALL,
	ANY;

	public static Modifier getModifier(String name) {
		for(Modifier modifier : values()) {
			if(modifier.name().equalsIgnoreCase(name)) {
				return modifier;
			}
		}

		return null;
	}

}
