package io.sporkpgm.region.types;

import io.sporkpgm.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockRegion extends Region {

	public static int INFINITE_POSITIVE = Integer.MAX_VALUE;
	public static int INFINITE_NEGATIVE = Integer.MIN_VALUE;

	String x;
	double xD;
	int xI;

	String y;
	double yD;
	int yI;

	String z;
	double zD;
	int zI;

	public BlockRegion(int x, int y, int z) {
		this(null, x + "", y + "", z + "");
	}

	public BlockRegion(String name, int x, int y, int z) {
		this(name, x + "", y + "", z + "");
	}

	public BlockRegion(double x, double y, double z) {
		this(null, x + "", y + "", z + "");
	}

	public BlockRegion(String name, double x, double y, double z) {
		this(name, x + "", y + "", z + "");
	}

	public BlockRegion(String x, String y, String z) {
		this(null, x, y, z);
	}

	public BlockRegion(String name, String x, String y, String z) {
		super(name);
		this.x = x;
		this.xD = parseDouble(x);
		this.xI = parseInteger(x);

		this.y = y;
		this.yD = parseDouble(y);
		this.yI = parseInteger(y);

		this.z = z;
		this.zD = parseDouble(z);
		this.zI = parseInteger(z);
	}

	public double parseDouble(String value) {
		try {
			return Double.valueOf(value);
		} catch(NumberFormatException e) {
			if(isInfinite(value)) {
				return isNegative(value) ? INFINITE_NEGATIVE : INFINITE_POSITIVE;
			}
			throw new NumberFormatException(value);
		}
	}

	public int parseInteger(String value) {
		try {
			return Integer.parseInt(value);
		} catch(NumberFormatException e) {
			if(isInfinite(value)) {
				return isNegative(value) ? INFINITE_NEGATIVE : INFINITE_POSITIVE;
			}

			return (int) parseDouble(value);
		}
	}

	public boolean isInfinite(String value) {
		return value.equals("oo") || value.equals("-oo");
	}

	public boolean isNegative(String value) {
		return isInfinite(value) ? x.equals("-oo") : getDoubleX() < 0;
	}

	public double getX() {
		return getDoubleX();
	}

	public String getStringX() {
		return x;
	}

	public double getDoubleX() {
		return xD;
	}

	public int getIntegerX() {
		return xI;
	}

	public boolean isXDouble() {
		return x.contains(".");
	}

	public boolean isXInfinite() {
		return x.equals("oo") || x.equals("-oo");
	}

	public boolean isXNegative() {
		return isXInfinite() ? x.equals("-oo") : getDoubleX() < 0;
	}

	public double getY() {
		return getDoubleY();
	}

	public String getStringY() {
		return y;
	}

	public double getDoubleY() {
		return yD;
	}

	public int getIntegerY() {
		return yI;
	}

	public boolean isYDouble() {
		return y.contains(".");
	}

	public boolean isYInfinite() {
		return isInfinite(y);
	}

	public boolean isYNegative() {
		return isNegative(y);
	}

	public double getZ() {
		return getDoubleZ();
	}

	public String getStringZ() {
		return z;
	}

	public double getDoubleZ() {
		return zD;
	}

	public int getIntegerZ() {
		return zI;
	}

	public boolean isZDouble() {
		return z.contains(".");
	}

	public boolean isZInfinite() {
		return isInfinite(z);
	}

	public boolean isZNegative() {
		return isNegative(z);
	}

	public Material getMaterial(World world) {
		return getBlock(world).getType();
	}

	public Location getLocation(World world) {
		return new Location(world, (isXDouble() ? getDoubleX() : getIntegerX()), (isYDouble() ? getDoubleY() :
				getIntegerY()), (isZDouble() ? getDoubleZ() : getIntegerZ()));
	}

	public Block getBlock(World world) {
		return getLocation(world).getBlock();
	}

	public double distance(BlockRegion other) {
		World world = Bukkit.getWorlds().get(0);
		Location one = getLocation(world);
		Location two = other.getLocation(world);
		return one.distance(two);
	}

	public List<BlockRegion> getValues() {
		List<BlockRegion> region = new ArrayList<>();
		region.add(this);
		return region;
	}

	public boolean matchesXZ(BlockRegion block) {
		return (block.getIntegerX() == getIntegerX()) && (block.getIntegerZ() == getIntegerZ());
	}

	public boolean isInside(BlockRegion block) {
		boolean isX = (block.isXInfinite() || isXInfinite()) || block.getIntegerX() == getIntegerX();
		boolean isY = (block.isYInfinite() || isYInfinite()) || block.getIntegerY() == getIntegerY();
		boolean isZ = (block.isZInfinite() || isZInfinite()) || block.getIntegerZ() == getIntegerZ();
		return isX && isY && isZ;
	}

	public boolean isAbove(BlockRegion block) {
		return matchesXZ(block) && block.getIntegerY() > getIntegerY();
	}

	public boolean isBelow(BlockRegion block) {
		return matchesXZ(block) && block.getIntegerY() < getIntegerY();
	}

	@Override
	public String toString() {
		return "BlockRegion{name=" + getName() + ",x=" + x + ",y=" + y + ",z=" + z + "}";
	}

}
