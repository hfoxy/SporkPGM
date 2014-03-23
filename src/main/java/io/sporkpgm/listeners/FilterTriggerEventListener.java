package io.sporkpgm.listeners;

import static io.sporkpgm.Spork.callEvent;
import io.sporkpgm.filter.FilterContext;
import io.sporkpgm.filter.event.FilterTriggerEvent;
import io.sporkpgm.map.event.BlockChangeEvent;
import io.sporkpgm.player.event.PlayingPlayerMoveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class FilterTriggerEventListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockChangeEvent event) {
		FilterContext context = new FilterContext(event, event.getNewBlock(), event.getPlayer().getTeam());
		callEvent(new FilterTriggerEvent(event, event.getLocation(), event.getPlayer(), context));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntitySpawn(CreatureSpawnEvent event) {
		FilterContext context = new FilterContext(event, event.getEntityType());
		callEvent(new FilterTriggerEvent(event, event.getLocation(), null, context));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayingPlayerMoveEvent event) {
		FilterContext context = new FilterContext(event, event.getPlayer().getTeam());
		callEvent(new FilterTriggerEvent(event, event.getTo(), event.getPlayer(), context));
	}

}
