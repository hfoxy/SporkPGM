package io.sporkpgm.objective.monument;


import io.sporkpgm.Spork;
import io.sporkpgm.map.SporkMap;
import io.sporkpgm.map.event.BlockChangeEvent;
import io.sporkpgm.module.ModuleInfo;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.extras.InitModule;
import io.sporkpgm.objective.ObjectiveModule;
import io.sporkpgm.player.SporkPlayer;
import io.sporkpgm.region.types.BlockRegion;
import io.sporkpgm.region.types.CuboidRegion;
import io.sporkpgm.team.SporkTeam;

import io.sporkpgm.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;


import java.lang.reflect.Method;
import java.util.List;

@ModuleInfo(name = "MonumentObjective", description = "The DTM objective module")
public class MonumentObjective extends ObjectiveModule implements InitModule{

	String name;
	Material material;
	boolean completed;
	ChatColor color;
	SporkPlayer completer;

	CuboidRegion place;
	SporkTeam team;
	OfflinePlayer player;
	List<MonumentBlock> blocks;
	StringBuilder spaces;

	public MonumentObjective(String name,Material material,CuboidRegion place,SporkTeam team){
		this.name=name;
		this.material=material;
		this.place=place;
		this.team=team;
	}

	@Override
	public SporkTeam getTeam(){
		return team;
	}

	public String getName(){
		return name;
	}

	public Material getMaterial(){
		return material;
	}

	public SporkPlayer getCompleter(){
		return completer;
	}

	public CuboidRegion getRegion(){
		return place;
	}

	public List<MonumentBlock> getBlocks(){
		return blocks;
	}

	@Override
	public boolean isComplete(){
		return completed;
	}

	@Override
	public void setComplete(boolean complete){
	   this.completed = complete;
		if(!complete) {
			return;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(color).append(name.toUpperCase()).append(ChatColor.GRAY).append(" was completed by ");
		builder.append(ChatColor.AQUA).append(completer.getName());

		Bukkit.broadcastMessage(builder.toString());
		update();

	}

	@Override
	public OfflinePlayer getPlayer(){
		return player;
	}

	@Override
	public void update(){
		int score = 0;
		if(this.player != null) {
			score = team.getMap().getObjective().getScore(player).getScore();
			SporkMap.ScoreAPI.reset(team.getMap().getObjective().getScore(player));
		}

		this.spaces = new StringBuilder();

		String title = getStatusColour() + name + spaces.toString();
		if(title.length() > 16) {
			title = title.substring(0, 16);
		}
		this.player = Spork.get().getServer().getOfflinePlayer(title);

		while(SporkMap.ScoreAPI.isSet(team.getMap().getObjective().getScore(player))) {
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


	@EventHandler
	public void onBlockChange(BlockChangeEvent event){
		if (isComplete()){
			event.setCancelled(true);
		}
		if (completer == null){
			return;
		}
		if (completer.getTeam() == getTeam()){
			event.setCancelled(true);
			completer.getPlayer().sendMessage(ChatColor.RED + "You can't break your own monument");
			return;
		}
		for (MonumentBlock block : getBlocks()){
			if (event.getLocation().getBlock().getType().toString().equals(block.getLoc().getBlock().getType().toString())){
				block.setBroken(true);
				block.setWhoBroke(event.getPlayer());
			}
		}
		int num = 0;
		for (MonumentBlock block : getBlocks()){
			if (block.isBroken()){
				num++;
			}
		}
		if (num == getBlocks().size()){
			setComplete(true);
		}
	}

	@Override
	public ChatColor getStatusColour(){
		return (isComplete() ? ChatColor.GREEN : ChatColor.RED);
	}

	@Override
	public Class<? extends Builder> builder(){
		return MonumentBuilder.class;
	}

	@Override
	public void start(){
		for (BlockRegion region : place.getValues()){
			Log.info(region.toString());
			Log.info(String.valueOf(blocks.size()));
			if (region.getLocation(team.getMap().getWorld()).getBlock().getType().toString().equals(material.toString())){
				blocks.add(new MonumentBlock(region.getLocation(team.getMap().getWorld())));
			}

		}
	}

	@Override
	public void stop(){

	}
}
