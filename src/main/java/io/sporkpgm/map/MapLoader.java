package io.sporkpgm.map;

import io.sporkpgm.module.modules.info.InfoModule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {

	InfoModule info;

	public MapLoader() {

	}

	public String getName() {
		return info.getName();
	}

	public static List<MapBuilder> build(File folder) {
		return new ArrayList<>();
	}

}
