package io.sporkpgm.map;

import io.sporkpgm.Spork;
import io.sporkpgm.module.modules.info.InfoModule;
import org.dom4j.Document;

import java.io.File;
import java.util.List;

public class MapBuilder {

	protected Document document;
	protected File folder;

	InfoModule info;

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

	public String getName() {
		return info.getName();
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
