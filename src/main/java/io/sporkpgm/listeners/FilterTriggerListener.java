package io.sporkpgm.listeners;

import io.sporkpgm.filter.Filter;
import io.sporkpgm.filter.event.FilterTriggerEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class FilterTriggerListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onFilterTriggerEvent(FilterTriggerEvent e) {
		for(Filter f : e.getFilterMatches()) {
			if(!f.matches(e.getFilterContext()).toBoolean()) {
				e.cancel();
			}
		}
	}
}
