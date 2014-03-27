package io.sporkpgm.filter.region;

import io.sporkpgm.region.Region;
import io.sporkpgm.region.types.groups.UnionRegion;

import java.util.List;

public class AppliedRegion extends UnionRegion {

	public AppliedRegion(String name, List<Region> regions) {
		super("applied-" + name + "-union", regions);
	}

}
