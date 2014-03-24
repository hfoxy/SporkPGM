package io.sporkpgm.region;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.match.Match;
import io.sporkpgm.region.exception.InvalidRegionException;
import io.sporkpgm.region.types.BlockRegion;
import io.sporkpgm.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SearchRegion extends Region {

	private SporkMap map;

	private String search;
	private Region region;

	public SearchRegion(String name, String search) {
		super((name != null ? name : "search-" + search));
		this.search = search;
	}

	@Override
	public String getName() {
		try {
			get();
			return region.getName();
		} catch(InvalidRegionException e) {
			Log.info(e.getMessage());
			return "unkown-search";
		}
	}

	public List<BlockRegion> getValues() {
		try {
			get();
			return region.getValues();
		} catch(InvalidRegionException e) {
			Log.info(e.getMessage());
			return new ArrayList<>();
		}
	}

	public boolean isInside(BlockRegion block) {
		try {
			get();
			return region.isInside(block);
		} catch(InvalidRegionException e) {
			Log.info(e.getMessage());
			return false;
		}
	}

	public boolean isAbove(BlockRegion block) {
		try {
			get();
			return region.isAbove(block);
		} catch(InvalidRegionException e) {
			Log.info(e.getMessage());
			return false;
		}
	}

	public boolean isBelow(BlockRegion block) {
		try {
			get();
			return region.isBelow(block);
		} catch(InvalidRegionException e) {
			Log.info(e.getMessage());
			return false;
		}
	}

	private void get() throws InvalidRegionException {
		// Log.info(toString());
		if(region != null && map == Match.getMatch().getMap()) {
			// Log.info("Already found the Region and the map hasn't changed");
			return;
		}

		try {
			this.map = Match.getMatch().getMap();
			this.region = map.getRegion(search);
		} catch(NullPointerException e) {
			throw new InvalidRegionException(null, "Unable to fetch info about the map just yet!");
		}
		// Log.info("Set new Region for " + toString());
	}

	@Override
	public String toString() {
		return "SearchRegion{map=" + map + ",search=" + search + ",region=" + region + "}";
	}

}
