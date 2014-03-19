package io.sporkpgm.base;

import org.bukkit.entity.Player;

public class User {

	private Player player;
	
	public User(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public String getName() {
		return getPlayer().getName();
	}
	
	public void sendMessage(String message) {
		getPlayer().sendMessage(message);
	}
	
}
