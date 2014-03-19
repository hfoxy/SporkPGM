package io.sporkpgm.map;

import java.util.Collection;

import tc.oc.commons.core.util.ContextStore;

public class MapManager extends ContextStore<SporkMap> {

	public MapManager(Collection<SporkMap> maps) {
		for(SporkMap map : maps) add("NAME HERE LATER", map);
	}
	
	public Collection<SporkMap> getMaps() {
		return store.values();
	}

	public SporkMap getMap(String map) {
		return store.get(map);
	}

}
