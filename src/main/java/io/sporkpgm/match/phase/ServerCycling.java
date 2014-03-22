package io.sporkpgm.match.phase;

import io.sporkpgm.Spork;
import io.sporkpgm.match.Match;
import io.sporkpgm.match.MatchPhase;
import io.sporkpgm.module.exceptions.ModuleLoadException;
import io.sporkpgm.player.SporkPlayer;
import io.sporkpgm.region.exception.InvalidRegionException;
import io.sporkpgm.rotation.Rotation;
import io.sporkpgm.rotation.RotationSlot;
import io.sporkpgm.rotation.exceptions.RotationLoadException;
import io.sporkpgm.util.Log;
import io.sporkpgm.util.NMSUtil;
import io.sporkpgm.util.NumberUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.lang.reflect.Method;
import java.util.List;

public class ServerCycling extends ServerPhase {

	public ServerCycling(Match match, MatchPhase phase) {
		this.match = match;
		this.phase = phase;
		this.duration = phase.getDuration() * 20;
	}

	@Override
	public void run() {
		if(complete)
			return;
		Rotation rotation = Spork.get().getRotation();
		RotationSlot next = rotation.getNextSlot();

		if(next.getMatch() == null) {
			try {
				next.load();
			} catch(RotationLoadException e) {
				Log.severe(e.getMessage());
				next = null;
			} catch(InvalidRegionException e) {
				Log.severe(e.getMessage());
				next = null;
			} catch(ModuleLoadException e) {
				Log.severe(e.getMessage());
				next = null;
			}
		}

		ChatColor colour = ChatColor.GRAY;
		String what = "Server restarting";
		if(next != null) {
			colour = ChatColor.AQUA;
			what = "Cycling to " + ChatColor.DARK_AQUA + next.getMap().getName() + colour;
		}

		try {
			List<SporkPlayer> players = match.getMap().getWinner().getPlayers();
			if(players.size() > 0 && isFullSecond()) { // check for more than 1 player
				SporkPlayer player = players.get(NumberUtil.getRandom(0, players.size() - 1));

				Location location = player.getPlayer().getLocation();
				Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
				FireworkMeta meta = firework.getFireworkMeta();
				meta.clearEffects();
				meta.addEffect(getFirework());
				firework.setFireworkMeta(meta);

				try {
					Method method = firework.getClass().getMethod("getHandle");
					method.setAccessible(true);
					method.invoke(firework);
				} catch(Exception e) {
					Log.warning("Server isn't running a version of Bukkit which allows the use of Firework.detonate() - resorting to manual detonation.");
					try {
						Object craft = NMSUtil.getClass("CraftFirework").cast(firework);
						Method method = craft.getClass().getMethod("getHandle");
						method.setAccessible(true);
						Object handle = method.invoke(craft);
						handle.getClass().getField("expectedLifespan").set(handle, 0);
					} catch(Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		} catch(NullPointerException ignored) {

		}

		if(duration <= 0) {
			broadcast(colour + what + "!");
			match.stop();
			complete = true;
			rotation.cycle();
			return;
		}

		boolean show = false;
		if(getTicks() % 20 == 0) {
			if(getSeconds() % 30 == 0)
				show = true;
			else if(getSeconds() < 30 && getSeconds() % 15 == 0)
				show = true;
			else if(getSeconds() < 15 && getSeconds() % 5 == 0)
				show = true;
			else if(getSeconds() < 5)
				show = true;
		}

		if(show)
			broadcast(colour + what + " in " + ChatColor.RED + getSeconds() + " second" + (getSeconds() != 1 ? "s" : "") + colour + "!");
		setTicks(getTicks() - 1);
	}

}
