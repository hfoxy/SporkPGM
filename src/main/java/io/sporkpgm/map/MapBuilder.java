package io.sporkpgm.map;

import io.sporkpgm.Spork;
import io.sporkpgm.module.modules.info.InfoModule;
import org.dom4j.Document;

import java.io.File;
import java.util.List;

public class MapBuilder {

	private Document document;
	private File folder;

	private InfoModule info;

	public MapBuilder(Document document, File folder) {
		this.document = document;
		this.folder = folder;
	}

	public Document getDocument() {
		return document;
	}

	public File getFolder() {
		return folder;
	}

	public InfoModule getInfo() {
		return info;
	}

	public String getName() {
		return info.getName();
	}

	public SporkMap getMap() {
		return null;
	}

	public static MapBuilder getLoader(String string) {
		List<MapBuilder> loaders = Spork.getMaps();

		for(MapBuilder loader : loaders)
			if(loader.getName().equalsIgnoreCase(string))
				return loader;

		for(MapBuilder loader : loaders)
			if(loader.getName().toLowerCase().contains(string.toLowerCase()))
				return loader;

		return null;
	}

}
