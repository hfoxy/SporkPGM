package io.sporkpgm;

import io.sporkpgm.map.MapLoader;
import io.sporkpgm.map.MapManager;
import io.sporkpgm.map.SporkMap;
import io.sporkpgm.match.MatchManager;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.builder.BuilderAbout;
import io.sporkpgm.rotation.Rotation;
import io.sporkpgm.util.Config;
import io.sporkpgm.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;
import org.dom4j.Document;

public class Spork extends JavaPlugin {

	private static Spork spork;

	private MapManager mapManager;
	private MatchManager matchManager;

	protected List<Class<? extends Builder>> builders;

	@Override
	public void onEnable() {
		spork = this;

		Config.init();
		Log.setDebugging(Config.General.DEBUG);

		Log.info("Loading maps...");
		MapLoader loader = new MapLoader();
		File mapDir = new File(Config.Map.DIRECTORY);
		Collection<SporkMap> maps = loader.loadMaps(mapDir);
		this.mapManager = new MapManager(maps);

		Log.info("Loading rotation...");
		File rotationFile = new File(this.getDataFolder(), Config.Rotation.ROTATION);
		Rotation rotation = new Rotation(mapManager, rotationFile);
		this.matchManager = new MatchManager(rotation);
	}

	public List<Module> getModules(Document document) {
		return getModules(document, builders);
	}

	public List<Module> getModules(Document document, List<Class<? extends Builder>> builders) {
		List<Module> modules = new ArrayList<>();

		for (Class<? extends Builder> clazz : builders) {
			try {
				Constructor<? extends Builder> constructor = clazz.getConstructor(Document.class);
				constructor.setAccessible(true);
				Builder builder = (Builder) constructor.newInstance(document);
				BuilderAbout about = builder.getInfo();

				if (!about.isDocumentable()) {
					constructor = clazz.getConstructor(SporkMap.class);
					builder = (Builder) constructor.newInstance(document);
				}
			} catch (Exception e) {
				Log.warning("Error when loading '" + clazz.getSimpleName() + "' due to " + e.getClass().getSimpleName());
				continue;
			}
		}

		return modules;
	}

	public List<Class<? extends Builder>> getBuilders() {
		return builders;
	}

	public List<Class<? extends Builder>> getBuilders(List<Class<? extends Builder>> builders, boolean documentable) {
		List<Class<? extends Builder>> classes = new ArrayList<>();

		for (Class<? extends Builder> clazz : builders) {
			try {
				if (documentable && Builder.isDocumentable(clazz)) {
					classes.add(clazz);
				} else {
					classes.add(clazz);
				}
			} catch (Exception e) {
				Log.warning("Error when loading '" + clazz.getSimpleName() + "' due to " + e.getClass().getSimpleName());
				continue;
			}
		}

		return classes;
	}

	protected void builders() {
		builders = new ArrayList<>();
	}

	public static Spork get() {
		return spork;
	}

}
