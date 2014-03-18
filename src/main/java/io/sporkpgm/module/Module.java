package io.sporkpgm.module;

import org.bukkit.event.Listener;

public abstract class Module implements Listener {

	public ModuleAbout getInfo() {
		return new ModuleAbout(this.getClass());
	}

}
