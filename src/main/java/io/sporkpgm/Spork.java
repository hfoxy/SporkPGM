package io.sporkpgm;

import org.bukkit.plugin.java.JavaPlugin;

public class Spork extends JavaPlugin {

	protected static Spork spork;

	@Override
	public void onEnable() {
		spork = this;
	}

	public static Spork get() {
		return spork;
	}

}
