package io.sporkpgm.region.types;

import io.sporkpgm.region.Region;
import io.sporkpgm.util.RegionUtil;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CuboidRegion extends Region {

	protected BlockRegion one;
	protected BlockRegion two;

	public CuboidRegion(BlockRegion one, BlockRegion two) {
		this(null, one, two);
	}

	public CuboidRegion(String name, BlockRegion one, BlockRegion two) {
		super(name);
		double xMin, xMax, yMin, yMax, zMin, zMax = 0;
		xMin = Math.min(one.getX(), two.getX());
		xMax = Math.max(one.getX(), two.getX()) - 1;
		yMin = Math.min(one.getY(), two.getY());
		yMax = Math.max(one.getY(), two.getY()) - 1;
		zMin = Math.min(one.getZ(), two.getZ());
		zMax = Math.max(one.getZ(), two.getZ()) - 1;

		this.one = new BlockRegion(xMax, yMax, zMax);
		this.two = new BlockRegion(xMin, yMin, zMin);
	}

	public BlockRegion[] getPoints() {
		return new BlockRegion[]{one, two};
	}

	public List<BlockRegion> getValues() {
		return RegionUtil.cuboid(one, two);
	}

	@Override
	public boolean isInside(BlockRegion block) {
		double xMin, xMax, yMin, yMax, zMin, zMax = 0;
		xMin = getPoints()[1].getX();
		xMax = getPoints()[0].getX();
		yMin = getPoints()[1].getY();
		yMax = getPoints()[0].getY();
		zMin = getPoints()[1].getZ();
		zMax = getPoints()[0].getZ();

		Vector point = new Vector(block.getX(), block.getY(), block.getZ());
		Vector min = new Vector(xMin, yMin, zMin);
		Vector max = new Vector(xMax, yMax, zMax);

		return point.isInAABB(min, max);
	}

	public boolean isAboveOrBelow(BlockRegion region) {
		BlockRegion block = new BlockRegion(region.getX(), getPoints()[0].getY(), region.getZ());
		if(isInside(block)) {
			double yMin = getPoints()[1].getY();
			double yMax = getPoints()[0].getY();
			if(region.getY() > yMax || region.getY() < yMin) {
				return true;
			}
		}

		return false;
	}

	public boolean isAbove(BlockRegion region) {
		BlockRegion block = new BlockRegion(region.getX(), getPoints()[0].getY(), region.getZ());
		if(isInside(block)) {
			double yMax = getPoints()[0].getY();
			if(region.getY() > yMax) {
				return true;
			}
		}

		return false;
	}

	public boolean isBelow(BlockRegion region) {
		BlockRegion block = new BlockRegion(region.getX(), getPoints()[0].getY(), region.getZ());
		if(isInside(block)) {
			double yMin = getPoints()[1].getY();
			if(region.getY() < yMin) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return "CuboidRegion{name=" + getName() + ",min=[" + one + "],max=[" + two + "]}";
	}

}
