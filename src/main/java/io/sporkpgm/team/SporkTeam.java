package io.sporkpgm.team;

import io.sporkpgm.Spork;
import io.sporkpgm.map.SporkMap;
import io.sporkpgm.objective.ObjectiveModule;
import io.sporkpgm.objective.scored.ScoredObjective;
import io.sporkpgm.player.SporkPlayer;
import io.sporkpgm.team.spawns.SporkSpawn;
import io.sporkpgm.util.NumberUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class SporkTeam {

	SporkMap map;
	Team team;
	OfflinePlayer player;
	ScoredObjective scored;

	String name;
	ChatColor color;
	ChatColor overhead;
	int max;
	int overfill;
	boolean observers;
	String display;
	boolean capped;
	boolean closed;
	boolean ready;
	List<SporkSpawn> spawns;

	SporkTeam(SporkMap map, String name, ChatColor color) {
		this(map, name, color, color, 0, 0, true);
	}

	SporkTeam(SporkMap map, String name, ChatColor color, int max) {
		this(map, name, color, color, max, max + max / 4, false);
	}

	SporkTeam(SporkMap map, String name, ChatColor color, int max, int overfill) {
		this(map, name, color, color, max, overfill, false);
	}

	SporkTeam(SporkMap map, String name, ChatColor color, ChatColor overhead, int max, int overfill, boolean observers) {
		this.map = map;
		this.name = name;
		this.color = color;
		this.overhead = overhead;
		this.max = max;
		this.overfill = overfill;
		this.observers = observers;
		this.capped = true;
		this.closed = false;
		this.spawns = new ArrayList<>();
		this.team = map.getScoreboard().registerNewTeam(name);
		this.team.setPrefix(getColor().toString());
		this.team.setDisplayName(getColoredName());
		this.team.setCanSeeFriendlyInvisibles(true);

		String title = getColoredName();
		if(title.length() > 16)
			title = title.substring(0, 16);
		// Log.info("'" + title + "' is " + title.length() + " characters long");
		this.player = Spork.get().getServer().getOfflinePlayer(title);
	}

	public SporkMap getMap() {
		return map;
	}

	public String getName() {
		return name;
	}

	public ChatColor getColor() {
		return color;
	}

	public ChatColor getOverhead() {
		return overhead;
	}

	public int getMax() {
		return max;
	}

	public int getOverfill() {
		return overfill;
	}

	public boolean isObservers() {
		return observers;
	}

	public boolean isReady() {
		return ready;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public boolean isCapped() {
		return capped;
	}

	public void setCapped(boolean capped) {
		this.capped = capped;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public String getColoredName() {
		return getColor() + getName();
	}

	public Team getTeam() {
		return team;
	}

	public OfflinePlayer getPlayer() {
		return player;
	}

	public ScoredObjective getScored() {
		return scored;
	}

	public void setScored(ScoredObjective objective) {
		this.scored = objective;
	}

	public void setReady(boolean value) {
		ready = value;
	}

	public List<SporkSpawn> getSpawns() {
		return spawns;
	}

	public Location getSpawn() {
		return spawns.get(NumberUtil.getRandom(0, spawns.size() - 1)).getSpawn();
	}

	public SporkSpawn getSporkSpawn() {
		return spawns.get(NumberUtil.getRandom(0, spawns.size() - 1));
	}

	public boolean canJoin(SporkPlayer player) {
		if(isObservers()) {
			return true;
		}

		if(isClosed()) {
			return false;
		}

		/*
		if(size() >= getOverfill() && isCapped()) {
			return player.getPlayer().hasPermission("manager.join.overflow");
		} else if(size() >= getMax() && isCapped()) {
			return player.getPlayer().hasPermission("manager.join.max");
		}
		*/

		return true;
	}

	public String reasonJoin(SporkPlayer player, ChatColor colour) {
		/*
		if(size() >= getOverfill() && isCapped() && !player.getPlayer().hasPermission("manager.join.overflow")) {
			return colour + "You can't join " + getColoredName() + colour + " because it has reached overflow capacity.";
		} else if(size() >= getMax() && isCapped() && !player.getPlayer().hasPermission("manager.join.max")) {
			return colour + "The teams on this map are full!";
		}
		*/

		return "";
	}

	public List<SporkPlayer> getPlayers() {
		List<SporkPlayer> players = new ArrayList<>();
		for(SporkPlayer player : SporkPlayer.getPlayers()) {
			if(player.getTeam() == this) {
				players.add(player);
			}
		}

		return players;
	}

	public int size() {
		return getPlayers().size();
	}

	public List<ObjectiveModule> getObjectives() {
		// Log.info(map.getName() + ": " + map.getObjectives().size() + " Objectives");

		List<ObjectiveModule> objectives = new ArrayList<>();
		for(ObjectiveModule objective : map.getObjectives())
			if(objective.getTeam() == this)
				objectives.add(objective);
		return objectives;
	}

	public boolean complete() {
		boolean yes = true;
		for(ObjectiveModule module : getObjectives())
			if(!module.isComplete()) {
				yes = false;
				break;
			}

		return yes;
	}

	@Override
	public String toString() {
		return "SporkTeam{name=" + name + ",color=" + color.name() + ",overhead=" + overhead.name() + "," +
				"max=" + max + ",overfill=" + overfill + ",size=" + size() + "}";
	}

}
