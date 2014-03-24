package io.sporkpgm.region.types;

import org.bukkit.util.Vector;

public class RectangleRegion extends CuboidRegion {

	public RectangleRegion(String name, BlockRegion min, BlockRegion max) {
		super(name, new BlockRegion(min.getStringX(), "-oo", min.getStringZ()), new BlockRegion(max.getStringX(), "oo", max.getStringZ()));
	}

	@Override
	public boolean isInside(BlockRegion block) {
		return isInside(block, false);
	}

	@Override
	public boolean isInside(BlockRegion block, boolean log) {
		Vector point = new Vector(block.getX(), block.getY(), block.getZ());
		double xMin, xMax, zMin, zMax;
		xMin = getPoints()[1].getX();
		xMax = getPoints()[0].getX();
		zMin = getPoints()[1].getZ();
		zMax = getPoints()[0].getZ();

		return (xMin <= point.getX()) && (point.getX() <= xMax) && (zMin <= point.getZ()) && (point.getZ() <= zMax);
	}

}
