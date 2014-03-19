package io.sporkpgm.match;

import io.sporkpgm.map.SporkMap;

import org.bukkit.World;

public class Match {

	private SporkMap map;
	private World world;

	public Match(SporkMap map, World world) {
		this.map = map;
		this.world = world;
	}

}
