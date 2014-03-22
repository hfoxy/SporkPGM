package io.sporkpgm.listeners;

import io.sporkpgm.map.event.BlockChangeEvent;
import io.sporkpgm.region.Region;
import io.sporkpgm.rotation.RotationSlot;
import io.sporkpgm.team.spawns.SporkSpawn;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MapListener implements Listener {

	@EventHandler
	public void onBlockChange(BlockChangeEvent event) {
		if(isSpawn(event.getLocation()))
			event.setCancelled(true);
	}

	public boolean isSpawn(Location location) {
		for(SporkSpawn spawn : RotationSlot.getRotation().getCurrent().getSpawns())
			for(Region region : spawn.getRegion())
				if(region.isInside(location))
					return true;

		return false;
	}

}
