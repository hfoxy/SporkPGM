package io.sporkpgm.filter.event;

import io.sporkpgm.Spork;
import io.sporkpgm.filter.Filter;
import io.sporkpgm.filter.FilterContext;
import io.sporkpgm.player.SporkPlayer;
import io.sporkpgm.region.Region;
import io.sporkpgm.util.Log;
import org.bukkit.Location;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class FilterTriggerEvent {

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

	public List<Filter> getFilterMatches() {
		List<Filter> filterList = new ArrayList<>();
		for(Region r : Spork.get().getMatch().getMap().getContainingRegions(location)) {
			List<Filter> regionFilters = r.getFilters();
			for(Filter f : regionFilters) {
				filterList.add(f);
			}
		}
		return filterList;
	}

	public FilterContext getFilterContext() {
		return context;
	}

	public void cancel() {
		try {
			context.getCause().setCancelled(true);
		} catch(NullPointerException e) {
			Log.severe(e.getMessage());
		}
	}

}
