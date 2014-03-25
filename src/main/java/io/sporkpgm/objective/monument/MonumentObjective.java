package io.sporkpgm.objective.monument;


import io.sporkpgm.module.ModuleInfo;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.objective.ObjectiveModule;
import io.sporkpgm.player.SporkPlayer;
import io.sporkpgm.region.types.BlockRegion;
import io.sporkpgm.team.SporkTeam;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

@ModuleInfo(name = "MonumentObjective", description = "The DTM objective module")
public class MonumentObjective extends ObjectiveModule {

	String name;
	boolean completed;
	SporkPlayer completer;
	BlockRegion place;
	SporkTeam team;
	OfflinePlayer player;


	@Override
	public SporkTeam getTeam(){
		return team;
	}

	@Override
	public boolean isComplete(){
		return completed;
	}

	@Override
	public void setComplete(boolean complete){
	   this.completed = complete;
	}

	@Override
	public OfflinePlayer getPlayer(){
		return player;
	}

	@Override
	public void update(){

	}

	@Override
	public ChatColor getStatusColour(){
		return (isComplete() ? ChatColor.GREEN : ChatColor.RED);
	}

	@Override
	public Class<? extends Builder> builder(){
		return MonumentBuilder.class;
	}
}
