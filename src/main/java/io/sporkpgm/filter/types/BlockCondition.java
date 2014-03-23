package io.sporkpgm.filter.types;

import io.sporkpgm.filter.Condition;
import io.sporkpgm.filter.FilterContext;
import io.sporkpgm.filter.State;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockCondition extends Condition {

	private Material material;

	public BlockCondition(State state, Material material) {
		super(state);
		this.material = material;
	}

	@Override
	public State match(FilterContext context) {
		if(context.getBlock() != null) {
			return match(context.getBlock());
		}
		return State.ABSTAIN;
	}

	private State match(Block block) {
		return State.fromBoolean(this.material == block.getType());
	}

	public Material getMaterial() {
		return material;
	}

	@Override
	public String toString() {
		return "BlockCondition{block=" + material.toString() + ",state=" + getState().toString() + "}";
	}

}
