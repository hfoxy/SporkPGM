package io.sporkpgm.player.event;

import io.sporkpgm.player.SporkPlayer;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayingPlayerMoveEvent {

	Event event;
	SporkPlayer player;

	Location from;
	Location to;

	public PlayingPlayerMoveEvent(Event event, SporkPlayer player, Location from, Location to) {
		this.event = event;
		this.player = player;
		this.from = from;
		this.to = to;
	}

	public Event getEvent() {
		return event;
	}

	public SporkPlayer getPlayer() {
		return player;
	}

	public Location getFrom() {
		return from;
	}

	public Location getTo() {
		return to;
	}

	public void cancel() {
		if(!(getEvent() instanceof PlayerMoveEvent)) {
			return;
		}

		PlayerMoveEvent event = (PlayerMoveEvent) getEvent();
		event.getPlayer().teleport(event.getFrom());
	}

}
