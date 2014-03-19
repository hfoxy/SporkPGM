package io.sporkpgm;

import io.sporkpgm.map.MapBuilder;
import io.sporkpgm.map.MapLoader;
import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.ModuleBuilder;
import io.sporkpgm.rotation.Rotation;
import io.sporkpgm.rotation.exceptions.RotationLoadException;
import io.sporkpgm.util.Config;
import io.sporkpgm.util.Config.Map;
import io.sporkpgm.util.Log;
import org.bukkit.plugin.java.JavaPlugin;
import org.dom4j.Document;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static io.sporkpgm.Spork.StartupType.*;

public class Spork extends JavaPlugin {

	protected static Spork spork;

	protected List<Class<? extends ModuleBuilder>> builders;
	protected List<MapBuilder> maps;
	protected File repository;

	protected Rotation rotation;

	@Override
	public void onEnable() {
		spork = this;
		saveDefaultConfig();

		Config.init();
		Log.setDebugging(Config.General.DEBUG);

		if(getConfig().getString("settings.maps.repository") == null) {
			getConfig().set("settings.maps.repository", "maps/");
		}

		repository = Map.DIRECTORY;
		maps = new ArrayList<>();

		StartupType type = Map.STARTUP;

		List<MapBuilder> all = MapLoader.build(repository);
		List<MapBuilder> load = new ArrayList<>();
		if(type == ALL) {
			load.addAll(all);
		} else if(type == SPECIFIED) {
			load.clear();
			List<String> specified = getConfig().getStringList("settings.maps.load");
			if(specified != null) {
				for(MapBuilder map : all) {
					if(contains(specified, "map name")) {
						load.add(map);
					}
				}
			}
		}

		if(load.isEmpty() && type != SPECIFIED) {
			Log.warning("No maps were available from the specified list - loading all maps");
			load = new ArrayList<>();
			load.addAll(all);
		}

		if(load.isEmpty()) {
			Log.severe("Unable to find any maps inside the repository! Disabling plugin...");
			setEnabled(false);
			return;
		}

		boolean failed = false;
		String reason = "";
		try {
			this.rotation = Rotation.provide();
		} catch(RotationLoadException rle) {
			Log.severe(rle.getClass().getSimpleName() + ": " + rle.getMessage());
			Log.severe("Could not create a suitable Rotation! Disabling plugin...");
			setEnabled(false);
			return;
		} catch(IOException ioe) {
			Log.severe(ioe.getClass().getSimpleName() + ": " + ioe.getMessage());
			failed = true;
			reason = "Unable to save new Rotation";
		}

		if(failed) {
			Log.severe(reason + "! Disabling plugin...");
			setEnabled(false);
			return;
		}
	}

	private boolean contains(List<String> strings, String search) {
		for(String string : strings) {
			if(string.equalsIgnoreCase(search)) {
				return true;
			}
		}

		return false;
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
