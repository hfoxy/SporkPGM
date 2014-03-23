package io.sporkpgm.region;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.match.Match;
import io.sporkpgm.region.types.BlockRegion;

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
		if(region != null && map == Match.getMatch().getMap()) {
			return;
		}

		this.map = Match.getMatch().getMap();
		this.region = map.getRegion(search);
	}

}
