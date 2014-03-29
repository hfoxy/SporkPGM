package com.oman.trackerdeaths;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DeathEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private Death death;

	public DeathEvent(Death death) {
		this.death = death;
	}

	public Death getDeath() {
		return death;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
