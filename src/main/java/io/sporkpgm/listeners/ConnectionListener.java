package io.sporkpgm.listeners;

import io.sporkpgm.Spork;
import io.sporkpgm.player.SporkPlayer;
import io.sporkpgm.util.SchedulerUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		new SchedulerUtil(new Runnable() {

			@Override
			public void run() {
				SporkPlayer player = SporkPlayer.getPlayer(event.getPlayer());
				player.setTeam(Spork.get().getRotation().getCurrent().getObservers());
				player.updateInventory();
			}

		}, false).delay(1);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		SporkPlayer.remove(event.getPlayer());
	}

}
