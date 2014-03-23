package io.sporkpgm.region.types;

import io.sporkpgm.region.Region;
import io.sporkpgm.util.RegionUtil;

import java.util.ArrayList;
import java.util.List;

public class CylinderRegion extends Region {

	BlockRegion center;
	double radius;
	double height;
	boolean hollow;

	List<BlockRegion> values;
	boolean infinite;

	public CylinderRegion(BlockRegion centre, double radius, double height, boolean hollow) {
		this(null, centre, radius, height, hollow);
	}

	public CylinderRegion(String name, BlockRegion centre, double radius, double height, boolean hollow) {
		super(name);
		this.values = new ArrayList<>();
		if(!(centre.isXInfinite() || centre.isYInfinite() || centre.isZInfinite())) {
			this.values = RegionUtil.cylinder(centre, radius, 1, hollow, false);
		} else if(centre.isYInfinite() && !(centre.isXInfinite() || centre.isZInfinite())) {
			this.values = RegionUtil.cylinder(new BlockRegion(centre.getX() + "", "@", centre.getZ() + ""), radius, 1, hollow, false);
			this.infinite = true;
		}

		this.center = centre;
		this.radius = radius;
		this.height = height;
		this.hollow = hollow;
	}

	public List<BlockRegion> getValues() {
		return values;
	}

	@Override
	public boolean isInside(BlockRegion block) {
		if(infinite) {
			return matchesXZ(block);
		}

		BlockRegion check = new BlockRegion(block.getStringX(), values.get(0).getStringY(), block.getStringZ());
		double max = check.getDoubleY() + height;

		for(BlockRegion region : getValues()) {
			if(region.isInside(check) && block.getDoubleY() <= max) {
				return true;
			}
		}

		return false;
	}

	public boolean matchesXZ(BlockRegion region) {
		BlockRegion block = new BlockRegion(region.getX(), center.getY(), region.getZ());
		CylinderRegion cyl = new CylinderRegion(center, radius, 1, hollow);
		return cyl.isInside(block);
	}

	public boolean isAbove(BlockRegion region) {
		if(infinite || !matchesXZ(region)) {
			return false;
		}

		CuboidRegion cube = new CuboidRegion(region, new BlockRegion(region.getX(), region.getY() + height, region.getZ()));
		return cube.isAbove(region);
	}

	public boolean isBelow(BlockRegion region) {
		if(infinite || !matchesXZ(region)) {
			return false;
		}

		CuboidRegion cube = new CuboidRegion(region, new BlockRegion(region.getX(), region.getY() + height, region.getZ()));
		return cube.isBelow(region);
	}

}
