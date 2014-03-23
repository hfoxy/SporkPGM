package io.sporkpgm.filter.types;

import io.sporkpgm.filter.Condition;
import io.sporkpgm.filter.FilterContext;
import io.sporkpgm.filter.State;
import io.sporkpgm.map.event.BlockChangeEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

public class WorldCondition extends Condition {

	public WorldCondition(String name, State state) {
		super(name, state);
	}

	@Override
	public State match(FilterContext context) {
		if(context.getCause() != null) {
			Event cause = context.getCause();
			if(cause instanceof BlockChangeEvent) {
				BlockChangeEvent change = (BlockChangeEvent) cause;
				if(change.hasPlayer()) {
					return State.DENY;
				} else {
					return State.ALLOW;
				}
			}
		}

		return State.ABSTAIN;
	}

	@Override
	public String toString() {
		return "BlockCondition{state=" + getState().toString() + "}";
	}

}
