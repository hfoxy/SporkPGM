package io.sporkpgm;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.ModuleBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class Spork extends JavaPlugin {

	protected static Spork spork;

	protected List<Class<? extends ModuleBuilder>> builders;

	@Override
	public void onEnable() {
		spork = this;
	}

	public List<Module> getModules(SporkMap map, List<Class<? extends ModuleBuilder>> builders) {
		List<Module> modules = new ArrayList<>();

		for(Class<? extends ModuleBuilder> clazz : builders) {
			try {
				Constructor constructor = clazz.getConstructor(SporkMap.class);
				constructor.setAccessible(true);
				ModuleBuilder builder = (ModuleBuilder) constructor.newInstance(map);
				modules.addAll(builder.build());
			} catch(Exception e) {
				getLogger().warning("Error when loading '" + clazz.getSimpleName() + "' due to " + e.getClass().getSimpleName());
				continue;
			}
		}

		return modules;
	}

	public List<Class<? extends ModuleBuilder>> getBuilders() {
		return builders;
	}

	protected void builders() {
		builders = new ArrayList<>();
	}

	public static Spork get() {
		return spork;
	}

}
