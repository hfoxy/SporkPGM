package io.sporkpgm.region.types;

public class CircleRegion extends CylinderRegion {

	public CircleRegion(String name, BlockRegion centre, double radius, boolean hollow) {
		super(name, new BlockRegion(centre.getStringX(), "-oo", centre.getStringZ()), radius, 1, hollow);
	}

	@Override
	public boolean isInside(BlockRegion block) {
		return matchesXZ(block);
	}

}
