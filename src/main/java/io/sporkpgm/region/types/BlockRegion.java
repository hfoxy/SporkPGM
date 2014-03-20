package io.sporkpgm.region.types;

import io.sporkpgm.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BlockRegion extends Region {

	public static int INFINITE_POSITIVE = Integer.MAX_VALUE;
	public static int INFINITE_NEGATIVE = Integer.MIN_VALUE;

	String x;
	String y;
	String z;

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
		this.y = y;
		this.z = z;
	}

	public boolean isXDouble() {
		return x.contains(".");
	}

	public boolean isXInfinite() {
		return x.equals("@") || x.equals("-@");
	}

	public boolean isXNegative() {
		return isXInfinite() ? x.equals("-@") : getDoubleX() < 0;
	}

	public double getX() {
		return isXDouble() ? getDoubleX() : getIntegerX();
	}

	public int getIntegerX() {
		try {
			return Integer.parseInt(x);
		} catch(NumberFormatException e) {
			if(isXInfinite()) {
				return isXNegative() ? INFINITE_NEGATIVE : INFINITE_POSITIVE;
			}
			throw new NumberFormatException(x);
		}
	}

	public double getDoubleX() {
		try {
			return Double.valueOf(x);
		} catch(NumberFormatException e) {
			if(isYInfinite()) {
				return isYNegative() ? INFINITE_NEGATIVE : INFINITE_POSITIVE;
			}
			throw new NumberFormatException(x);
		}
	}

	public boolean isYDouble() {
		return y.contains(".");
	}

	public boolean isYInfinite() {
		return y.equals("@") || y.equals("-@");
	}

	public boolean isZNegative() {
		return isZInfinite() ? z.equals("-@") : getDoubleZ() < 0;
	}

	public double getY() {
		return isYDouble() ? getDoubleY() : getIntegerY();
	}

	public int getIntegerY() {
		try {
			return Integer.parseInt(y);
		} catch(NumberFormatException e) {
			if(isYInfinite()) {
				return isYNegative() ? INFINITE_NEGATIVE : INFINITE_POSITIVE;
			}
			throw new NumberFormatException(y);
		}
	}

	public double getDoubleY() {
		try {
			return Double.valueOf(y);
		} catch(NumberFormatException e) {
			if(isYInfinite()) {
				return isYNegative() ? INFINITE_NEGATIVE : INFINITE_POSITIVE;
			}
			throw new NumberFormatException(y);
		}
	}

	public boolean isZDouble() {
		return z.contains(".");
	}

	public boolean isZInfinite() {
		return z.equals("@") || z.equals("-@");
	}

	public boolean isYNegative() {
		return isYInfinite() ? y.equals("-@") : getDoubleY() < 0;
	}

	public double getZ() {
		return isZDouble() ? getDoubleZ() : getIntegerZ();
	}

	public Vector toVector() {
		return new Vector(getIntegerX(), getIntegerY(), getIntegerZ());
	}

	public int getIntegerZ() {
		try {
			return Integer.parseInt(z);
		} catch(NumberFormatException e) {
			if(isZInfinite()) {
				return isZNegative() ? INFINITE_NEGATIVE : INFINITE_POSITIVE;
			}
			throw new NumberFormatException(z);
		}
	}

	public double getDoubleZ() {
		try {
			return Double.valueOf(z);
		} catch(NumberFormatException e) {
			if(isYInfinite()) {
				return isYNegative() ? INFINITE_NEGATIVE : INFINITE_POSITIVE;
			}
			throw new NumberFormatException(z);
		}
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

	@Override
	public List<BlockRegion> getValues() {
		List<BlockRegion> region = new ArrayList<>();
		region.add(this);
		return region;
	}

	public boolean matchesXZ(BlockRegion block) {
		return (block.getIntegerX() == getIntegerX()) && (block.getIntegerZ() == getIntegerZ());
	}

	public boolean isInside(BlockRegion block) {
		boolean isX = (block.isXInfinite() || isXInfinite()) || block.getX() == getX();
		boolean isY = (block.isYInfinite() || isYInfinite()) || block.getY() == getY();
		boolean isZ = (block.isZInfinite() || isZInfinite()) || block.getZ() == getZ();
		return isX && isY && isZ;
	}

	public boolean isAbove(BlockRegion block) {
		return matchesXZ(block) && block.getIntegerY() > getIntegerY();
	}

	public boolean isBelow(BlockRegion block) {
		return matchesXZ(block) && block.getIntegerY() < getIntegerY();
	}

}
