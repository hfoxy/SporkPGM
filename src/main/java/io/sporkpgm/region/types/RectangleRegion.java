package io.sporkpgm.region.types;

import io.sporkpgm.util.Log;

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
		double xMin, xMax, zMin, zMax;
		xMin = getPoints()[1].getX();
		xMax = getPoints()[0].getX();
		zMin = getPoints()[1].getZ();
		zMax = getPoints()[0].getZ();

		double x = block.getX();
		double z = block.getZ();

		boolean isX = x >= xMin && x <= xMax;
		if(log) {
			Log.info("X = (" + x + " >= " + xMin + " = " + (x >= xMin) + " && " + x + " >= " + xMax + " = " + (x >= xMax) + ") = " + isX);
		}

		boolean isZ = z >= zMin && z <= zMax;
		if(log) {
			Log.info("Z = (" + z + " >= " + zMin + " = " + (z >= zMin) + " && " + z + " >= " + zMax + " = " + (z >= zMax) + ") = " + isZ);
		}

		return isX &&  isZ;
	}

}
