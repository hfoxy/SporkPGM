package io.sporkpgm.region;

import io.sporkpgm.filter.Filter;
import io.sporkpgm.region.types.BlockRegion;
import io.sporkpgm.region.types.groups.UnionRegion;

import java.util.List;

public class FilteredRegion extends Region {

	private UnionRegion region;
	private List<Filter> filters;

	public FilteredRegion(String name, List<Region> regions, List<Filter> filters) {
		super(name);
		this.region = new UnionRegion(null, regions);
		this.filters = filters;
	}

	public List<BlockRegion> getValues() {
		return region.getValues();
	}

	public boolean isInside(BlockRegion block) {
		return region.isInside(block);
	}

	public boolean isAbove(BlockRegion block) {
		return region.isAbove(block);
	}

	public boolean isBelow(BlockRegion block) {
		return region.isBelow(block);
	}

	public List<Filter> getFilters() {
		return filters;
	}

}
