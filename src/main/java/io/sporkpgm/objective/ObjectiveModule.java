package io.sporkpgm.objective;

import io.sporkpgm.module.Module;
import io.sporkpgm.team.SporkTeam;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

@SuppressWarnings("ALL")
public abstract class ObjectiveModule extends Module {

	public abstract SporkTeam getTeam();

	public abstract boolean isComplete();

	public abstract void setComplete(boolean complete);

	public abstract OfflinePlayer getPlayer();

	public abstract void update();

	public abstract ChatColor getStatusColour();

}
