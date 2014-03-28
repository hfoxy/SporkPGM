package io.sporkpgm.listeners;

import io.sporkpgm.filter.AppliedRegion;
import io.sporkpgm.filter.exceptions.InvalidContextException;
import io.sporkpgm.filter.other.Context;
import io.sporkpgm.map.SporkMap;
import io.sporkpgm.map.event.BlockChangeEvent;
import io.sporkpgm.player.event.PlayingPlayerMoveEvent;
import io.sporkpgm.region.Region;
import io.sporkpgm.util.Log;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class FilterTriggerListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockChange(BlockChangeEvent event) {
		if(event.hasPlayer()) {
			Log.info("Checking " + event.getPlayer().getName() + "'s Block " + (event.isBreak() ? "Break" : "Place") + " for any filter incursions (" + event.getRegion() + ")");
		}

		apply(event, true);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerMove(PlayingPlayerMoveEvent event) {
		apply(event);
	}

	private void apply(Event event) {
		apply(event, false);
	}

	private void apply(Event event, boolean log) {
		try {
			Context context = new Context(event);
			for(Region region : SporkMap.getMap().getRegions()) {
				if(log) {
					Log.info("Checking if " + region.getClass() + " is an instance of AppliedRegion (" + (region.getClass() == AppliedRegion.class) + ")");
				}
				if(region.getClass() == AppliedRegion.class) {
					AppliedRegion applied = (AppliedRegion) region;
					if(log) {
						Log.info("Checking Filters for '" + region.getName() + "'");
					}
					applied.apply(context, log);
				}
			}
		} catch(InvalidContextException e) {
			e.printStackTrace();
		}
	}

}
