package io.sporkpgm.filter;

import io.sporkpgm.map.event.BlockChangeEvent;
import io.sporkpgm.player.SporkPlayer;
import io.sporkpgm.player.event.PlayingPlayerMoveEvent;

public class Context {

	SporkPlayer player;
	BlockChangeEvent block;
	BlockChangeEvent transformation;
	PlayingPlayerMoveEvent movement;

}
