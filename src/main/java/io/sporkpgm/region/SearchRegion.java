package io.sporkpgm.region;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.match.Match;
import io.sporkpgm.region.types.BlockRegion;
import io.sporkpgm.util.Log;

import java.util.List;

public class SearchRegion extends Region {

	private SporkMap map;

	private String search;
	private Region region;

	public SearchRegion(String name, String search) {
		super(name);
		this.search = search;
	}

	public List<BlockRegion> getValues() {
		get();
		return region.getValues();
	}

	public boolean isInside(BlockRegion block) {
		get();
		return region.isInside(block);
	}

	public boolean isAbove(BlockRegion block) {
		get();
		return region.isAbove(block);
	}

	public boolean isBelow(BlockRegion block) {
		get();
		return region.isBelow(block);
	}

	private void get() {
		// Log.info(toString());
		if(region != null && map == Match.getMatch().getMap()) {
			// Log.info("Already found the Region and the map hasn't changed");
			return;
		}

		try {
			this.map = Match.getMatch().getMap();
			this.region = map.getRegion(search);
		} catch(NullPointerException e) {
			Log.info("Unable to fetch info about the map just yet!");
		}
		// Log.info("Set new Region for " + toString());
	}

	@Override
	public String toString() {
		return "SearchRegion{map=" + map + ",search=" + search + ",region=" + region + "}";
	}

}
