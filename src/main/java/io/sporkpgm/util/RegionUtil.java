package io.sporkpgm.util;

import io.sporkpgm.region.types.BlockRegion;

import java.util.ArrayList;
import java.util.List;

public class RegionUtil {

	public static double closest(BlockRegion origin, List<BlockRegion> values) {
		double closest = values.get(0).distance(origin);

		for(int i = 1; i < values.size(); i++) {
			double length = values.get(i).distance(origin);
			if(closest > length) {
				closest = length;
			}
		}

		return closest;
	}

	public static BlockRegion closestBlock(BlockRegion origin, List<BlockRegion> values) {
		BlockRegion value = values.get(0);
		double closest = values.get(0).distance(origin);

		for(int i = 1; i < values.size(); i++) {
			double length = values.get(i).distance(origin);
			if(closest > length) {
				closest = length;
				value = values.get(i);
			}
		}

		return value;
	}

	public static List<BlockRegion> sphere(BlockRegion loc, double radius, boolean hollow) {
		return cylinder(loc, radius, radius, hollow, true);
	}

	public static List<BlockRegion> cylinder(BlockRegion loc, double radius, double height, Boolean hollow,
											 Boolean sphere) {
		if(radius == 0) {
			radius = 1;
		}
		if(height == 0) {
			height = 1;
		}
		List<BlockRegion> circleblocks = new ArrayList<>();
		double cx = loc.getX();
		double cy = loc.getY();
		double cz = loc.getZ();

		for(int x = (int) (cx - radius); x <= cx + radius; x++) {
			for(int z = (int) (cz - radius); z <= cz + radius; z++) {
				for(int y = (int) (sphere ? cy - radius : cy); y < (sphere ? cy + radius : cy + height); y++) {
					double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
					if(dist < radius * radius && !(hollow && dist < (radius - 1) * (radius - 1))) {
						BlockRegion l = new BlockRegion(x, y, z);
						circleblocks.add(l);
					}
				}
			}
		}

		return circleblocks;
	}

	public static List<BlockRegion> cuboid(BlockRegion corner1, BlockRegion corner2) {
		List<BlockRegion> blocks = new ArrayList<>();

		double xMin, xMax, yMin, yMax, zMin, zMax = 0;
		xMin = Math.min(corner1.getX(), corner2.getX());
		xMax = Math.max(corner1.getX(), corner2.getX());
		yMin = Math.min(corner1.getY(), corner2.getY());
		yMax = Math.max(corner1.getY(), corner2.getY());
		zMin = Math.min(corner1.getZ(), corner2.getZ());
		zMax = Math.max(corner1.getZ(), corner2.getZ());

		double px = xMin;
		while(px <= xMax) {
			double py = yMin;
			while(py <= yMax) {
				double pz = zMin;
				while(pz <= zMax) {
					blocks.add(new BlockRegion(px, py, pz));
					pz++;
				}
				py++;
			}
			px++;
		}

		return blocks;
	}

	public static BlockRegion getRandom(List<BlockRegion> list) {
		int min = 0;
		int max = list.size() - 1;
		int random = NumberUtil.getRandom(min, max);
		return list.get(random);
	}

}
