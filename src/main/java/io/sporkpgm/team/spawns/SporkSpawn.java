package io.sporkpgm.team.spawns;

import io.sporkpgm.region.Region;
import io.sporkpgm.rotation.RotationSlot;
import io.sporkpgm.team.spawns.kits.SporkKit;
import io.sporkpgm.util.RegionUtil;
import org.bukkit.Location;
import org.bukkit.Material;

public class SporkSpawn {

	private String name;
	private Region region;
	private SporkKit kit;

	private float yaw;
	private float pitch;

	public SporkSpawn(String name, Region region) {
		this(name, region, null, 0, 0);
	}

	public SporkSpawn(String name, Region region, float yaw) {
		this(name, region, null, yaw, 0);
	}

	public SporkSpawn(String name, Region region, float yaw, float pitch) {
		this(name, region, null, yaw, pitch);
	}

	public SporkSpawn(String name, Region region, SporkKit kit) {
		this(name, region, kit, 0, 0);
	}

	public SporkSpawn(String name, Region region, SporkKit kit, float yaw) {
		this(name, region, kit, yaw, 0);
	}

	public SporkSpawn(String name, Region region, SporkKit kit, float yaw, float pitch) {
		this.name = name;
		this.region = region;
		this.kit = kit;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public String getName() {
		return name;
	}

	public Region getRegion() {
		return region;
	}

	public Location getSpawn() {
		Location spawn = RegionUtil.getRandom(region.getValues(Material.AIR, RotationSlot.getRotation().getCurrent().getWorld())).getLocation(RotationSlot.getRotation().getCurrent().getWorld());
		spawn.setYaw(yaw);
		spawn.setPitch(pitch);
		return spawn;
	}

	public SporkKit getKit() {
		return kit;
	}

	public boolean hasKit() {
		return kit != null;
	}

	public void setKit(SporkKit kit) {
		this.kit = kit;
	}
	
}
