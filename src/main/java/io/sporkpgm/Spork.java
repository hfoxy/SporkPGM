package io.sporkpgm;

import io.sporkpgm.map.MapBuilder;
import io.sporkpgm.map.MapLoader;
import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.ModuleBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.dom4j.Document;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class Spork extends JavaPlugin {

	protected static Spork spork;

	protected List<Class<? extends ModuleBuilder>> builders;
	protected List<MapBuilder> maps;
	protected File repository;

	@Override
	public void onEnable() {
		spork = this;
		saveDefaultConfig();

		if(getConfig().getString("settings.maps.respository") == null) {
			getConfig().set("settings.maps.respository", "maps/");
		}

		repository = new File(getConfig().getString("settings.maps.respository"));
		maps = new ArrayList<>();

		String startup = getConfig().getString("settings.maps.startup");
		StartupType type = StartupType.getType(startup);

		List<MapBuilder> load = MapLoader.build(repository);
		if(type == StartupType.SPECIFIED) {
			List<String> specified = getConfig().getStringList("settings.maps.load");
			if(specified != null) {

			}
		}
	}

	public List<Module> getModules(Document document) {
		return getModules(document, builders);
	}

	public List<Module> getModules(Document document, List<Class<? extends ModuleBuilder>> builders) {
		List<Module> modules = new ArrayList<>();

		for(Class<? extends ModuleBuilder> clazz : builders) {
			try {
				Constructor constructor = clazz.getConstructor(SporkMap.class);
				constructor.setAccessible(true);
				ModuleBuilder builder = (ModuleBuilder) constructor.newInstance(document);
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

	public enum StartupType {

		ALL(new String[]{"all"}),
		SPECIFIED(new String[]{"specified", "specify", "listed"});

		String[] names;

		StartupType(String[] names) {
			this.names = names;
		}

		public String[] getNames() {
			return names;
		}

		public boolean matches(String name) {
			for(String value : names) {
				if(value.equalsIgnoreCase(name)) {
					return true;
				}
			}

			return false;
		}

		public static StartupType getType(String name) {
			if(name != null) {
				for(StartupType type : values()) {
					if(type.matches(name)) {
						return type;
					}
				}
			}

			return ALL;
		}

	}

}
