package io.sporkpgm.region.types;

import io.sporkpgm.region.Region;
import io.sporkpgm.util.RegionUtil;

import java.util.ArrayList;
import java.util.List;

public class SphereRegion extends Region {

	BlockRegion center;
	double radius;
	boolean hollow;

	List<BlockRegion> values;

	public SphereRegion(BlockRegion centre, double radius, boolean hollow) {
		this(null, centre, radius, hollow);
	}

	public SphereRegion(String name, BlockRegion centre, double radius, boolean hollow) {
		super(name);
		this.values = new ArrayList<>();
		if(!(centre.isXInfinite() || centre.isYInfinite() || centre.isZInfinite())) {
			this.values = RegionUtil.sphere(centre, radius, hollow);
		}

		this.center = centre;
		this.radius = radius;
		this.hollow = hollow;
	}

	public List<BlockRegion> getValues() {
		return values;
	}

	@Override
	public boolean isInside(BlockRegion block) {
		for(BlockRegion region : getValues()) {
			if(region.isInside(block)) {
				return true;
			}
		}

		return false;
	}

	public boolean matchesXZ(BlockRegion region) {
		BlockRegion block = new BlockRegion(region.getX(), center.getY(), region.getZ());
		CylinderRegion cylinder = new CylinderRegion(center, radius, 1, hollow);
		return cylinder.isInside(block);
	}

	public boolean isAbove(BlockRegion region) {
		if(!matchesXZ(region) || isInside(region)) {
			return false;
		}

		return center.getY() < region.getY();
	}

	public boolean isBelow(BlockRegion region) {
		if(!matchesXZ(region) || isInside(region)) {
			return false;
		}

		return center.getY() > region.getY();
	}

}
