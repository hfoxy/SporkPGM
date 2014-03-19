package io.sporkpgm;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.builder.BuilderAbout;
import io.sporkpgm.util.Config;
import io.sporkpgm.util.Log;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;
import org.dom4j.Document;

public class Spork extends JavaPlugin {

	protected static Spork spork;

	protected List<Class<? extends Builder>> builders;

	@Override
	public void onEnable() {
		spork = this;
		
		Config.init();
		Log.setDebugging(Config.General.DEBUG);
	}

	public List<Module> getModules(Document document) {
		return getModules(document, builders);
	}

	public List<Module> getModules(Document document, List<Class<? extends Builder>> builders) {
		List<Module> modules = new ArrayList<>();

		for(Class<? extends Builder> clazz : builders) {
			try {
				Constructor<? extends Builder> constructor = clazz.getConstructor(Document.class);
				constructor.setAccessible(true);
				Builder builder = (Builder) constructor.newInstance(document);
				BuilderAbout about = builder.getInfo();

				if(!about.isDocumentable()) {
					constructor = clazz.getConstructor(SporkMap.class);
					builder = (Builder) constructor.newInstance(document);
				}
			} catch(Exception e) {
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

		for(Class<? extends Builder> clazz : builders) {
			try {
				if(documentable && Builder.isDocumentable(clazz)) {
					classes.add(clazz);
				} else {
					classes.add(clazz);
				}
			} catch(Exception e) {
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
