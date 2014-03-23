package io.sporkpgm.filter.types;

import io.sporkpgm.filter.Condition;
import io.sporkpgm.filter.FilterContext;
import io.sporkpgm.filter.State;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class VoidCondition extends Condition {

	public VoidCondition(State state) {
		super(state);
	}

	@Override
	public State match(FilterContext context) {
		if(context.getBlock() == null) {
			return State.ABSTAIN;
		}
		Location loc = context.getBlock().getLocation();
		World world = loc.getWorld();
		Material mat = world.getBlockAt(loc.getBlockX(), 0, loc.getBlockZ()).getType();
		if(mat == Material.AIR) {
			return State.ALLOW;
		}
		return State.DENY;
	}

	@Override
	public String toString() {
		return "VoidCondition{state=" + getState().toString() + "}";
	}

}
