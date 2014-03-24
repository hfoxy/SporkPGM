package io.sporkpgm.listeners;


import io.sporkpgm.Spork;
import io.sporkpgm.match.MatchPhase;
import io.sporkpgm.player.SporkPlayer;
import io.sporkpgm.rotation.RotationSlot;
import io.sporkpgm.util.Chars;
import io.sporkpgm.util.SchedulerUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

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

	@EventHandler
	public void onServerPing(ServerListPingEvent event){
		MatchPhase phase = RotationSlot.getRotation().getCurrentMatch().getPhase();
		ChatColor status=null;
		if (phase == MatchPhase.WAITING){
			status = ChatColor.GRAY;
		}
		else if (phase == MatchPhase.STARTING){
			status = ChatColor.GREEN;
		}
		else if (phase == MatchPhase.PLAYING){
			status = ChatColor.BLUE;
		}
		else if (phase == MatchPhase.CYCLING){
			status = ChatColor.RED;
		}
		event.setMotd(status + "" + Chars.RAQUO + "" + ChatColor.AQUA + " " + RotationSlot.getRotation().getCurrent().getName() + " " + status + Chars.LAQUO);
	}

}
