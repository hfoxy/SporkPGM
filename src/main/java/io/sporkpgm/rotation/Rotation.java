package io.sporkpgm.rotation;

import io.sporkpgm.Spork;
import io.sporkpgm.map.MapManager;
import io.sporkpgm.map.SporkMap;
import io.sporkpgm.util.Config;
import io.sporkpgm.util.Log;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

public class Rotation {

	private MapManager mapManager;

	private List<SporkMap> rotation = Lists.newArrayList();

	public Rotation(MapManager mapManager, File rotation) {
		this.mapManager = mapManager;

		this.rotation = loadRotation(rotation);
	}

	public List<SporkMap> getRotation() {
		return rotation;
	}

	@SuppressWarnings("unchecked")
	private List<SporkMap> loadRotation(File rotation) {
		List<String> raw = Lists.newArrayList();
		
		try {
			raw = FileUtils.readLines(new File(Spork.get().getDataFolder(), Config.Rotation.ROTATION));
		} catch (Exception e) {
			Log.log(e);
		}
		
		List<SporkMap> maps = Lists.newArrayList();

		for (String s : raw) {
			SporkMap map = mapManager.getMap(s);
			if (map != null)
				maps.add(map);
		}

		return maps;
	}

}
