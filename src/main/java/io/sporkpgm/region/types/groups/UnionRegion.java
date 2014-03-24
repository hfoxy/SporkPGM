package io.sporkpgm.region.types.groups;

import io.sporkpgm.region.Region;
import io.sporkpgm.region.types.BlockRegion;
import io.sporkpgm.util.Log;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class UnionRegion extends Region {

	List<Region> regions;

	public UnionRegion(List<Region> regions) {
		this(null, regions);
	}

	public UnionRegion(String name, List<Region> regions) {
		super(name);
		this.regions = regions;
	}

	@Override
	public boolean isInside(BlockRegion block) {
		return isInside(block, false);
	}

	@Override
	public boolean isInside(BlockRegion block, boolean log) {
		for(Region region : regions) {
			if(log) {
				Log.info("Checking '" + region.getName() + "' (" + region.getClass().getSimpleName() + ") for " + block);
			}

			if(region.isInside(block, log)) {
				// Log.info(block + " was found inside '" + region.getName() + "'");
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isInside(Material material, World world) {
		for(BlockRegion region : getValues()) {
			if(region.getMaterial(world) == material) {
				return true;
			}
		}

		return false;
	}

	@Override
	public List<BlockRegion> getValues() {
		List<BlockRegion> blocks = new ArrayList<>();
		for(Region region : regions) {
			blocks.addAll(region.getValues());
		}
		return blocks;
	}

	@Override
	public List<BlockRegion> getValues(Material material, World world) {
		List<BlockRegion> blocks = new ArrayList<>();
		for(BlockRegion region : getValues()) {
			if(region.getMaterial(world) == material) {
				blocks.add(region);
			}
		}
		return blocks;
	}

	public List<Region> getRegions() {
		return regions;
	}

	@Override
	public List<BlockRegion> getValues(Material material, int damage, World world) {
		List<BlockRegion> blocks = new ArrayList<>();
		for(BlockRegion region : getValues()) {
			if(region.getMaterial(world) == material && region.getBlock(world).getData() == (byte) damage) {
				blocks.add(region);
			}
		}
		return blocks;
	}

	public boolean isAbove(BlockRegion block) {
		return false;
	}

	public boolean isBelow(BlockRegion block) {
		return false;
	}

}
