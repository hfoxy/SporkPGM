package io.sporkpgm.map;

import io.sporkpgm.util.Config;

import java.io.File;
import java.util.Collection;

import com.google.common.collect.Lists;

public class MapLoader {

	public Collection<SporkMap> loadMaps(File dir) {
		Collection<SporkMap> maps = Lists.newArrayList();

		for (File f : dir.listFiles()) {
			if (!valid(f))
				continue;
			// TODO: Actually load the map
		}
		return maps;
	}

	private boolean valid(File file) {
		return new File(file, "level.dat").isFile() && new File(file, "region").isDirectory() && new File(file, Config.Map.CONFIG).isFile();
	}

}
