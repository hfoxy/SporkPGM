package io.sporkpgm.map.debug;

import io.sporkpgm.region.Region;
import io.sporkpgm.region.types.BlockRegion;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class VisibleRegion {

	Region region;
	Material material;
	short damage;

	DyeColor dye;

	public VisibleRegion(Region region, Material material, short damage) {
		this.region = region;
		this.material = material;
		this.damage = damage;
		this.dye = DyeColor.getByWoolData((byte) damage);
	}

	public void set(World world) {
		for(BlockRegion value : region.getValues()) {
			Block block = value.getBlock(world);
			block.setType(material);
			block.setData((byte) damage);
		}
	}

	public Region getRegion() {
		return region;
	}

	public Material getMaterial() {
		return material;
	}

	public short getDamage() {
		return damage;
	}

	public DyeColor getDye() {
		return dye;
	}

}
