package io.sporkpgm.player.event;

import io.sporkpgm.player.SporkPlayer;
import org.bukkit.event.Event;

public class PlayerDealPlayerDamageEvent {

	Event event;
	SporkPlayer player;

	private SporkPlayer victim;

	public PlayerDealPlayerDamageEvent(Event event, SporkPlayer player, SporkPlayer victim) {
		this.event = event;
		this.player = player;
		this.victim = victim;
	}

	public SporkPlayer getVictim() {
		return victim;
	}

}
