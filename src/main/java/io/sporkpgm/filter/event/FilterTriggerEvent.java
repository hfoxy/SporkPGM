package io.sporkpgm.filter.event;

import io.sporkpgm.Spork;
import io.sporkpgm.filter.Filter;
import io.sporkpgm.filter.FilterContext;
import io.sporkpgm.map.event.BlockChangeEvent;
import io.sporkpgm.player.SporkPlayer;
import io.sporkpgm.player.event.PlayingPlayerMoveEvent;
import io.sporkpgm.region.FilteredRegion;
import io.sporkpgm.region.FilteredRegion.AppliedValue;
import io.sporkpgm.region.Region;
import io.sporkpgm.util.Log;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.ArrayList;
import java.util.List;

public class FilterTriggerEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private Event event;
	private Location location;
	private SporkPlayer player;
	private FilterContext context;

	public FilterTriggerEvent(Event event, Location location, SporkPlayer player, FilterContext context) {
		this.event = event;
		this.location = location;
		this.player = player;
		this.context = context;
	}

	public FilterTriggerEvent(Event event, Location location, SporkPlayer player, Object... objects) {
		this(event, location, player, new FilterContext(objects));
	}

	public Location getLocation() {
		return location;
	}

	public List<FilteredRegion> getRegionMatches() {
		return getRegionMatches(location);
	}

	public List<FilteredRegion> getRegionMatches(Location location) {
		List<FilteredRegion> filterList = new ArrayList<>();
		for(Region region : Spork.get().getMatch().getMap().getContainingRegions(location)) {
			if(region instanceof FilteredRegion) {
				FilteredRegion filtered = (FilteredRegion) region;
				filterList.add(filtered);
			}
		}
		return filterList;
	}

	public List<Filter> getFilterMatches() {
		List<Filter> filterList = new ArrayList<>();
		for(Region region : Spork.get().getMatch().getMap().getContainingRegions(location)) {
			if(region instanceof FilteredRegion) {
				FilteredRegion filtered = (FilteredRegion) region;
				List<Filter> regionFilters = filtered.getFilters();
				// Log.info("Checking '" + filtered.getName() + "' for " + context.toString() + " contains " + regionFilters.size() + " Filters");
				for(Filter filter : regionFilters) {
					filterList.add(filter);
				}
			}
		}
		return filterList;
	}

	public FilterContext getFilterContext() {
		return context;
	}

	public void cancel() {
		try {
			if(event instanceof PlayingPlayerMoveEvent) {
				PlayingPlayerMoveEvent move = (PlayingPlayerMoveEvent) event;
				move.getPlayer().getPlayer().teleport(move.getFrom());
			} else if(event instanceof BlockChangeEvent) {
				((BlockChangeEvent) event).setCancelled(true);
			} else if(event instanceof Cancellable) {
				((Cancellable) event).setCancelled(true);
			}

			SporkPlayer player = null;
			AppliedValue value = null;
			if(event instanceof PlayingPlayerMoveEvent) {
				PlayingPlayerMoveEvent move = (PlayingPlayerMoveEvent) event;
				player = move.getPlayer();
				value = AppliedValue.ENTER;
			} else if(event instanceof BlockChangeEvent) {
				player = ((BlockChangeEvent) event).getPlayer();
			} else if(event instanceof PlayerEvent) {
				player = SporkPlayer.getPlayer(((PlayerEvent) event).getPlayer());
			}

			for(FilteredRegion region : getRegionMatches()) {
				if(player != null) {
					region.apply(value, player);
				}
			}
		} catch(NullPointerException e) {
			Log.severe(e.getMessage());
		}
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
