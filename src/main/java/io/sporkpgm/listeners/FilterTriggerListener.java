package io.sporkpgm.listeners;

import io.sporkpgm.filter.Filter;
import io.sporkpgm.filter.event.FilterTriggerEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class FilterTriggerListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onFilterTriggerEvent(FilterTriggerEvent event) {
		for(Filter filter : event.getFilterMatches()) {
			if(!filter.matches(event.getFilterContext()).toBoolean()) {
				event.cancel();
			}
		}
	}
}
