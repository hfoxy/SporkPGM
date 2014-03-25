package io.sporkpgm.objective.monument;

import io.sporkpgm.player.SporkPlayer;
import org.bukkit.Location;

/**
 * @author MasterEjay
 */
public class MonumentBlock {

	boolean broken;
	SporkPlayer whoBroke;
	Location loc;

	public void setBroken(boolean broken){
		this.broken=broken;
	}

	public void setWhoBroke(SporkPlayer whoBroke){
		this.whoBroke=whoBroke;
	}

	public boolean isBroken(){
		return broken;
	}

	public SporkPlayer getWhoBroke(){
		return whoBroke;
	}

	public Location getLoc(){
		return loc;
	}

	public MonumentBlock(Location loc){
		this.loc=loc;
	}
}
