package io.sporkpgm.listeners;

import io.sporkpgm.filter.AppliedRegion;
import io.sporkpgm.filter.exceptions.InvalidContextException;
import io.sporkpgm.filter.other.Context;
import io.sporkpgm.map.SporkMap;
import io.sporkpgm.map.event.BlockChangeEvent;
import io.sporkpgm.player.event.PlayingPlayerMoveEvent;
import io.sporkpgm.region.Region;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class FilterTriggerListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockChange(BlockChangeEvent event) {
		apply(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayingPlayerMoveEvent event) {
		apply(event);
	}

	private void apply(Event event) {
		try {
			Context context = new Context(event);
			for(Region region : SporkMap.getMap().getRegions()) {
				if(region instanceof AppliedRegion) {
					AppliedRegion applied = (AppliedRegion) region;
					applied.apply(context);
				}
			}
		} catch(InvalidContextException e) {
			e.printStackTrace();
		}
	}

}
