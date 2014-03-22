package io.sporkpgm.objective.victory;

import io.sporkpgm.Spork;
import io.sporkpgm.map.SporkMap.ScoreAPI;
import io.sporkpgm.match.phase.ServerPhase;
import io.sporkpgm.module.ModuleInfo;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.objective.ObjectiveModule;
import io.sporkpgm.player.SporkPlayer;
import io.sporkpgm.region.types.BlockRegion;
import io.sporkpgm.team.SporkTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.OfflinePlayer;

@ModuleInfo(name = "VictoryObjective", description = "Defines a region where blocks fall")
public class VictoryObjective extends ObjectiveModule {

	String name;
	DyeColor dye;
	ChatColor color;
	BlockRegion place;

	SporkPlayer completer;
	boolean complete;

	SporkTeam team;
	OfflinePlayer player;
	StringBuilder spaces;

	public VictoryObjective(String name, SporkTeam team, DyeColor dye, ChatColor color, BlockRegion place) {
		this.name = name;
		this.team = team;
		this.dye = dye;
		this.color = color;
		this.place = place;

		update();
	}

	@Override
	public SporkTeam getTeam() {
		return team;
	}

	@Override
	public boolean isComplete() {
		return complete;
	}

	@Override
	public void setComplete(boolean complete) {
		this.complete = complete;
		if(!complete) {
			return;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(color).append(name).append(ChatColor.GRAY).append(" was completed by ");
		builder.append(ChatColor.AQUA).append(completer.getName());

		Bukkit.broadcastMessage(builder.toString());
	}

	@Override
	public void update() {
		int score = 0;
		if(this.player != null) {
			score = team.getMap().getObjective().getScore(player).getScore();
			ScoreAPI.reset(team.getMap().getObjective().getScore(player));
		}

		this.spaces = new StringBuilder();

		String title = getStatusColour() + name + spaces.toString();
		if(title.length() > 16) {
			title = title.substring(0, 16);
		}
		this.player = Spork.get().getServer().getOfflinePlayer(title);

		while(ScoreAPI.isSet(team.getMap().getObjective().getScore(player))) {
			spaces.append(" ");

			title = getStatusColour() + name + spaces.toString();
			if(title.length() > 16) {
				String coloured = (getStatusColour() + name).substring(0, 15 - spaces.length());
				title = coloured + spaces.toString();
			}

			player = Spork.get().getServer().getOfflinePlayer(title);
		}

		team.getMap().getObjective().getScore(player).setScore(score);
	}

	@Override
	public ChatColor getStatusColour() {
		return (isComplete() ? ChatColor.GREEN : ChatColor.RED);
	}

	@Override
	public OfflinePlayer getPlayer() {
		return player;
	}

	public FireworkEffect getFirework() {
		FireworkEffect.Builder builder = FireworkEffect.builder();
		builder.withColor(ServerPhase.getColor(color));
		builder.with(Type.BALL_LARGE);
		builder.trail(false);
		builder.flicker(true);
		return builder.build();
	}

	public Class<? extends Builder> builder() {
		return VictoryBuilder.class;
	}

	@Override
	public String toString() {
		return "VictoryObjective{name=" + name + ",dye=" + dye.name() + ",color=" + color.name() + ",place=" + place.toString() + ",complete=" + complete + "}";
	}

}
