package io.sporkpgm.filter.other;

import io.sporkpgm.filter.exceptions.InvalidContextException;
import io.sporkpgm.map.event.BlockChangeEvent;
import io.sporkpgm.player.SporkPlayer;
import io.sporkpgm.player.event.PlayingPlayerMoveEvent;

import java.util.List;

public class Context {

	SporkPlayer player;
	BlockChangeEvent block;
	BlockChangeEvent transformation;
	PlayingPlayerMoveEvent movement;

	public Context(SporkPlayer player, BlockChangeEvent block, BlockChangeEvent transformation, PlayingPlayerMoveEvent movement) {
		this.player = player;
		this.block = block;
		this.transformation = transformation;
		this.movement = movement;
	}

	public Context(List<Object> objects) throws InvalidContextException {
		for(Object object : objects) {
			fill(object);
		}
	}

	private void fill(Object object) throws InvalidContextException {
		if(object instanceof SporkPlayer) {
			this.player = (SporkPlayer) object;
		} else if(object instanceof BlockChangeEvent) {
			BlockChangeEvent event = (BlockChangeEvent) object;
			if(event.hasPlayer()) {
				this.block = event;
			} else {
				this.transformation = event;
			}
		} else if(object instanceof PlayingPlayerMoveEvent) {
			this.movement = (PlayingPlayerMoveEvent) object;
		}

		throw new InvalidContextException("Attempted to supply an Object which was unsupported");
	}

	public SporkPlayer getPlayer() {
		return player;
	}

	public boolean hasPlayer() {
		return player != null;
	}

	public BlockChangeEvent getBlock() {
		return block;
	}

	public boolean hasBlock() {
		return block != null;
	}

	public BlockChangeEvent getTransformation() {
		return transformation;
	}

	public boolean hasTransformation() {
		return transformation != null;
	}

	public PlayingPlayerMoveEvent getMovement() {
		return movement;
	}

	public boolean hasMovement() {
		return movement != null;
	}

}
