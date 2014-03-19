package io.sporkpgm.map;

import io.sporkpgm.util.Config;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;

public class MapLoader {

	public List<SporkMap> loadMaps(File dir) {
		List<SporkMap> maps = Lists.newArrayList();

		for (File f : dir.listFiles()) {
			if (!valid(f))
				continue;
			//TODO: Actually load the map
		}
		return maps;
	}

	private boolean valid(File file) {
		return new File(file, "level.dat").isFile() && new File(file, "region").isDirectory() && new File(file, Config.Map.CONFIG).isFile();
	}

}
